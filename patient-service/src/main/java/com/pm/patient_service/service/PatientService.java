package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(PatientMapper::toDTO).toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        //checks whether the patient email id is already exists in db
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with same email already exists " + patientRequestDTO.getEmail());
        }
        //only creates patient if email is unique
        //converts request DTO to a Data entity model of Patient to save a record in table
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        //converts Model to a responseDTO to return back to controller
        PatientResponseDTO patientResponseDTO = PatientMapper.toDTO(patient);

        return patientResponseDTO;
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
       //Finding the patient with ID if patient not found throw exception
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id "+ id));

        //checks whether the another patient in db with same email as the one we try to update but with diff id and if patient exists it will throw exception
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A Patient with same email already exists " + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        //updating the changes in repository
        Patient updatedPatient = patientRepository.save(patient);

        PatientResponseDTO patientResponseDTO = PatientMapper.toDTO(updatedPatient);
        return patientResponseDTO;
    }

    public void deletePatient(UUID id){
     patientRepository.deleteById(id);

    }
}
