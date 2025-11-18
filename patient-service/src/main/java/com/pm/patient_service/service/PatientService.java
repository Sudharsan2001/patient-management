package com.pm.patient_service.service;

import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

}
