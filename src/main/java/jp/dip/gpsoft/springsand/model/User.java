package jp.dip.gpsoft.springsand.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import jp.dip.gpsoft.springsand.Role;

@Entity
@Table(name = "users")
public class User {
	private static final String ROLE_SEPARATOR = ",";

	@Id
	@GeneratedValue
	private Integer id;

	private String loginId;
	private String password;
	private String roles;

	// FIX: CLOBって、H2専用かなぁ?
	@Lob
	@Column(columnDefinition = "CLOB NOT NULL DEFAULT ''")
	private String avatar;

	public User() {
	}

	public User(String loginId, String pwEncoded, Role role) {
		this(loginId, pwEncoded, new HashSet<>(Arrays.asList(role)), "");
	}

	public User(String loginId, String pwEncoded, Set<Role> roles, String avatar) {
		this.loginId = loginId;
		password = pwEncoded;
		this.roles = roles2RoleStrs(roles);
		this.avatar = avatar;
	}

	static public String roles2RoleStrs(Set<Role> roles) {
		return roles.stream().map(Role::name).collect(Collectors.joining(ROLE_SEPARATOR));
	}

	public Set<Role> getRoleSet() {
		if (roles.isEmpty()) return new HashSet<Role>();

		return Arrays.asList(roles.split(ROLE_SEPARATOR)).stream().map(Role::valueOf).collect(
				Collectors.toSet());
	}

	// ------------- generated getters and setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
