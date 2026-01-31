package com.example.onboarding.employee;

import com.example.onboarding.process.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

     List<Employee> findByLastNameContainingIgnoreCase(String lastName);

     List<Employee> findByStatus(OnboardingStatus status);
}
