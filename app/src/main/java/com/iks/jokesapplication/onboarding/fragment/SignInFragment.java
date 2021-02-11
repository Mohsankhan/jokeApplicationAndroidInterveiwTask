package com.iks.jokesapplication.onboarding.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iks.jokesapplication.BaseFragment;
import com.iks.jokesapplication.MainActivity;
import com.iks.jokesapplication.R;
import com.iks.jokesapplication.common.JokesConstants;
import com.iks.jokesapplication.common.LocalStorageController;
import com.iks.jokesapplication.common.Utils;
import com.iks.jokesapplication.onboarding.OnBoardingEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInFragment extends BaseFragment {
    OnBoardingEventListener mListener;
    private NavController navController;
    private EditText emailText;
    private EditText passwordText;
    private View emailLayout;
    private View passwordLayout;
    private View logInCtaLayout;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    String user;
    private Button loginCta;
    private final View.OnClickListener ctaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.hideSoftKeyBoard();
            validateFieldsAndChecks();
        }
    };
    private final View.OnClickListener forgotPasswordLabelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle =  new Bundle();
            bundle.putInt(JokesConstants.LOGIN_BUNDLE_KEY,JokesConstants.FORGOT_PASSWORD_BUNDLE_KEY);
            navController.popBackStack();
            navController.navigate(R.id.signUpFragment,bundle);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mListener = (OnBoardingEventListener) context;
        } catch (ClassCastException exp) {
            throw new ClassCastException(context.toString() + " must implement MainActivityEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        navController = Navigation.findNavController(container);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setSpannableText(view);
    }

    void initViews(View view) {
        emailLayout = view.findViewById(R.id.email_layout);
        passwordLayout = view.findViewById(R.id.password_layout);
        TextView forgotPasswordLabel = view.findViewById(R.id.forget_password_label);
        forgotPasswordLabel.setOnClickListener(forgotPasswordLabelListener);
        TextInputLayout passwordTextInput = passwordLayout.findViewById(R.id.text_input_layout);
        TextInputLayout emailTextInput = emailLayout.findViewById(R.id.text_input_layout);
        emailTextInput.setHint(getString(R.string.prompt_email));
        passwordTextInput.setHint("Password");
        emailText = emailLayout.findViewById(R.id.edit_text);
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        passwordText = passwordLayout.findViewById(R.id.edit_text);
        passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        logInCtaLayout = view.findViewById(R.id.login);
        loginCta = logInCtaLayout.findViewById(R.id.cta);
        loginCta.setText(getString(R.string.sign_in));
        loginCta.setOnClickListener(ctaClickListener);
        emailText.setText(mListener.getEmail() == null ? "" : mListener.getEmail());

    }

    void setSpannableText(View view) {
        SpannableString notEmailYet = new SpannableString(getString(R.string.not_email_yet));
        SpannableString signUpLink = new SpannableString(getString(R.string.sign_up_link_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Bundle bundle =  new Bundle();
                bundle.putInt(JokesConstants.LOGIN_BUNDLE_KEY,JokesConstants.SIGN_UP_BUNDLE_KEY);
                navController.popBackStack();
                navController.navigate(R.id.signUpFragment,bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);
            }
        };
        signUpLink.setSpan(clickableSpan, 0, signUpLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView signUpLinkLabel = view.findViewById(R.id.sign_up_label);
        signUpLinkLabel.setText(TextUtils.concat(notEmailYet, JokesConstants.SPACE, signUpLink));
        signUpLinkLabel.setMovementMethod(LinkMovementMethod.getInstance());
        signUpLinkLabel.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onDetach() {
        mContext = null;
        super.onDetach();
    }

    private void signIn() {
        firebaseAuth.signInWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();
                        if(task.isSuccessful()){
                            //Start profile Activity
                            mListener.loadNextActivity();
                            startActivity(new Intent(mContext, MainActivity.class));
                        }else {
                            Toast.makeText(mContext, "Something wrong happened", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void validateFieldsAndChecks() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        boolean allFieldsValid = true;
        if (!Utils.isValidEmail(email)) {
            allFieldsValid = false;
            emailText.setError("Please enter valid email..");
        }
        if (!Utils.isValidPassword(password)) {
            allFieldsValid = false;
            passwordText.setError("password min length is 4 and max is 8");
        }
        if (allFieldsValid) {
            setProgressDialog();
            signIn();
        }
    }
    private void enableCta(){
        loginCta.setEnabled(true);
        loginCta.setTextColor(getContext().getResources().getColor(R.color.white));
    }
    private void disableCta(){
        loginCta.setEnabled(false);
        loginCta.setTextColor(getContext().getResources().getColor(R.color.selected_item_bg));
    }

}