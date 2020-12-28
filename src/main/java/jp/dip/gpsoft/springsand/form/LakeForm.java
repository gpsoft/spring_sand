package jp.dip.gpsoft.springsand.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import jp.dip.gpsoft.springsand.model.Lake;
import jp.dip.gpsoft.springsand.service.LakeService;
import jp.dip.gpsoft.springsand.validation.NotBlankUnlessPref;
import jp.dip.gpsoft.springsand.validation.NumberString;

@NotBlankUnlessPref(message = "場所を入力してください。")
public class LakeForm {
	private Integer id;

	@NotBlank(message = "湖の名前を入力してください。")
	@Length(max = 50, message = "{max}文字以内で入力してください。")
	private String name;

	private String bySelect;
	private String prefecture;

	@Length(max = 20, message = "{max}文字以内で入力してください。")
	private String location;

	@NotBlank(message = "面積を入力してください。")
	@NumberString(message = "数値を入力してください。")
	private String area;

	public LakeForm() {
		id = null;
		name = "";
		bySelect = "0";
		prefecture = "";
		location = "";
		area = "";
	}

	public LakeForm(Lake lake) {
		id = lake.getId();
		name = lake.getName();
		location = lake.getLocation();
		bySelect = LakeService.isPref(location) ? "1" : "0";
		area = lake.getArea().toString();
	}

	public boolean isNew() {
		return id == null;
	}

	public boolean isPref() {
		return bySelect.equals("1");
	}

	public String[] allPrefs() {
		return LakeService.allPrefs();
	}

	@Override
	public String toString() {
		return "LakeForm id=" + id + ", name=" + name + ", location=" + location
				+ ", bySelect=" + bySelect + ", pref=" + prefecture
				+ ", " + area + "km2]";
	}

	public Lake toLake() {
		Lake lake = new Lake(name, isPref() ? prefecture : location,
				Integer.valueOf(area));
		lake.setId(id);
		return lake;
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

	public String getBySelect() {
		return bySelect;
	}

	public void setBySelect(String bySelect) {
		this.bySelect = bySelect;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String prefecture) {
		this.prefecture = prefecture;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
