package com.example.onboarding.process;

import com.example.onboarding.employee.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OnboardingStatusTransitionService {

    public boolean canTransition(Employee employee, OnboardingStatus targetStatus){

        OnboardingStatus current = employee.getStatus();

        return switch(targetStatus){
            case PRE_EMPLOYMENT ->
                current == OnboardingStatus.OFFER_ACCEPTED;

            case READY_FOR_FIRST_DAY ->
                current == OnboardingStatus.PRE_EMPLOYMENT;

            case IN_PROGRESS ->
                    current == OnboardingStatus.READY_FOR_FIRST_DAY
                        && LocalDate.now().equals(employee.getStartDate());

            case COMPLETED ->
                current == OnboardingStatus.IN_PROGRESS;

            default -> false;
        };
    }
}
