package healthcare.controller.model;

import java.util.HashSet;
import java.util.Set;

import healthcare.entity.Hospital;
import healthcare.entity.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HealthcarePatient {
	private Long patientId;
	private String patientFirstName;
	private String patientLastName;
	private Long patientAge;
	private String patientCondition;
	private String patientEmail;
	private String patientPhone;
	private Set<Hospital> hospitals = new HashSet<>();

	public HealthcarePatient(Patient patient) {
		patientId = patient.getPatientId();
		patientFirstName = patient.getPatientFirstName();
		patientLastName = patient.getPatientLastName();
		patientAge = patient.getPatientAge();
		patientCondition = patient.getPatientCondition();
		patientEmail = patient.getPatientEmail();
		patientPhone = patient.getPatientPhone();
	}

	public HealthcarePatient(Long patientId, String patientFirstName, String patientLastName, Long patientAge,
			String patientCondition, String patientEmail, String patientPhone) {
		this.patientId = patientId;
		this.patientFirstName = patientFirstName;
		this.patientLastName = patientLastName;
		this.patientAge = patientAge;
		this.patientCondition = patientCondition;
		this.patientEmail = patientEmail;
		this.patientPhone = patientPhone;
	}

	public Patient toPatient() {
		Patient patient = new Patient();

		patient.setPatientId(patientId);
		patient.setPatientFirstName(patientFirstName);
		patient.setPatientLastName(patientLastName);
		patient.setPatientAge(patientAge);
		patient.setPatientCondition(patientCondition);
		patient.setPatientEmail(patientEmail);
		patient.setPatientPhone(patientPhone);

		return patient;
	}
}
