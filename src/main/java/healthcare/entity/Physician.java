package healthcare.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Physician {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long physicianId;
	private String physicianFirstName;
	private String physicianLastName;
	private String physicianSpecialty;
	private String physicianPhone;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "hospital_id", nullable = false)
	private Hospital hospital;
}
