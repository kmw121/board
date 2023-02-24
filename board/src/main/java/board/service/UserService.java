package board.service;

import org.springframework.stereotype.Service;

import board.domain.user.User;
import board.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	public User find(Long id) {
		User user = userRepository.findById(id).get();
		return user;
	}

}
