package jp.dip.gpsoft.springsand.form;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import jp.dip.gpsoft.springsand.Role;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.validation.OnInsert;
import jp.dip.gpsoft.springsand.validation.UniqueLoginId;

public class UserForm {

	private Integer id;

	@NotBlank(message = "ログインIDを入力してください。", groups = { OnInsert.class })
	@Length(min = 4,
			max = 10,
			message = "{min}文字以上、{max}文字以内で入力してください。",
			groups = { OnInsert.class })
	@UniqueLoginId(message = "既に使われています。別のログインIDにしてください。", groups = { OnInsert.class })
	private String loginId;

	@NotBlank(message = "パスワードを入力してください。", groups = { OnInsert.class })
	private String password;

	@Size(min = 1, message = "1つ以上のロールをチェックしてください。")
	private Set<Role> roles;

	public UserForm() {
		id = null;
		loginId = null;
		password = null;
		roles = new HashSet<Role>();
	}

	public UserForm(User user) {
		id = user.getId();
		loginId = user.getLoginId();
		password = null;
		roles = user.getRoleSet();
	}

	@Override
	public String toString() {
		return "UserForm id=" + id + ", loginId=" + loginId + ", roles=" + User.roles2RoleStrs(
				roles) + "]";
	}

	public boolean isNew() {
		return id == null;
	}

	public String[] allRoles() {
		return Arrays.stream(Role.values()).map(Role::name).toArray(String[]::new);
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
