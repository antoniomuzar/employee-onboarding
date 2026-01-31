package com.example.onboarding.employee;

import com.example.onboarding.process.OnboardingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository repository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.repository = employeeRepository;
    }

    public void create(Employee employee){
        employee.setStatus(OnboardingStatus.OFFER_ACCEPTED);
        repository.save(employee);
    }

    public List<Employee> findAll(){
        return repository.findAll();
    }

    public Optional<Employee> findById(Long id){
        return repository.findById(id);
    }

    public List<Employee> search(String lastName, OnboardingStatus status) {

        return repository.findAll(
                Specification.where(EmployeeSpecifications.lastNameContains(lastName))
                        .and(EmployeeSpecifications.hasStatus(status))
        );
    }

    public void save(Employee employee){
        repository.save(employee);
    }

    public void deactivateEmployee(Long employeeId){
        Employee employee = repository.findById(employeeId)
                .orElseThrow(()-> new IllegalArgumentException("Employee not found"));

        employee.setStatus(OnboardingStatus.CANCELED);
        repository.save(employee);
    }
}

























