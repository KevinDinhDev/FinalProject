package healthcare.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.SqlConfig;

import healthcare.HealthcareApplication;
import healthcare.controller.model.HealthcareHospital;
import healthcare.controller.model.HealthcarePatient;
import healthcare.controller.model.HealthcarePhysician;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = HealthcareApplication.class)
@ActiveProfiles("test")
@SqlConfig(encoding = "utf-8")
class HealthcareControllerTest extends HealthcareServiceTestSupport {

	@Test
	void testInsertHospital() {
		// Given: a hospital request
		HealthcareHospital request = buildInsertHospital(1);
		HealthcareHospital expected = buildInsertHospital(1);
		// When: the hospital is added to the hospital table
		HealthcareHospital actual = insertHospital(request);

		// Then: the hospital reuturned is what is expected
		assertThat(actual).isEqualTo(expected);

		// And: there is one row in the hospital table
		assertThat(rowsInHospitalTable()).isOne();
	}

	@Test
	void testRetrieveHospital() {
		// Given: a hospital
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));
		HealthcareHospital expected = buildInsertHospital(1);

		// When: the hospital is retrieved by hospital ID
		HealthcareHospital actual = retrieveHospital(hospital.getHospitalId());

		// Then: the actual hospital is equal to the expected hospital
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testRetrieveAllHospitals() {
		// Given: two hospitals
		List<HealthcareHospital> expected = insertTwoHospitals();
		// When: all hospitals are retrieved
		List<HealthcareHospital> actual = retrieveAllHospitals();

		// Then: the retrieved hospitals are the same as expected
		assertThat(actual).isEqualTo(expected);

	}

	@Test
	void testUpdateHospital() {
		// Given: a Hospital and an update request
		insertHospital(buildInsertHospital(1));
		HealthcareHospital expected = buildUpdateLocation();

		// When: the Hospital is updated
		HealthcareHospital actual = updateHospital(expected);

		// Then: the Hospital is returned as expected
		assertThat(actual).isEqualTo(expected);

		// And: there is one row in the hospital table
		assertThat(rowsInHospitalTable()).isOne();

	}

	@Test
	void testDeleteHospitalWithPatients() {
		// Given: a hospital with two patients
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));
		Long hospitalId = hospital.getHospitalId();

		insertPatient(1, hospitalId);
		insertPatient(2, hospitalId);

		assertThat(rowsInHospitalTable()).isOne();
		assertThat(rowsInPatientTable()).isEqualTo(2);
		assertThat(rowsInHospitalPatientTable()).isEqualTo(2);

		// When: the Hospital is deleted
		deleteHospital(hospitalId);

		// Then: there are no Hospital, patient, or hospital_patient rows
		assertThat(rowsInHospitalTable()).isZero();
		assertThat(rowsInPatientTable()).isZero();
		assertThat(rowsInHospitalPatientTable()).isZero();

	}

	@Test
	void testInsertPatient() {
		// Creates a Hospital entity before testing testInsertPatient()
		HealthcareHospital requestHospital = buildInsertHospital(1);
		HealthcareHospital expectedHospital = buildInsertHospital(1);
		HealthcareHospital actualHospital = insertHospital(requestHospital);
		assertThat(actualHospital).isEqualTo(expectedHospital);

		// Given: a patient request
		HealthcarePatient request = buildInsertPatient(1);
		HealthcarePatient expected = buildInsertPatient(1);

		// When: the patient is added to the patient table
		HealthcarePatient actual = insertPatientForInsertPatientTest(request.getPatientId(), request);

		// Then: the patient reuturned is what is expected
		assertThat(actual).isEqualTo(expected);

		// And: there is one row in the patient table
		assertThat(rowsInPatientTable()).isOne();

	}

	@Test
	void testRetrievePatient() {
		// Creates a Hospital entity before testing testRetrievePatient()
		HealthcareHospital requestHospital = buildInsertHospital(1);
		HealthcareHospital actualHospital = insertHospital(requestHospital);

		// Ensure that the hospitalId is set explicitly and cannot be null
		Long hospitalId = actualHospital.getHospitalId();
		assertThat(hospitalId).isNotNull();

		// Given: a patient
		HealthcarePatient requestPatient = buildInsertPatient(1);
		HealthcarePatient insertedPatient = insertPatientForInsertPatientTest(hospitalId, requestPatient);

		// When: the patient is retrieved by patient ID
		HealthcarePatient retrievedPatient = retrievePatient(hospitalId, insertedPatient.getPatientId());

		// Then: the actual patient is equal to the expected patient
		assertThat(retrievedPatient).isEqualTo(requestPatient);
	}

	@Test
	void testRetrieveAllPatients() {
		// Creates a Hospital entity before testing testRetrieveAllPatients
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));

		// Given: two patients
		List<HealthcarePatient> expected = insertTwoPatients(hospital.getHospitalId());

		// When: all patients are retrieved
		List<HealthcarePatient> actual = retrieveAllPatients(hospital.getHospitalId());

		// Then: the retrieved patients are the same as expected
		assertThat(actual).isEqualTo(expected);

	}

	@Test
	void testUpdatePatient() {
		// Creates a Hospital entity before testing testUpdatePatient
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));

		// Given: a Patient and an update request
		HealthcarePatient initialPatient = insertPatient(1, hospital.getHospitalId());
		HealthcarePatient updateRequest = buildUpdatePatient(initialPatient.getPatientId(), hospital.getHospitalId());

		// When: the Patient is updated
		HealthcarePatient updatedPatient = updatePatient(updateRequest);

		// Then: the Patient is returned as expected
		assertThat(updatedPatient).isEqualTo(updateRequest);

		// And: there is one row in the Patient table
		assertThat(rowsInPatientTable()).isOne();
	}

	@Test
	void testDeleteAllPatients() {
		// Given: a Hospital with two patients
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));
		Long hospitalId = hospital.getHospitalId();

		insertPatient(1, hospitalId);
		insertPatient(2, hospitalId);

		assertThat(rowsInHospitalTable()).isOne();
		assertThat(rowsInPatientTable()).isEqualTo(2);
		assertThat(rowsInHospitalPatientTable()).isEqualTo(2);

		// When: the patients are deleted
		deletePatients(hospitalId);

		// Then: there are no patient or hospital_patient rows but Hospital will still
		// have one row
		assertThat(rowsInHospitalTable()).isOne();
		assertThat(rowsInPatientTable()).isZero();
		assertThat(rowsInHospitalPatientTable()).isZero();

	}

	@Test
	void testInsertPhysician() {
		// Creates a Hospital entity before testing testInsertPhysician()
		HealthcareHospital requestHospital = buildInsertHospital(1);
		HealthcareHospital expectedHospital = buildInsertHospital(1);
		HealthcareHospital actualHospital = insertHospital(requestHospital);
		assertThat(actualHospital).isEqualTo(expectedHospital);

		// Given: a Physician request
		HealthcarePhysician request = buildInsertPhysician(1);
		HealthcarePhysician expected = buildInsertPhysician(1);

		// When: the Physician is added to the physician table
		HealthcarePhysician actual = insertPhysicianForInsertPhysicianTest(request.getPhysicianId(), request);

		// Then: the Physician reuturned is what is expected
		assertThat(actual).isEqualTo(expected);

		// And: there is one row in the Physician table
		assertThat(rowsInPhysicianTable()).isOne();

	}

	@Test
	void testRetrievePhysician() {
		// Creates a Hospital entity before testing testRetrievePhysician()
		HealthcareHospital requestHospital = buildInsertHospital(1);
		HealthcareHospital actualHospital = insertHospital(requestHospital);

		// Ensure that the hospitalId is set explicitly and cannot be null
		Long hospitalId = actualHospital.getHospitalId();
		assertThat(hospitalId).isNotNull();

		// Given: a Physician
		HealthcarePhysician requestPhysician = buildInsertPhysician(1);
		HealthcarePhysician insertedPhysician = insertPhysicianForInsertPhysicianTest(hospitalId, requestPhysician);

		// When: the Physician is retrieved by Physician ID
		HealthcarePhysician retrievedPhysician = retrievePhysician(hospitalId, insertedPhysician.getPhysicianId());

		// Then: the actual Physician is equal to the expected Physician
		assertThat(retrievedPhysician).isEqualTo(requestPhysician);
	}

	@Test
	void testRetrieveAllPhysicians() {
		// Creates a Hospital entity before testing testRetrieveAllPhysicians
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));

		// Given: two Physicians
		List<HealthcarePhysician> expected = insertTwoPhysicians(hospital.getHospitalId());

		// When: all Physicians are retrieved
		List<HealthcarePhysician> actual = retrieveAllPhysicians(hospital.getHospitalId());

		// Then: the retrieved Physicians are the same as expected
		assertThat(actual).isEqualTo(expected);

	}

	@Test
	void testUpdatePhysician() {
		// Creates a Hospital entity before testing testUpdatePhysician
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));

		// Given: a Physician and an update request
		HealthcarePhysician initialPhysician = insertPhysician(1, hospital.getHospitalId());
		HealthcarePhysician updateRequest = buildUpdatePhysician(initialPhysician.getPhysicianId(),
				hospital.getHospitalId());

		// When: the Physician is updated
		HealthcarePhysician updatedPhysician = updatePhysician(updateRequest);

		// Then: the Physician is returned as expected
		assertThat(updatedPhysician).isEqualTo(updateRequest);

		// And: there is one row in the Physician table
		assertThat(rowsInPhysicianTable()).isOne();
	}

	@Test
	void testDeleteAllPhysicians() {
		// Given: a Hospital with two Physicians
		HealthcareHospital hospital = insertHospital(buildInsertHospital(1));
		Long hospitalId = hospital.getHospitalId();

		insertPhysician(1, hospitalId);
		insertPhysician(2, hospitalId);

		assertThat(rowsInHospitalTable()).isOne();
		assertThat(rowsInPhysicianTable()).isEqualTo(2);

		// When: the Physicians are deleted
		deletePhysicians(hospitalId);

		// Then: there are no physician rows but Hospital will still
		// have one row
		assertThat(rowsInHospitalTable()).isOne();
		assertThat(rowsInPhysicianTable()).isZero();
	}

}
