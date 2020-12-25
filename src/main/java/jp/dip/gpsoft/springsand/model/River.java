package jp.dip.gpsoft.springsand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rivers")
public class River {
	@Id
	@GeneratedValue
	private Integer id;

	private String name;
	private String source;
	private String mouse;

	public River() {
	}

	public River(String name, String source, String mouse) {
		this.name = name;
		this.source = source;
		this.mouse = mouse;
	}

	public boolean isNew() {
		return id == null;
	}

	@Override
	public String toString() {
		return "River#" + id + "[name=" + name + ", from " + source + " to " + mouse
				+ "]";
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMouse() {
		return mouse;
	}

	public void setMouse(String mouse) {
		this.mouse = mouse;
	}
}
