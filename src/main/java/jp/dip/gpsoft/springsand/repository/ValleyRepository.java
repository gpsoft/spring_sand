package jp.dip.gpsoft.springsand.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.dip.gpsoft.springsand.model.Valley;

public interface ValleyRepository extends JpaRepository<Valley, Integer> {
	long countByNameLike(String name);

	List<Valley> findByNameLike(String name, Pageable pageable);

	@Query(value = "SELECT * FROM valleys V WHERE (:anyName OR V.name LIKE :name)",
			countQuery = "SELECT COUNT(*) FROM valleys V WHERE (:anyName OR V.name LIKE :name)",
			nativeQuery = true)
	Page<Valley> find(@Param("anyName") boolean anyName, @Param("name") String name,
			Pageable pageable);
}
