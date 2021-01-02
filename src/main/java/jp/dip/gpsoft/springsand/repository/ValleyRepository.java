package jp.dip.gpsoft.springsand.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.Valley;

public interface ValleyRepository extends JpaRepository<Valley, Integer> {
	List<Valley> findByNameLike(String name);
}
