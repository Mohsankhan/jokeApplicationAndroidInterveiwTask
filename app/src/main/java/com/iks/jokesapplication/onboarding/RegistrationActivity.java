package com.iks.jokesapplication.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.iks.jokesapplication.MainActivity;
import com.iks.jokesapplication.MyApplication;
import com.iks.jokesapplication.R;
import com.iks.jokesapplication.common.ConnectionReceiver;
import com.iks.jokesapplication.common.JokesConstants;
import com.iks.jokesapplication.common.LocalStorageController;
import com.iks.jokesapplication.common.Utils;


public class RegistrationActivity extends AppCompatActivity implements OnBoardingEventListener {

    private String email;
    private String nick;
    private String phoneNumber;
    private final boolean isChecked = false;
    private String administratorEmail;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        navController = navHostFragment.getNavController();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new ConnectionReceiver(), filter);
        loadInitialFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void loadNextActivity() {
        LocalStorageController.getInstance().updateSharedPreference(JokesConstants.APPLICATION_STATE, JokesConstants.SUCCESS_FULL_CREATED);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void loadVerificationUserFragment() {

    }

    @Override
    public void loadSetPasswordFragment() {

    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getNick() {
        return nick;
    }


    @Override
    public void hideSoftKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void backFromActivateDevice() {
    }


    private void loadInitialFragment() {
        String applicationState = LocalStorageController.getInstance().getSharedPreferenceString(JokesConstants.APPLICATION_STATE);
        if (applicationState.equalsIgnoreCase(JokesConstants.VERIFICATION_CODE_STATE)) {
            navController.popBackStack();
            navController.navigate(R.id.signInFragment);
        } else if(applicationState.equalsIgnoreCase(JokesConstants.SET_PASSWORD_STATE))  {

        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(new ConnectionReceiver.ConnectionReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                if (!isConnected) {
                    Utils.showInternetError(RegistrationActivity.this);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
