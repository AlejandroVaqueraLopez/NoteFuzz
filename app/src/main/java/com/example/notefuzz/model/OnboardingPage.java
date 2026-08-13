package com.example.notefuzz.model;

public class OnboardingPage {

    private final int imageRes;
    private final int titleRes;
    private final int descriptionRes;

    public OnboardingPage(int imageRes, int titleRes, int descriptionRes) {
        this.imageRes = imageRes;
        this.titleRes = titleRes;
        this.descriptionRes = descriptionRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }
}
