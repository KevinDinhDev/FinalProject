package healthcare.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import healthcare.controller.model.HealthcareHospital;
import healthcare.controller.model.HealthcarePatient;
import healthcare.controller.model.HealthcarePhysician;
import healthcare.dao.HospitalDao;
import healthcare.dao.PatientDao;
import healthcare.dao.PhysicianDao;
import healthcare.entity.Hospital;
import healthcare.entity.Patient;
import healthcare.entity.Physician;

@Service
public class HealthcareService {

	@Autowired
	private HospitalDao hospitalDao;

	@Autowired
	private PhysicianDao physicianDao;

	@Autowired
	private PatientDao patientDao;

	@Transactional(readOnly = false)
	public HealthcareHospital saveHospital(HealthcareHospital healthcareHospital) {
		Long hospitalId = healthcareHospital.getHospitalId();
		Hospital hospital = findOrCreateHospital(hospitalId);

		copyHospitalFields(hospital, healthcareHospital);
		return new HealthcareHospital(hospitalDao.save(hospital));

	}

	private Hospital findOrCreateHospital(Long hospitalId) {
		if (hospitalId == null) {
			return new Hospital();
		} else {
			return findHospitalById(hospitalId);
		}
	}

	private Hospital findHospitalById(Long hospitalId) {
		return hospitalDao.findById(hospitalId).orElseThrow(
				() -> new NoSuchElementException("Hospital with the ID = " + hospitalId + " was not found."));
	}

	private void copyHospitalFields(Hospital hospital, HealthcareHospital healthcareHospital) {
		hospital.setHospitalName(healthcareHospital.getHospitalName());
		hospital.setHospitalAddress(healthcareHospital.getHospitalAddress());
		hospital.setHospitalCity(healthcareHospital.getHospitalCity());
		hospital.setHospitalState(healthcareHospital.getHospitalState());
		hospital.setHospitalZip(healthcareHospital.getHospitalZip());
		hospital.setHospitalPhone(healthcareHospital.getHospitalPhone());
	}

	@Transactional(readOnly = true)
	public List<HealthcareHospital> retrieveAllHospitals() {
		List<Hospital> hospitals = hospitalDao.findAll();
		List<HealthcareHospital> result = new LinkedList<>();

		for (Hospital hospital : hospitals) {
			HealthcareHospital hs = new HealthcareHospital(hospital);

			hs.getPatients().clear();
			hs.getPhysicians().clear();

			result.add(hs);
		}

		return result;
	}

	@Transactional(readOnly = true)
	public HealthcareHospital retrieveHospitalById(Long hospitalId) {
		Hospital hospital = findHospitalById(hospitalId);

		if (hospital != null) {
			HealthcareHospital healthcareHospital = new HealthcareHospital();
			healthcareHospital.setHospitalId(hospital.getHospitalId());
			healthcareHospital.setHospitalName(hospital.getHospitalName());
			healthcareHospital.setHospitalAddress(hospital.getHospitalAddress());
			healthcareHospital.setHospitalCity(hospital.getHospitalCity());
			healthcareHospital.setHospitalState(hospital.getHospitalState());
			healthcareHospital.setHospitalZip(hospital.getHospitalZip());
			healthcareHospital.setHospitalPhone(hospital.getHospitalPhone());

			return healthcareHospital;
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void deleteHospitalById(Long hospitalId) {
		Hospital hospital = findHospitalById(hospitalId);
		hospitalDao.delete(hospital);

	}

	@Transactional(readOnly = false)
	public HealthcarePhysician savePhysician(Long hospitalId, HealthcarePhysician healthcarePhysician) {
		Hospital hospital = findHospitalById(hospitalId);
		Physician physician = findOrCreatePhysician(hospitalId, healthcarePhysician.getPhysicianId());

		copyEmployeeFields(physician, healthcarePhysician);
		physician.setHospital(hospital);

		hospital.getPhysicians().add(physician);

		return new HealthcarePhysician(physicianDao.save(physician));
	}

	private Physician findOrCreatePhysician(Long hospitalId, Long physicianId) {
		if (physicianId == null) {
			return new Physician();
		} else {
			return findPhysicianById(hospitalId, physicianId);
		}
	}

	private Physician findPhysicianById(Long hospitalId, Long physicianId) {
		Physician physician = physicianDao.findById(physicianId).orElseThrow(
				() -> new NoSuchElementException("Physician with ID= " + physicianId + " could not be found."));

		if (!(physician.getHospital().getHospitalId() == hospitalId)) {
			throw new IllegalArgumentException(
					"Physician with ID= " + physicianId + " does not belong to the Hospital with ID= " + hospitalId);
		}
		return physician;
	}

	private void copyEmployeeFields(Physician physician, HealthcarePhysician healthcarePhysician) {
		physician.setPhysicianFirstName(healthcarePhysician.getPhysicianFirstName());
		physician.setPhysicianLastName(healthcarePhysician.getPhysicianLastName());
		physician.setPhysicianSpecialty(healthcarePhysician.getPhysicianSpecialty());
		physician.setPhysicianPhone(healthcarePhysician.getPhysicianPhone());

	}

	@Transactional(readOnly = true)
	public List<HealthcarePhysician> retrievePhysiciansByHospitalId(Long hospitalId) {
		List<Physician> physicians = physicianDao.findByHospitalHospitalId(hospitalId);
		List<HealthcarePhysician> results = new LinkedList<>();

		for (Physician physician : physicians) {
			results.add(new HealthcarePhysician(physician));
		}
		return results;
	}

	@Transactional(readOnly = true)
	public HealthcarePhysician retrievePhysicianByIdWithHospitalId(Long physicianId, Long hospitalId) {
		Optional<Physician> optionalPhysician = physicianDao.findByPhysicianIdAndHospitalHospitalId(physicianId,
				hospitalId);

		return optionalPhysician.map(HealthcarePhysician::new).orElse(null);
	}

	@Transactional(readOnly = false)
	public void deletePhysicianByPhysicianIdAndHospitalId(Long hospitalId, Long physicianId) {
		physicianDao.deleteByPhysicianIdAndHospitalHospitalId(physicianId, hospitalId);

	}

	@Transactional(readOnly = false)
	public HealthcarePatient savePatient(Long hospitalId, HealthcarePatient healthcarePatient) {
		Hospital hospital = findHospitalById(hospitalId);
		Patient patient = findOrCreatePatient(hospitalId, healthcarePatient.getPatientId());

		copyPatientFields(patient, healthcarePatient);
		addHospitalToPatient(patient, hospital);

		hospital.getPatients().add(patient);

		return new HealthcarePatient(patientDao.save(patient));
	}

	private void addHospitalToPatient(Patient patient, Hospital hospital) {
		patient.getHospitals().add(hospital);
	}

	private Patient findOrCreatePatient(Long hospitalId, Long patientId) {
		if (patientId == null) {
			return new Patient();
		} else {
			return findPatientById(hospitalId, patientId);
		}
	}

	private Patient findPatientById(Long hospitalId, Long patientId) {
		Patient patient = patientDao.findById(patientId).orElseThrow(
				() -> new NoSuchElementException("Patient with ID= " + patientId + " could not be found."));

		for (Hospital hospital : patient.getHospitals()) {
			if (hospital.getHospitalId() == (hospitalId)) {
				return patient;
			}
		}
		throw new IllegalArgumentException(
				"Patient with ID= " + patientId + " is not associated with Hospital ID= " + hospitalId);
	}

	private void copyPatientFields(Patient patient, HealthcarePatient healthcarePatient) {
		patient.setPatientFirstName(healthcarePatient.getPatientFirstName());
		patient.setPatientLastName(healthcarePatient.getPatientLastName());
		patient.setPatientAge(healthcarePatient.getPatientAge());
		patient.setPatientCondition(healthcarePatient.getPatientCondition());
		patient.setPatientEmail(healthcarePatient.getPatientEmail());
		patient.setPatientPhone(healthcarePatient.getPatientPhone());
	}

	@Transactional(readOnly = true)
	public List<HealthcarePatient> retrievePatientsByHospitalId(Long hospitalId) {
		List<Patient> patients = patientDao.findByHospitalsHospitalId(hospitalId);
		List<HealthcarePatient> results = new LinkedList<>();

		for (Patient patient : patients) {
			results.add(new HealthcarePatient(patient));
		}
		return results;
	}

	@Transactional(readOnly = true)
	public HealthcarePatient retrievePatientByIdWithHospitalId(Long patientId, Long hospitalId) {
		Optional<Patient> optionalPatient = patientDao.findByPatientIdAndHospitalsHospitalId(patientId, hospitalId);

		return optionalPatient.map(HealthcarePatient::new).orElse(null);
	}

	@Transactional(readOnly = false)
	public void deletePatientByPatientIdAndHospitalId(Long hospitalId, Long patientId) {
		Patient patient = patientDao.findById(patientId).orElse(null);

		if (patient != null) {

			patient.getHospitals().forEach(hospital -> hospital.getPatients().remove(patient));

			patient.getHospitals().clear();

			patientDao.delete(patient);
		}
	}
}
