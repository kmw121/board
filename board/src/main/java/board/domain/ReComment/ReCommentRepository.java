package board.domain.ReComment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ReCommentRepository extends JpaRepository<ReComment,Long> {
	@Transactional
    void deleteByCommentId(Long commentId);
	
	List<ReComment> findByCommentId(Long commentId);
}
