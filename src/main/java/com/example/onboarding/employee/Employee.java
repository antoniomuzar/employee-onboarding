package com.example.onboarding.employee;

import com.example.onboarding.process.OnboardingStatus;
import com.example.onboarding.task.OnboardingTask;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee", schema = "onboarding")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;
    private String department;
    private String position;
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private OnboardingStatus status;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OnboardingTask> tasks = new ArrayList<>();

    @Transient
    public String getStatusBadgeClass() {
        if (status == null) return "";
        switch (status) {
            case COMPLETED:
            case IN_PROGRESS:
            case READY_FOR_FIRST_DAY:
                return "status-active";
            case OFFER_ACCEPTED:
            case PRE_EMPLOYMENT:
                return "status-pending";
            case CANCELED:
                return "status-inactive";
            default:
                return "";
        }
    }

    @Transient
    public String getStatusDisplayName() {
        if (status == null) return "";
        switch (status) {
            case COMPLETED:
            case IN_PROGRESS:
            case READY_FOR_FIRST_DAY:
                return "Active";
            case OFFER_ACCEPTED:
            case PRE_EMPLOYMENT:
                return "Pending";
            case CANCELED:
                return "Inactive";
            default:
                return status.name();
        }
    }

    @Transient
    public OnboardingStatus getNextStatus() {
        if (status == null) return null;
        switch (status) {
            case OFFER_ACCEPTED: return OnboardingStatus.PRE_EMPLOYMENT;
            case PRE_EMPLOYMENT: return OnboardingStatus.READY_FOR_FIRST_DAY;
            case READY_FOR_FIRST_DAY: return OnboardingStatus.IN_PROGRESS;
            case IN_PROGRESS: return OnboardingStatus.COMPLETED;
            default: return null; // COMPLETED i CANCELED
        }
    }
}