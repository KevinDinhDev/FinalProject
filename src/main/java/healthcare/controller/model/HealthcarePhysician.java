package healthcare.controller.model;

import healthcare.entity.Physician;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HealthcarePhysician {
	private Long physicianId;
	private String physicianFirstName;
	private String physicianLastName;
	private String physicianSpecialty;
	private String physicianPhone;

	public HealthcarePhysician(Physician physician) {
		physicianId = physician.getPhysicianId();
		physicianFirstName = physician.getPhysicianFirstName();
		physicianLastName = physician.getPhysicianLastName();
		physicianSpecialty = physician.getPhysicianSpecialty();
		physicianPhone = physician.getPhysicianPhone();
	}

	public HealthcarePhysician(Long physicianId, String physicianFirstName, String physicianLastName,
			String physicianSpecialty, String physicianPhone) {
		this.physicianId = physicianId;
		this.physicianFirstName = physicianFirstName;
		this.physicianLastName = physicianLastName;
		this.physicianSpecialty = physicianSpecialty;
		this.physicianPhone = physicianPhone;
	}

	public Physician toPhysician() {
		Physician physician = new Physician();

		physician.setPhysicianId(physicianId);
		physician.setPhysicianFirstName(physicianFirstName);
		physician.setPhysicianLastName(physicianLastName);
		physician.setPhysicianSpecialty(physicianSpecialty);
		physician.setPhysicianPhone(physicianPhone);

		return physician;
	}
}
