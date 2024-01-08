package healthcare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import healthcare.entity.Hospital;

public interface HospitalDao extends JpaRepository<Hospital, Long> {
}
