package jp.dip.gpsoft.springsand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lakes")
public class Lake {
	@Id
	@GeneratedValue
	private Integer id;

	private String name;
	private String location;
	private Integer area;

	public Lake() {
	}

	public Lake(String name, String location, Integer area) {
		this.name = name;
		this.location = location;
		this.area = area;
	}

	public boolean isNew() {
		return id == null;
	}

	@Override
	public String toString() {
		return "Lake#" + id + "[name=" + name + ", in " + location + ", " + area + "km2]";
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}
}
