package board.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import board.domain.comment.Comment;
import board.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ReCommentService reCommentService;
	
	public void save(Comment comment) {
		commentRepository.save(comment);
	}
	public Comment find(Long id) {
		return commentRepository.findById(id).get();
	}
	public boolean passwordCheck(Long commentId,String password) {
		Comment comment = find(commentId);
		return bCryptPasswordEncoder.matches(password,comment.getPassword());
	}
	
	public void deleteComment(Long commentId,String type) {
		Comment comment = commentRepository.findById(commentId).get();
		comment.setDeleteFlag("checked");
		comment.setDeleteDate();
		comment.setDeleteType(type);
		commentRepository.flush();
	}
	

}
