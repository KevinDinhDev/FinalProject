package healthcare.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import healthcare.entity.Patient;

public interface PatientDao extends JpaRepository<Patient, Long> {

	List<Patient> findByHospitalsHospitalId(Long hospitalId);

	Optional<Patient> findByPatientIdAndHospitalsHospitalId(Long patientId, Long hospitalId);

	void deleteByPatientIdAndHospitalsHospitalId(Long patientId, Long hospitalId);

}
