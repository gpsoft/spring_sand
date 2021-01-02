package jp.dip.gpsoft.springsand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "valleys")
public class Valley {
	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	public Valley() {
	}

	public Valley(String name) {
		this.name = name;
	}

	public boolean isNew() {
		return id == null;
	}

	@Override
	public String toString() {
		return "Valley#" + id + "[name=" + name + "]";
	}

	// ------------- generated getters and setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
