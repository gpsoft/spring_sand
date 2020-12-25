package jp.dip.gpsoft.springsand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.River;

public interface RiverRepository extends JpaRepository<River, Integer> {
}
