package com.example.onboarding.employee;

import com.example.onboarding.process.OnboardingStatus;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {

    public static Specification<Employee> lastNameContains(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null :
                        cb.like(cb.lower(root.get("lastName")),
                                "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Employee> hasStatus(OnboardingStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }
}
