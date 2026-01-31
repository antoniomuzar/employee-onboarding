package com.example.onboarding.process;

import com.example.onboarding.employee.Employee;
import com.example.onboarding.employee.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OnboardingProcessService {

    private final EmployeeService employeeService;
    private final OnboardingStatusTransitionService onboardingStatusTransitionService;


    public OnboardingProcessService(EmployeeService employeeService, OnboardingStatusTransitionService onboardingStatusTransitionService) {
        this.employeeService = employeeService;
        this.onboardingStatusTransitionService = onboardingStatusTransitionService;
    }

    public void changeStatus(Long employeeId, OnboardingStatus targetStatus) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));


        if (!onboardingStatusTransitionService.canTransition(employee, targetStatus)) {
            throw new IllegalStateException(
                    "Cannot transition from " + employee.getStatus() + " to " + targetStatus
            );
        }

        employee.setStatus(targetStatus);
        employeeService.save(employee);
    }

    public void moveToNextStatus(Long employeeId) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        OnboardingStatus nextStatus = employee.getStatus().next();

        if (nextStatus == null) {
            throw new IllegalStateException("No next status available");
        }

        if (!onboardingStatusTransitionService.canTransition(employee, nextStatus)) {
            throw new IllegalStateException(
                    "Cannot transition from " + employee.getStatus() + " to " + nextStatus
            );
        }

        employee.setStatus(nextStatus);
        employeeService.save(employee);
    }
}






















