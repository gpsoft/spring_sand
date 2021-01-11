package jp.dip.gpsoft.springsand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByLoginId(String loginId);

	int countByLoginId(String loginId);
}
