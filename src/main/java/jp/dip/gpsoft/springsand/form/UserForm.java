package jp.dip.gpsoft.springsand.form;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import jp.dip.gpsoft.springsand.Role;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.validation.OnInsert;
import jp.dip.gpsoft.springsand.validation.UniqueLoginId;

public class UserForm {

	private static final int AVATAR_MAX_WIDTH = 200;

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

	private MultipartFile avatar;
	private String avatarSrc;

	public UserForm() {
		id = null;
		loginId = null;
		password = null;
		roles = new HashSet<Role>();
		avatar = null;
		avatarSrc = "";
	}

	public UserForm(User user) {
		id = user.getId();
		loginId = user.getLoginId();
		password = null;
		roles = user.getRoleSet();
		avatar = null;
		avatarSrc = user.getAvatar();
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

	public String loadAvatarSrc() {
		if (avatar.isEmpty()) return "";
		try {
			// get the uploaded file
			BufferedImage img = ImageIO.read(avatar.getInputStream());
			// shrink it if it's too large
			if (img.getWidth() > AVATAR_MAX_WIDTH) {
				img = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
						AVATAR_MAX_WIDTH,
						Scalr.OP_ANTIALIAS);
			}
			// make it byte[]
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(img, avatar.getContentType().split("/")[1], out);
			out.flush();
			byte[] raw = out.toByteArray();
			// convert for src attribute
			String src = "data:image/png;base64," + Base64.getEncoder().encodeToString(raw);
			return src;
		} catch (IOException e) {
			return "";
		}
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

	public MultipartFile getAvatar() {
		return avatar;
	}

	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	}

	public String getAvatarSrc() {
		return avatarSrc;
	}
}
