package com.iks.jokesapplication.onboarding;


public interface OnBoardingEventListener {

    void loadNextActivity();
    void loadVerificationUserFragment();
    void loadSetPasswordFragment();
    void hideSoftKeyBoard();
    String getEmail();
    String getPhoneNumber();
    String getNick();
    void backFromActivateDevice();

}
