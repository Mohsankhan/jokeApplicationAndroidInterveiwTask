package com.iks.jokesapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iks.jokesapplication.common.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddJokeDialog.JokeAddListener, JokesAdapter.OnCLickListener {
    ImageView addJoke;
    JokesAdapter jokesAdapter;
    ArrayList<Joke> arrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View myJokeTab;
    View tabHome;
    TextView topBarTitle;
    private RecyclerView allJokesList;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allJokesList = findViewById(R.id.joke_list);
        topBarTitle = findViewById(R.id.title);
        initAdapter();
        getJokes();
        tabHome = findViewById(R.id.tab_home);
        tabHome.setOnClickListener(this);
        myJokeTab = findViewById(R.id.tab_settings);
        myJokeTab.setOnClickListener(this);
        tabHome.setBackgroundColor(getResources().getColor(R.color.orange));
        myJokeTab.setBackgroundColor(getResources().getColor(R.color.black));
        topBarTitle.setText("All");
        addJoke = findViewById(R.id.record_btn);
        addJoke.setOnClickListener(this);
    }

    private void getJokes() {
        arrayList.clear();
        db.collection("jokes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().equals(FirebaseAuth.getInstance().getUid())) {
                                    Jokes jokes = document.toObject(Jokes.class);
                                    arrayList.addAll(jokes.getJokes());
                                    jokesAdapter.setList(arrayList);
                                }

                            }
                        } else {

                            Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initAdapter() {
        jokesAdapter = new JokesAdapter(this, new ArrayList<Joke>());
        allJokesList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        allJokesList.setLayoutManager(linearLayoutManager);
        allJokesList.setAdapter(jokesAdapter);
    }

    @Override
    public void onAddStudentClick(String title, String description) {
        addJoke(title, description);
    }

    @Override
    public void onItemClick(Joke joke) {

    }

    @Override
    public void onLikeClick(int position) {
        updateVote("like", position, 0);
    }

    @Override
    public void onDislikeClick(int position) {
        updateVote("dislike", position, 0);
    }

    @Override
    public void onRatingClick(int position, float ratting) {
        updateVote("rating", position, ratting);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home: {
                tabHome.setBackgroundColor(getResources().getColor(R.color.orange));
                myJokeTab.setBackgroundColor(getResources().getColor(R.color.black));
                topBarTitle.setText("All");
                getJokes();
            }
            break;
            case R.id.record_btn: {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                AddJokeDialog addStudentDialog = new AddJokeDialog(MainActivity.this::onAddStudentClick);
                addStudentDialog.show(fragmentTransaction, AddJokeDialog.ADD_STUDENT_DIALOG);
            }
            break;
            case R.id.tab_settings: {
                myJokeTab.setBackgroundColor(getResources().getColor(R.color.orange));
                tabHome.setBackgroundColor(getResources().getColor(R.color.black));
                topBarTitle.setText("My Jokes");
                getMyJokes();

               /* NavDestination currentFragment = navController.getCurrentDestination();
                if (currentFragment != null && currentFragment.getId() != R.id.myJokesFragment) {
                    navController.navigate(R.id.myJokesFragment);
                }*/

            }
            break;
        }
    }


    private void getMyJokes() {
        arrayList.clear();
        DocumentReference docRef = db.collection("jokes").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Jokes jokes = documentSnapshot.toObject(Jokes.class);
                arrayList.addAll(jokes.getJokes());
                jokesAdapter.setList(arrayList);
            }
        });
    }

    private void addJoke(String title, String description) {
        arrayList.clear();
        DocumentReference docRef = db.collection("jokes").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Jokes object = documentSnapshot.toObject(Jokes.class);
                arrayList.addAll(object.getJokes());
                Joke joke = new Joke();
                joke.setDescription(description);
                joke.setTitle(title);
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                joke.setPostingTime(currentTime);
                arrayList.add(joke);
                Jokes jokes = new Jokes();
                jokes.setJokes(arrayList);
                db.collection("jokes")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(jokes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        jokesAdapter.setList(arrayList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void updateVote(String type, int position, float rating) {
        int count = 0;
        Joke joke = arrayList.get(position);
        if (type.equals("like")) {
            count = joke.getLikeCount();
            count = count + 1;
            joke.setLikeCount(count);
        } else if (type.equals("dislike")) {
            count = joke.getDisLikeCount();
            count = count + 1;
            joke.setDisLikeCount(count);
        } else {
            joke.setRating(rating);
        }

        arrayList.set(position, joke);
        Jokes jokes = new Jokes();
        jokes.setJokes(arrayList);
        db.collection("jokes")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(jokes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                jokesAdapter.setList(arrayList);
                Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateData() {
       /* FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference complaintsRef = rootRef.collection("complaints");
        complaintsRef.whereEqualTo("title", ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("complainant_Name", "ABC");
                        complaintsRef.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });*/
    }
}