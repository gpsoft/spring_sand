package jp.dip.gpsoft.springsand.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import jp.dip.gpsoft.springsand.model.User;

public class UserForm {

	private Integer id;

	@NotBlank(message = "ログインIDを入力してください。")
	@Length(min = 4, max = 10, message = "{min}文字以上、{max}文字以内で入力してください。")
	private String loginId;

//	@Length(min = 4, message = "{min}文字以上で入力してください。")
//	private String pwPlain;

	public UserForm() {
		id = null;
		loginId = "";
//		pwPlain = "";
	}

	public UserForm(User user) {
		id = user.getId();
		loginId = user.getLoginId();
//		pwPlain = "";
	}

	@Override
	public String toString() {
		return "UserForm id=" + id + ", loginId=" + loginId + "]";
	}

	public boolean isNew() {
		return id == null;
	}

	public User toUser() {
		User user = new User(loginId, "", "", "");
		user.setId(id);
		return user;
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
}
