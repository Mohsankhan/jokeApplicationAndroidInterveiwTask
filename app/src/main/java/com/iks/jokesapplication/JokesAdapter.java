package com.iks.jokesapplication;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.iks.jokesapplication.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class JokesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final OnCLickListener mListener;
    private int selectedIndex = -1;
    List<Joke> jokes =  new ArrayList<>();


    public JokesAdapter(OnCLickListener listener, ArrayList<Joke> jokes) {
        this.mListener = listener;
        this.jokes = jokes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.joke_item, parent, false);
        JokeViewHolder viewHolder = new JokeViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final JokeViewHolder jokeViewHolder = (JokeViewHolder) holder;
        final Joke joke = jokes.get(position);
        jokeViewHolder.jokeTitle.setText(joke.getTitle()== null?"":joke.getTitle());
        jokeViewHolder.postingTime.setText(joke.getPostingTime()== null?"":joke.getPostingTime());
        jokeViewHolder.jokeDesc.setText(joke.getDescription()== null?"":joke.getDescription());
        jokeViewHolder.jokeLikeCount.setText(String.valueOf(joke.getLikeCount()));
        jokeViewHolder.jokeDislikeCount.setText(String.valueOf(joke.getDisLikeCount()));
        jokeViewHolder.ratingBar.setRating(joke.getRating());
        jokeViewHolder.jokeDesc.setText(joke.getDescription()== null?"":joke.getDescription());
        jokeViewHolder.jokeLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLikeClick(position);
            }
        });
        jokeViewHolder.jokeDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDislikeClick(position);
            }
        });
        jokeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(joke);
            }
        });
        jokeViewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mListener.onRatingClick(position,rating);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }


    public interface OnCLickListener {
        void onItemClick(Joke joke);
        void onLikeClick(int position);
        void  onDislikeClick(int position);
        void  onRatingClick(int position,float rating);
    }

    public interface OnLikeClickListener {
        void onItemClick(int position);
    }
    public interface OnDisLikeClickListener {
        void onItemClick(int position);
    }
    public static class JokeViewHolder extends RecyclerView.ViewHolder {
        public TextView jokeTitle;
        public TextView postingTime;
        public TextView jokeLikeCount;
        public TextView jokeDislikeCount;
        public TextView jokeDesc;
        public RatingBar ratingBar;
        public ImageView jokeLike;
        public ImageView jokeDisLike;
        public JokeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.jokeDesc = itemView.findViewById(R.id.joke_desc);
            this.postingTime = itemView.findViewById(R.id.time);
            this.ratingBar = itemView.findViewById(R.id.rating);
            this.jokeLike = itemView.findViewById(R.id.like_image);
            this.jokeDisLike = itemView.findViewById(R.id.dislike_image);
            this.jokeTitle = itemView.findViewById(R.id.title);
            this.jokeLikeCount = itemView.findViewById(R.id.like_count_label);
            this.jokeDislikeCount = itemView.findViewById(R.id.dislike_count_label);

        }
    }
    public void setList(List<Joke> jokes){
        this.jokes = jokes;
        notifyDataSetChanged();
    }

}