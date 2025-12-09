package com.pm.patient_service.repository;

import com.pm.patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    //Query to check email already exists in table
    boolean existsByEmail(String email);
    //Query to search database for any patients that have email or patient we try to update
    //But it will ignore the patient we're trying to update in its search results
    boolean existsByEmailAndIdNot(String email, UUID id);
}
