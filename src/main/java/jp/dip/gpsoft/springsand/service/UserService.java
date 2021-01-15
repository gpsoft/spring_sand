package jp.dip.gpsoft.springsand.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.exception.NotFoundStatusException;
import jp.dip.gpsoft.springsand.form.UserForm;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.repository.UserRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder pwEncoder;

	public User lookupUser(Integer id) {
		Optional<User> maybeUser = userRepository.findById(id);
		return maybeUser.orElseThrow(NotFoundStatusException::new);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public boolean isAvailableLoginId(String loginId) {
		return userRepository.countByLoginId(loginId) <= 0;
		// ↑INSERT時専用なので、これでいい。
		// UPDATE時にログインIDの変更を許すなら「自分以外」を条件に加える必要あり。
	}

	@Transactional(readOnly = false)
	public void saveUser(UserForm form) {
		User user;
		if (form.isNew()) {
			user = new User();
			user.setLoginId(form.getLoginId());
		} else {
			// 既存ユーザの編集時、パスワード欄が未入力なら、
			// パスワードを変更したくない。
			// また、ログインIDは編集不可とする。
			// そのため、一旦、現在値をDBから読んでおく。
			user = lookupUser(form.getId());
		}
		if (!form.getPassword().isEmpty()) {
			user.setPassword(pwEncoder.encode(form.getPassword()));
		}
		user.setRoles(User.roles2RoleStrs(form.getRoles()));
		form.loadAvatarSrc().ifPresent(user::setAvatar);
		userRepository.save(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}
}
