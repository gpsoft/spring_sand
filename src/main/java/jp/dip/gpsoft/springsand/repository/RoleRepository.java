package jp.dip.gpsoft.springsand.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.dip.gpsoft.springsand.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
}
