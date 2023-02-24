package board.domain.DTO;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import board.domain.ReComment.ReComment;
import lombok.Data;

@Data
public class RecommentDto {
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private String content;
	private String password;
	private String postId;
	
	public ReComment toEntity() {
		ReComment reComment = new ReComment();
		reComment.setContent(content);
		String encPassword = bCryptPasswordEncoder.encode(this.password);
		reComment.setPassword(encPassword);
        return reComment;
	}
}
