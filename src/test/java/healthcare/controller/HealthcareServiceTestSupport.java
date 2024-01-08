package healthcare.controller;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.jdbc.JdbcTestUtils;

import healthcare.controller.model.HealthcareHospital;
import healthcare.controller.model.HealthcarePatient;
import healthcare.controller.model.HealthcarePhysician;
import healthcare.entity.Hospital;
import healthcare.entity.Patient;
import healthcare.entity.Physician;

public class HealthcareServiceTestSupport {

	private static final String PHYSICIAN_TABLE = "physician";

	private static final String HOSPITAL_PATIENT_TABLE = "hospital_patient";

	private static final String PATIENT_TABLE = "patient";

	private static final String HOSPITAL_TABLE = "hospital";

	private static final String INSERT_PATIENT_1_SQL = """
			INSERT INTO patient
			(patient_first_name, patient_last_name, patient_age, patient_condition, patient_email, patient_phone)
			VALUES ('Tommy', 'Rod', 74, 'Diabetes', 'tommyrod@outlook.com', '(384) 291-1008')
			""";

	private static final String INSERT_PATIENT_2_SQL = """
			INSERT INTO patient
			(patient_first_name, patient_last_name, patient_age, patient_condition, patient_email, patient_phone)
			VALUES ('Jessica', 'Hammel', 18, 'COPD', 'jesshammel@gmail.com', '(211) 875-3591')
			""";

	private static final String INSERT_PHYSICIAN_1_SQL = """
			INSERT INTO physician
			(physician_first_name, physician_last_name, physician_specialty, physician_phone, hospital_id)
			VALUES ('Lenny', 'Sodder', 'General Medicine', '(149) 238-2594', ?)
			""";

	private static final String INSERT_PHYSICIAN_2_SQL = """
			INSERT INTO physician
			(physician_first_name, physician_last_name, physician_specialty, physician_phone, hospital_id)
			VALUES ('Hommer', 'Sids', 'Podiatry', '(364) 369-3569', ?)
			""";

	// @formatter:off
	private HealthcareHospital insertAddress1 = new HealthcareHospital(
			1L,
			"Tom's Hospital",
			"83 Crower Street",
			"Austing",
			"Texas",
			20938L,
			"(839) 854- 1033"	
	);
	
	private HealthcareHospital insertAddress2 = new HealthcareHospital(
			2L,
			"Crok Lake's Hospital",
			"10 Winder Road",
			"Miami",
			"Florida",
			20938L,
			"(694) 326- 9123"	
	);
	
	private HealthcareHospital updateAddress1 = new HealthcareHospital(
			1L,
			"Tom Hanks Hospital",
			"83 Crower Street",
			"Austing",
			"Texas",
			20938L,
			"(849) 374- 1465"
	);
	
	private HealthcarePatient insertPatient1 = new HealthcarePatient(
			1L,
			"Warden",
			"Howl",
			28L,
			"Scoliosis",
			"wardenhowl@gmail.com",
			"(496) 732- 4761"
	);
	
	private HealthcarePatient insertPatient2 = new HealthcarePatient(
			2L,
			"Jordan",
			"Sheers",
			39L,
			"Asthma",
			"JSheers@Outlook.com",
			"(743) 418- 7469"
	);
	
	private HealthcarePatient updatePatient1 = new HealthcarePatient(
			1L,
			"Jermey",
			"Howl",
			30L,
			"Diabetes",
			"howlwarden@gmail.com",
			"(496) 732- 4762"
	);
	
	
	private HealthcarePhysician insertPhysician1 = new HealthcarePhysician(
			1L,
			"Alexander",
			"Fleming",
			"Pharmacology",
			"(856) 743- 2176"
	);
	
	private HealthcarePhysician insertPhysician2 = new HealthcarePhysician(
			2L,
			"Helen",
			"Brooke",
			"Pediatric Cardiology",
			"(542) 189- 9473"
	);
	
	private HealthcarePhysician updatePhysician1 = new HealthcarePhysician(
			1L,
			"Alexia",
			"Fleming",
			"Organic Pharmacology",
			"(856) 743- 2177"
	);
	// @formatter:on

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private HealthcareController healthcareController;

	protected HealthcareHospital buildInsertHospital(int which) {
		return which == 1 ? insertAddress1 : insertAddress2;
	}

	protected int rowsInHospitalTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, HOSPITAL_TABLE);
	}

	protected HealthcareHospital insertHospital(HealthcareHospital healthcareHospital) {
		Hospital hospital = healthcareHospital.toHospital();
		HealthcareHospital clone = new HealthcareHospital(hospital);

		clone.setHospitalId(null);
		return healthcareController.createHospital(clone);
	}

	protected HealthcareHospital retrieveHospital(Long hospitalId) {
		return healthcareController.getHospitalWithId(hospitalId);
	}

	protected List<HealthcareHospital> insertTwoHospitals() {
		HealthcareHospital hospital1 = insertHospital(buildInsertHospital(1));
		HealthcareHospital hospital2 = insertHospital(buildInsertHospital(2));

		return List.of(hospital1, hospital2);
	}

	protected List<HealthcareHospital> retrieveAllHospitals() {
		return healthcareController.getAllHospitals();
	}

	protected HealthcareHospital updateHospital(HealthcareHospital healthcareHospital) {
		return healthcareController.updateHospital(healthcareHospital.getHospitalId(), healthcareHospital);
	}

	protected HealthcareHospital buildUpdateLocation() {
		return updateAddress1;
	}

	protected HealthcarePatient insertPatient(int which, Long hospitalId) {
		String patientSql = which == 1 ? INSERT_PATIENT_1_SQL : INSERT_PATIENT_2_SQL;

		jdbcTemplate.update(patientSql);

		Long patientId = getPatientId();

		jdbcTemplate.update("INSERT INTO hospital_patient (hospital_id, patient_id) VALUES (?, ?)", hospitalId,
				patientId);

		return retrievePatient(hospitalId, patientId);
	}

	protected Long getPatientId() {
		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
	}

	protected int rowsInPatientTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, PATIENT_TABLE);
	}

	protected int rowsInHospitalPatientTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, HOSPITAL_PATIENT_TABLE);
	}

	protected void deleteHospital(Long hospitalId) {
		healthcareController.deleteHospitalById(hospitalId);
	}

	protected HealthcarePatient buildInsertPatient(int which) {
		return which == 1 ? insertPatient1 : insertPatient2;
	}

	protected HealthcarePatient insertPatientForInsertPatientTest(Long patientId, HealthcarePatient healthcarePatient) {
		Patient patient = healthcarePatient.toPatient();
		HealthcarePatient clone = new HealthcarePatient(patient);

		clone.setPatientId(null);
		return healthcareController.addAPatient(patientId, clone);
	}

	protected HealthcarePatient retrievePatient(Long hospitalId, Long patientId) {
		return healthcareController.getPatientById(hospitalId, patientId);
	}

	protected List<HealthcarePatient> insertTwoPatients(Long hospitalId) {

		HealthcarePatient patient1 = insertPatient(1, hospitalId);
		HealthcarePatient patient2 = insertPatient(2, hospitalId);

		return List.of(patient1, patient2);
	}

	protected List<HealthcarePatient> retrieveAllPatients(Long hospitalId) {
		return healthcareController.getAllPatientFromHospital(hospitalId);
	}

	protected HealthcarePatient buildUpdatePatient(Long hospitalId, Long patientId) {
		HealthcarePatient updateRequest = new HealthcarePatient();
		updateRequest.setPatientFirstName("Jermey");
		updateRequest.setPatientLastName("Howl");
		updateRequest.setPatientAge(30L);
		updateRequest.setPatientCondition("Diabetes");
		updateRequest.setPatientEmail("howlwarden@gmail.com");
		updateRequest.setPatientPhone("(496) 732- 4762");
		return healthcareController.updateAPatient(hospitalId, patientId, updateRequest);
	}

	protected HealthcarePatient updatePatient(HealthcarePatient updateRequest) {
		return updatePatient1;
	}

	protected void deletePatients(Long hospitalId) {
		List<HealthcarePatient> patients = healthcareController.getAllPatientFromHospital(hospitalId);

		for (HealthcarePatient patient : patients) {
			healthcareController.deletePatientUsingPatientIdAndHospitalId(hospitalId, patient.getPatientId());
		}
	}

	protected HealthcarePhysician buildInsertPhysician(int which) {
		return which == 1 ? insertPhysician1 : insertPhysician2;
	}

	protected HealthcarePhysician insertPhysicianForInsertPhysicianTest(Long physicianId,
			HealthcarePhysician healthcarePhysician) {
		Physician physician = healthcarePhysician.toPhysician();
		HealthcarePhysician clone = new HealthcarePhysician(physician);

		clone.setPhysicianId(null);
		return healthcareController.addAPhysician(physicianId, clone);
	}

	protected int rowsInPhysicianTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, PHYSICIAN_TABLE);
	}

	protected HealthcarePhysician retrievePhysician(Long hospitalId, Long physicianId) {
		return healthcareController.getPhysicianById(hospitalId, physicianId);
	}

	protected HealthcarePhysician insertPhysician(int which, Long hospitalId) {
		String physicianSql = which == 1 ? INSERT_PHYSICIAN_1_SQL : INSERT_PHYSICIAN_2_SQL;

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(physicianSql, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, hospitalId);
			return ps;
		}, keyHolder);

		Long physicianId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;

		if (physicianId != null) {
			return retrievePhysician(hospitalId, physicianId);
		} else {
			throw new RuntimeException("Failed to retrieve physicianId after insertion.");
		}
	}

	protected List<HealthcarePhysician> insertTwoPhysicians(Long hospitalId) {

		HealthcarePhysician physician1 = insertPhysician(1, hospitalId);
		HealthcarePhysician physician2 = insertPhysician(2, hospitalId);

		return List.of(physician1, physician2);
	}

	protected List<HealthcarePhysician> retrieveAllPhysicians(Long hospitalId) {
		return healthcareController.getAllPhysiciansFromHospital(hospitalId);
	}

	protected HealthcarePhysician buildUpdatePhysician(Long physicianId, Long hospitalId) {
		HealthcarePhysician updateRequest = new HealthcarePhysician();

		updateRequest.setPhysicianFirstName("Alexia");
		updateRequest.setPhysicianLastName("Fleming");
		updateRequest.setPhysicianSpecialty("Organic Pharmacology");
		updateRequest.setPhysicianPhone("(856) 743- 2177");

		return healthcareController.updatePhysician(hospitalId, physicianId, updateRequest);
	}

	protected HealthcarePhysician updatePhysician(HealthcarePhysician updateRequest) {
		return updatePhysician1;
	}

	protected void deletePhysicians(Long hospitalId) {
		List<HealthcarePhysician> physicians = healthcareController.getAllPhysiciansFromHospital(hospitalId);

		for (HealthcarePhysician physician : physicians) {
			healthcareController.deletePhysicianUsingPhysicianIdAndHospitalId(hospitalId, physician.getPhysicianId());
		}
	}

}
