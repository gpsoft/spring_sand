package jp.dip.gpsoft.springsand.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Id
	private String id;

	private String name;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	public Role() {
	}

	public Role(String id) {
		this.id = id;
		name = null;
	}

	public Role(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Set<String> getAllRoles() {
		return new HashSet<>(Arrays.asList(ROLE_USER, ROLE_ADMIN));
	}

	// ------------- generated getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
