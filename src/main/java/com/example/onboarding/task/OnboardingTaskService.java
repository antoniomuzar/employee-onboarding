package com.example.onboarding.task;

import com.example.onboarding.employee.Employee;
import com.example.onboarding.process.OnboardingStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OnboardingTaskService {

    private final OnboardingTaskRepository repository;

    public OnboardingTaskService(OnboardingTaskRepository onboardingTaskRepository) {
        this.repository = onboardingTaskRepository;
    }

    public void save(OnboardingTask onboardingTask) {
        repository.save(onboardingTask);
    }

    public List<OnboardingTask> getTasksForEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public void markCompleted(Long taskId) {
        OnboardingTask task = repository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setCompleted(true);
    }

    public Optional<OnboardingTask> findById(Long id) {
        return repository.findById(id);
    }

    public void createTaskForEmployee(Employee employee, String name, OnboardingTaskType onboardingTaskType) {

        OnboardingTask task = new OnboardingTask();
        task.setEmployee(employee);
        task.setType(onboardingTaskType);
        task.setName(name);
        task.setCompleted(false);
        save(task);
    }

    public boolean areAllPreEmploymentTasksCompleted(Employee employee) {
        return repository.findByEmployeeId(employee.getId()).stream()
                .allMatch(OnboardingTask::isCompleted);
    }

    @Transactional
    public void cancelTask(Long employeeId, Long taskId) {
        OnboardingTask task = repository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!task.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("Task does not belong to employee");
        }

        Employee employee = task.getEmployee();
        employee.getTasks().remove(task);

        repository.delete(task);
    }
}

