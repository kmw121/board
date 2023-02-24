package board.domain.DTO;

import board.domain.user.User;
import lombok.Data;

@Data
public class SignUpDto {
	private String username;
	private String password;
	private String name;
	
	public User toEntity() {
		User user = new User();
		user.setUsername(this.username);
		user.setPassword(password);
		user.setName(this.name);
		
		return user;
	}
}
