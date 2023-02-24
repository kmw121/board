package board.domain.DTO;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import board.domain.comment.Comment;
import lombok.Data;

@Data
public class CommentDto {
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private String content;
	private String password;
	
	public Comment toEntity() {
		Comment comment = new Comment();
		comment.setContent(content);
		String encPassword = bCryptPasswordEncoder.encode(this.password);
        comment.setPassword(encPassword);
        return comment;
	}
}
