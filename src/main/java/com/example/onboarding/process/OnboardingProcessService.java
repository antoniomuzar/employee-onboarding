package com.example.onboarding.process;

import com.example.onboarding.employee.Employee;
import com.example.onboarding.employee.EmployeeService;
import com.example.onboarding.task.OnboardingTaskService;
import com.example.onboarding.task.OnboardingTaskType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OnboardingProcessService {

    private final List<PredefinedTask> PRE_EMPLOYMENT_TASKS = List.of(
            new PredefinedTask("Sign contract", OnboardingTaskType.HR),
            new PredefinedTask("Provide personal documents", OnboardingTaskType.HR),
            new PredefinedTask("Prepare laptop", OnboardingTaskType.IT)
    );

    private final EmployeeService employeeService;
    private final OnboardingStatusTransitionService onboardingStatusTransitionService;
    private final OnboardingTaskService onboardingTaskService;


    public OnboardingProcessService(EmployeeService employeeService, OnboardingStatusTransitionService onboardingStatusTransitionService, OnboardingTaskService onboardingTaskService) {
        this.employeeService = employeeService;
        this.onboardingStatusTransitionService = onboardingStatusTransitionService;
        this.onboardingTaskService = onboardingTaskService;
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

        if(nextStatus == OnboardingStatus.PRE_EMPLOYMENT){
            createPreEmploymentTasks(employee);
        }
    }

    public void createPreEmploymentTasks(Employee employee){
    PRE_EMPLOYMENT_TASKS.forEach(task->
            onboardingTaskService.createTaskForEmployee(
                    employee,
                    task.name(),
                    task.type()
            )
    );
    }
}






















