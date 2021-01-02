package jp.dip.gpsoft.springsand.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.dip.gpsoft.springsand.model.CustomUserDetails;
import jp.dip.gpsoft.springsand.model.User;
import jp.dip.gpsoft.springsand.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(username);
		if (user == null) {
			throw new UsernameNotFoundException("Bad username");
		}
		return new CustomUserDetails(user);
	}
}