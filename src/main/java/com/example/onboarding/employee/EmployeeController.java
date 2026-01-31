package com.example.onboarding.employee;

import com.example.onboarding.process.OnboardingProcessService;
import com.example.onboarding.process.OnboardingStatus;
import com.example.onboarding.process.OnboardingStatusTransitionService;
import com.example.onboarding.task.OnboardingTask;
import com.example.onboarding.task.OnboardingTaskService;
import com.example.onboarding.task.OnboardingTaskType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final OnboardingProcessService onboardingProcessService;
    private final OnboardingStatusTransitionService onboardingStatusTransitionService;
    private final OnboardingTaskService onboardingTaskService;

    public EmployeeController(EmployeeService employeeService,
                              OnboardingProcessService onboardingProcessService,
                              OnboardingStatusTransitionService onboardingStatusTransitionService,
                              OnboardingTaskService onboardingTaskService) {
        this.employeeService = employeeService;
        this.onboardingProcessService = onboardingProcessService;
        this.onboardingStatusTransitionService = onboardingStatusTransitionService;
        this.onboardingTaskService = onboardingTaskService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String status,
                                Model model) {

        OnboardingStatus filterStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                filterStatus = OnboardingStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status filter: " + status);
            }
        }

        model.addAttribute("employees", employeeService.search(lastName, filterStatus));
        model.addAttribute("statuses", OnboardingStatus.values());
        model.addAttribute("selectedStatus", filterStatus);
        model.addAttribute("lastName", lastName);

        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("statuses", OnboardingStatus.values());
        return "employees/new";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute("employee") Employee employee) {
        if (employee.getStatus() == null) {
            employee.setStatus(OnboardingStatus.OFFER_ACCEPTED);
        }
        employeeService.create(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model){
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

        model.addAttribute("employee", employee);
        model.addAttribute("tasks", employee.getTasks());

        OnboardingStatus nextStatus = employee.getStatus().next();

        if (nextStatus != null && onboardingStatusTransitionService.canTransition(employee, nextStatus)) {
            model.addAttribute("nextStatus", nextStatus);
        } else {
            model.addAttribute("nextStatus", null);
        }

        return "employees/view";
    }

    @PostMapping("/{employeeId}/cancel")
    public String deactivateEmployee(@PathVariable Long employeeId){
        employeeService.deactivateEmployee(employeeId);
        return "redirect:/employees/" + employeeId;
    }

    @PostMapping("/{employeeId}/tasks/{taskId}/cancel")
    public String cancelTask(@PathVariable Long employeeId, @PathVariable Long taskId) {
        onboardingTaskService.cancelTask(employeeId, taskId);
        return "redirect:/employees/" + employeeId; // vrati na detalje employee
    }

    @PostMapping("/{id}/next-step")
    @ResponseBody
    public Map<String, Object> moveToNextStepAjax(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            onboardingProcessService.moveToNextStatus(id);
            response.put("status", "success");
            response.put("message", "Employee status updated successfully!");
        } catch (IllegalStateException e) {
            response.put("status", "error");
            response.put("message", "You have unfinished tasks. Please finish the tasks before you proceed to the next step.");
        }
        return response;
    }


    @PostMapping("/{id}/tasks")
    public String addTask(@PathVariable Long id,
                          @RequestParam String name,
                          @RequestParam OnboardingTaskType type) {

        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id"));

       onboardingTaskService.createTaskForEmployee(employee,name, type);

        return "redirect:/employees/" + id;
    }

    @PostMapping("/{employeeId}/tasks/{taskId}/complete")
    public String completeTask(@PathVariable Long employeeId,
                               @PathVariable Long taskId) {
        OnboardingTask task = onboardingTaskService.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        task.setCompleted(true);
        onboardingTaskService.save(task);

        return "redirect:/employees/" + employeeId;
    }
}
