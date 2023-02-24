package board.domain.imageUrl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ImageUrlRepository extends JpaRepository<ImageUrl,Long> {
	@Transactional
    void deleteByPostId(Long postId);
	
}
