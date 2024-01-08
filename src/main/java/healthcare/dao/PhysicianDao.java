package healthcare.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import healthcare.entity.Physician;

public interface PhysicianDao extends JpaRepository<Physician, Long> {

	List<Physician> findByHospitalHospitalId(Long hospitalId);

	Optional<Physician> findByPhysicianIdAndHospitalHospitalId(Long physicianId, Long hospitalId);

	void deleteByPhysicianIdAndHospitalHospitalId(Long physicianId, Long hospitalId);

}
