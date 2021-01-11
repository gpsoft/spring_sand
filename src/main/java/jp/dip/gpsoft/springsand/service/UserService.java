package jp.dip.gpsoft.springsand.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.dip.gpsoft.springsand.exception.NotFoundStatusException;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.repository.UserRepository;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User lookupUser(Integer id) {
		Optional<User> maybeUser = userRepository.findById(id);
		return maybeUser.orElseThrow(NotFoundStatusException::new);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}
}
