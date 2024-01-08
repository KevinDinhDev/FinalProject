package healthcare.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hospitalId;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String hospitalState;
	private Long hospitalZip;
	private String hospitalPhone;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	@JoinTable(name = "hospital_patient", joinColumns = @JoinColumn(name = "hospital_id"), inverseJoinColumns = @JoinColumn(name = "patient_id"))
	private Set<Patient> patients = new HashSet<>();

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Physician> physicians = new HashSet<>();
}
