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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iks.jokesapplication.BaseFragment;
import com.iks.jokesapplication.R;
import com.iks.jokesapplication.common.JokesConstants;
import com.iks.jokesapplication.common.Utils;
import com.iks.jokesapplication.onboarding.OnBoardingEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpFragment extends BaseFragment {

    OnBoardingEventListener mListener;
    Button signUpCta;
    private NavController navController;
    private EditText emailText;
    private View signUpCtaView;
    private int loginType;
    private EditText confirmPasswordField;
    private EditText nameText;
    private EditText passwordText;
    private TextInputLayout passwordTextInput;
    private TextInputLayout confirmPasswordTextInput;
    private View confirmPasswordLayout;
    private View passwordLayout;
    private View logInCtaLayout;
    private View emailLayout;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private final View.OnClickListener ctaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            validateFieldsAndChecks();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        navController = Navigation.findNavController(container);
        firebaseAuth = FirebaseAuth.getInstance();
        loginType = getArguments().getInt(JokesConstants.LOGIN_BUNDLE_KEY);
        databaseReference =  FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setSpannableText(view);
    }

    private void initViews(View view) {
        emailLayout = view.findViewById(R.id.email_layout_login);
        emailText = emailLayout.findViewById(R.id.edit_text);
        TextInputLayout emailTextInput = emailLayout.findViewById(R.id.text_input_layout);

        passwordLayout = view.findViewById(R.id.password_layout);
        confirmPasswordLayout = view.findViewById(R.id.confirm_password);


        passwordText = passwordLayout.findViewById(R.id.edit_text);
        confirmPasswordField = confirmPasswordLayout.findViewById(R.id.edit_text);


        passwordTextInput = passwordLayout.findViewById(R.id.text_input_layout);
        confirmPasswordTextInput = confirmPasswordLayout.findViewById(R.id.text_input_layout);

        passwordTextInput.setHint("Password");
        confirmPasswordTextInput.setHint("ConfirmPassword");
        emailTextInput.setHint(getString(R.string.prompt_email));
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        signUpCtaView = view.findViewById(R.id.sign_up);
        signUpCta = signUpCtaView.findViewById(R.id.cta);
        if (loginType == JokesConstants.SIGN_UP_BUNDLE_KEY) {
            signUpCta.setText(getString(R.string.sign_up));
        } else {
            TextView heading = view.findViewById(R.id.signup_heading);
            heading.setText(R.string.forgot_password_label);
            TextView description = view.findViewById(R.id.description_sign_up);
            description.setText(R.string.signup_description_label);
            signUpCta.setText(R.string.send_email_cta_label);
        }
        signUpCta.setOnClickListener(ctaClickListener);
    }

    private void setSpannableText(View view) {
        SpannableString notEmailYet = new SpannableString(getString(R.string.have_account));
        SpannableString signUpLink = new SpannableString(getString(R.string.sign_in_link_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                navController.popBackStack();
                navController.navigate(R.id.signInFragment);
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



    private void validateFieldsAndChecks() {
        String email = emailText.getText().toString();
        boolean allFieldsValid = true;

        if (!Utils.isValidEmail(email)) {
            allFieldsValid = false;
            emailText.setError("Please enter valid email..");
        }
        if (allFieldsValid) {
            setProgressDialog();
            disableCta();
            if (loginType == JokesConstants.SIGN_UP_BUNDLE_KEY) {
                registerUser();
            } else {
                //forgotPassword();
            }
        }
    }

    private void enableCta() {
        signUpCta.setEnabled(true);
        signUpCta.setTextColor(getContext().getResources().getColor(R.color.white));
    }

    private void disableCta() {
        signUpCta.setEnabled(false);
        signUpCta.setTextColor(getContext().getResources().getColor(R.color.selected_item_bg));
    }
    private void registerUser(){

        String email_tv = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confermpassword = confirmPasswordField.getText().toString().trim();

        if(TextUtils.isEmpty(email_tv))
        {
            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            //finish();
            //startActivity(new Intent(getApplication(),Dashboard.class));
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(mContext,"Enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(confermpassword))
        {
            Toast.makeText(mContext,"Enter Conform password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confermpassword))
        {
            Toast.makeText(mContext,"Password Not Match",Toast.LENGTH_SHORT).show();
            //finish();
            //startActivity(new Intent(getApplication(),Dashboard.class));
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email_tv,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();
                        if(task.isSuccessful()){
                            Toast.makeText(mContext,"Reg Success",Toast.LENGTH_SHORT).show();
                            mListener.loadNextActivity();
                        }else {
                            Toast.makeText(mContext,"not reg pass",Toast.LENGTH_SHORT).show();
                        }
                    }

                });


    }
}