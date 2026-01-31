package com.example.onboarding.task;

import com.example.onboarding.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "onboarding_task", schema = "onboarding")
public class OnboardingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private OnboardingTaskType type;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
