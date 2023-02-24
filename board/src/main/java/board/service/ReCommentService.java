package board.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import board.domain.ReComment.ReComment;
import board.domain.ReComment.ReCommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReCommentService {
		private final ReCommentRepository reCommentRepository;
		private final BCryptPasswordEncoder bCryptPasswordEncoder;
		
		public ReComment find(Long reCommentId) {
			return reCommentRepository.findById(reCommentId).get();
		}
		
		public void save(ReComment recomment) {
			reCommentRepository.save(recomment);
		}
		
		public boolean passwordCheck(Long reCommentId,String password) {
			ReComment recomment = find(reCommentId);
			return bCryptPasswordEncoder.matches(password,recomment.getPassword());
		}
		public void deleteReComment(Long reCommentId,String type) {
			ReComment recomment = find(reCommentId);
			recomment.setDeleteFlag("checked");
			recomment.setDeleteDate();
			recomment.setDeleteType(type);
			reCommentRepository.flush();
		}
}
