package jp.dip.gpsoft.springsand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.Lake;

public interface LakeRepository extends JpaRepository<Lake, Integer> {
}
