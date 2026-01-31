package com.example.onboarding.process;

public enum OnboardingStatus {

    OFFER_ACCEPTED,
    PRE_EMPLOYMENT,
    READY_FOR_FIRST_DAY,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public boolean canTransitionTO(OnboardingStatus target){
        return switch (this){
            case OFFER_ACCEPTED -> target == PRE_EMPLOYMENT;
            case PRE_EMPLOYMENT -> target == IN_PROGRESS;
            case IN_PROGRESS -> target == COMPLETED;
            default -> false;
        };
    }

    public OnboardingStatus next() {
        return switch (this) {
            case OFFER_ACCEPTED -> PRE_EMPLOYMENT;
            case PRE_EMPLOYMENT -> READY_FOR_FIRST_DAY;
            case READY_FOR_FIRST_DAY -> IN_PROGRESS;
            case IN_PROGRESS -> COMPLETED;
            default -> null;
        };
    }
}
