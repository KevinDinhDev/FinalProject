package healthcare.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import healthcare.controller.model.HealthcareHospital;
import healthcare.controller.model.HealthcarePatient;
import healthcare.controller.model.HealthcarePhysician;
import healthcare.service.HealthcareService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/healthcare")
@Slf4j
public class HealthcareController {

	@Autowired
	private HealthcareService healthcareService;

	@PostMapping("/hospital")
	@ResponseStatus(code = HttpStatus.CREATED)
	public HealthcareHospital createHospital(@RequestBody HealthcareHospital healthcareHospital) {
		log.info("Creating hospital {}", healthcareHospital);
		return healthcareService.saveHospital(healthcareHospital);
	}

	@PutMapping("/hospital/{hospitalId}")
	public HealthcareHospital updateHospital(@PathVariable Long hospitalId,
			@RequestBody HealthcareHospital healthcareHospital) {
		healthcareHospital.setHospitalId(hospitalId);
		log.info("Updating a hospital {}", healthcareHospital);
		return healthcareService.saveHospital(healthcareHospital);
	}

	@GetMapping("/hospital")
	public List<HealthcareHospital> getAllHospitals() {
		log.info("Retrieving all Hospitals");
		return healthcareService.retrieveAllHospitals();
	}

	@GetMapping("/hospital/{hospitalId}")
	public HealthcareHospital getHospitalWithId(@PathVariable Long hospitalId) {
		log.info("Retrieving Hospital with ID= " + hospitalId);
		return healthcareService.retrieveHospitalById(hospitalId);
	}

	@DeleteMapping("/hospital/{hospitalId}")
	public Map<String, String> deleteHospitalById(@PathVariable Long hospitalId) {
		log.info("Deleting hospital with ID = ", hospitalId);
		healthcareService.deleteHospitalById(hospitalId);
		return Map.of("Message", "Deletion of Hospital with ID= " + hospitalId + " was successful.");
	}

	@PostMapping("/hospital/{hospitalId}/physician")
	@ResponseStatus(code = HttpStatus.CREATED)
	public HealthcarePhysician addAPhysician(@PathVariable Long hospitalId,
			@RequestBody HealthcarePhysician healthcarePhysician) {
		log.info("Creating a Physician to Hospital ID = {} , Physician: {}", hospitalId, healthcarePhysician);
		return healthcareService.savePhysician(hospitalId, healthcarePhysician);
	}

	@PutMapping("/hospital/{hospitalId}/physician/{physicianId}")
	public HealthcarePhysician updatePhysician(@PathVariable Long hospitalId, @PathVariable Long physicianId,
			@RequestBody HealthcarePhysician healthcarePhysician) {
		log.info("Updating a Physician {}", healthcarePhysician);
		healthcarePhysician.setPhysicianId(physicianId);
		return healthcareService.savePhysician(hospitalId, healthcarePhysician);
	}

	@GetMapping("/hospital/{hospitalId}/physician")
	public List<HealthcarePhysician> getAllPhysiciansFromHospital(@PathVariable Long hospitalId) {
		log.info("Retrieving all Physicians under the Hospital with ID= ", hospitalId);
		return healthcareService.retrievePhysiciansByHospitalId(hospitalId);
	}

	@GetMapping("/hospital/{hospitalId}/physician/{physicianId}")
	public HealthcarePhysician getPhysicianById(@PathVariable Long hospitalId, @PathVariable Long physicianId) {
		log.info("Retrieving Physician with ID= " + physicianId + " with the Hospital ID= " + hospitalId);
		HealthcarePhysician physician = healthcareService.retrievePhysicianByIdWithHospitalId(physicianId, hospitalId);

		if (physician != null) {
			return physician;
		} else {
			return null;
		}
	}

	@DeleteMapping("/hospital/{hospitalId}/physician/{physicianId}")
	public Map<String, String> deletePhysicianUsingPhysicianIdAndHospitalId(@PathVariable Long hospitalId,
			@PathVariable Long physicianId) {
		log.info("Deleting Physician with ID= " + physicianId + " with the Hospital ID= " + hospitalId);
		healthcareService.deletePhysicianByPhysicianIdAndHospitalId(hospitalId, physicianId);
		return Map.of("Message", "Deletion of Physician with ID= " + physicianId + " from Hospital ID= " + hospitalId
				+ " was successful.");
	}

	@PostMapping("/hospital/{hospitalId}/patient")
	@ResponseStatus(code = HttpStatus.CREATED)
	public HealthcarePatient addAPatient(@PathVariable Long hospitalId,
			@RequestBody HealthcarePatient healthcarePatient) {
		log.info("Creating a Patient to the Hospital ID= {}, Patient: {}", hospitalId, healthcarePatient);
		return healthcareService.savePatient(hospitalId, healthcarePatient);
	}

	@PutMapping("/hospital/{hospitalId}/patient/{patientId}")
	public HealthcarePatient updateAPatient(@PathVariable Long hospitalId, @PathVariable Long patientId,
			@RequestBody HealthcarePatient healthcarePatient) {
		log.info("Updating a Patient {}", healthcarePatient);
		healthcarePatient.setPatientId(patientId);
		return healthcareService.savePatient(hospitalId, healthcarePatient);
	}

	@GetMapping("/hospital/{hospitalId}/patient")
	public List<HealthcarePatient> getAllPatientFromHospital(@PathVariable Long hospitalId) {
		log.info("Retrieving all Patients under the Hospital with ID= ", hospitalId);
		return healthcareService.retrievePatientsByHospitalId(hospitalId);
	}

	@GetMapping("/hospital/{hospitalId}/patient/{patientId}")
	public HealthcarePatient getPatientById(@PathVariable Long hospitalId, @PathVariable Long patientId) {
		log.info("Retrieving Patient with ID= " + patientId + " with the Hospital ID= " + hospitalId);
		HealthcarePatient patient = healthcareService.retrievePatientByIdWithHospitalId(patientId, hospitalId);

		if (patient != null) {
			return patient;
		} else {
			return null;
		}
	}

	@DeleteMapping("/hospital/{hospitalId}/patient/{patientId}")
	public Map<String, String> deletePatientUsingPatientIdAndHospitalId(@PathVariable Long hospitalId,
			@PathVariable Long patientId) {
		log.info("Delete Patient with ID= " + patientId + " with the Hospital ID= " + hospitalId);
		healthcareService.deletePatientByPatientIdAndHospitalId(hospitalId, patientId);
		return Map.of("Message",
				"Deletion of Patient with ID= " + patientId + " from HospitalId= " + hospitalId + " was successful.");
	}
}
