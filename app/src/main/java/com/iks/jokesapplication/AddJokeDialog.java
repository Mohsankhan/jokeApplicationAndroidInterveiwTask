package com.iks.jokesapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.iks.jokesapplication.common.Utils;

public class AddJokeDialog extends DialogFragment {
    public final static String ADD_STUDENT_DIALOG = "ADD_STUDENT_DIALOG";
    public static boolean shown = false;
    private Context mContext;
    private String notificationType;
    EditText jokeTile;
    EditText jokeDescription;
    Button cta;
    private AlertDialog internetAlertDialog;

    JokeAddListener jokeAddListener;
    private View.OnClickListener addStudentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            validateFieldsAndChecks();
        }
    };
    private View.OnClickListener closeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };
    public AddJokeDialog(JokeAddListener addListener) {
        this.jokeAddListener = (JokeAddListener) addListener;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_student, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        jokeTile = view.findViewById(R.id.joke_title_field);
        jokeDescription = view.findViewById(R.id.joke_desc_field);
        cta = view.findViewById(R.id.add_student_cta);
        View closeButton = view.findViewById(R.id.close_button);
        cta.setOnClickListener(addStudentListener);
        closeButton.setOnClickListener(closeButtonListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
    private void validateFieldsAndChecks() {
        String jokeTitleText = jokeTile.getText().toString();
        String jokeDescriptionText = jokeDescription.getText().toString();

        boolean allFieldsValid = true;
        if (jokeTitleText.isEmpty()) {
            allFieldsValid = false;
            jokeTile.setError("Please enter joke title");
        }
        if (jokeDescriptionText.isEmpty()) {
            allFieldsValid = false;
            jokeDescription.setError("Please enter joke Desc");
        }
        if (allFieldsValid) {
            Utils.hideSoftKeyBoard(getView(),getContext());
            jokeAddListener.onAddStudentClick(jokeTitleText,jokeDescriptionText);
            dismiss();
        }
    }
    public interface JokeAddListener {
        void onAddStudentClick(String name ,String email);
    }

    private void enableCta(){
        cta.setTextColor(getContext().getResources().getColor(R.color.white));
        cta.setEnabled(true);
    }
    private void disableCta(){
        cta.setTextColor(getContext().getResources().getColor(R.color.selected_item_bg));
        cta.setEnabled(false);
    }
}
