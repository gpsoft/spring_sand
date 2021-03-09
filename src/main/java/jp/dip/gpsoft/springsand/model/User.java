package jp.dip.gpsoft.springsand.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String loginId;
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	// FIX: CLOBって、H2専用かなぁ?
	@Lob
	@Column(columnDefinition = "CLOB NOT NULL DEFAULT ''")
	private String avatar;

	public User() {
		avatar = "";
		roles = new HashSet<>();
	}

	public User(String loginId, String pwEncoded, String role) {
		this(loginId, pwEncoded, new HashSet<>(Arrays.asList(new Role(role))), "");
	}

	public User(String loginId, String pwEncoded, Set<Role> roles, String avatar) {
		this.loginId = loginId;
		password = pwEncoded;
		this.roles = roles;
		this.avatar = avatar;
	}

	public String getRoleNames() {
		return roles.stream().map(Role::getName).collect(Collectors.joining(", "));
	}

	public Set<String> getRoleSet() {
		return roles.stream().map(Role::getId).collect(Collectors.toSet());
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
