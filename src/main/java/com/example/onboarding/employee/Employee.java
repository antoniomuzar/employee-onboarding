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
}
