package healthcare.controller.model;

import java.util.HashSet;
import java.util.Set;

import healthcare.entity.Hospital;
import healthcare.entity.Patient;
import healthcare.entity.Physician;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HealthcareHospital {
	private Long hospitalId;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String hospitalState;
	private Long hospitalZip;
	private String hospitalPhone;
	private Set<HealthcarePatient> patients = new HashSet<>();
	private Set<HealthcarePhysician> physicians = new HashSet<>();

	public HealthcareHospital(Hospital hospital) {
		hospitalId = hospital.getHospitalId();
		hospitalName = hospital.getHospitalName();
		hospitalAddress = hospital.getHospitalAddress();
		hospitalCity = hospital.getHospitalCity();
		hospitalState = hospital.getHospitalState();
		hospitalZip = hospital.getHospitalZip();
		hospitalPhone = hospital.getHospitalPhone();

		for (Patient patient : hospital.getPatients()) {
			patients.add(new HealthcarePatient(patient));
		}

		for (Physician physician : hospital.getPhysicians()) {
			physicians.add(new HealthcarePhysician(physician));
		}
	}

	public HealthcareHospital(Long hospitalId, String hospitalName, String hospitalAddress, String hospitalCity,
			String hospitalState, Long hospitalZip, String hospitalPhone) {
		this.hospitalId = hospitalId;
		this.hospitalName = hospitalName;
		this.hospitalAddress = hospitalAddress;
		this.hospitalCity = hospitalCity;
		this.hospitalState = hospitalState;
		this.hospitalZip = hospitalZip;
		this.hospitalPhone = hospitalPhone;
	}

	public Hospital toHospital() {
		Hospital hospital = new Hospital();

		hospital.setHospitalId(hospitalId);
		hospital.setHospitalName(hospitalName);
		hospital.setHospitalAddress(hospitalAddress);
		hospital.setHospitalCity(hospitalCity);
		hospital.setHospitalState(hospitalState);
		hospital.setHospitalZip(hospitalZip);
		hospital.setHospitalPhone(hospitalPhone);

		return hospital;
	}

}
