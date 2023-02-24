package board.domain.post;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
	Page<Post> findAll(Pageable pageable);
	Page<Post> findByType(String type,Pageable pageable);
	
	Page<Post> findByDeleteFlag(String deleteFlag,Pageable pageable);
	Page<Post> findByTypeAndDeleteFlag(String type,String deleteFlag,Pageable pageable);
	
	Page<Post> findByTypeAndTitleContains(String type,String title,Pageable pageable);
	Page<Post> findByTypeAndCreateDateKrContains(String type,String createDateKr,Pageable pageable);
	Page<Post> findByTypeAndTypeCount(String type,String typeCount,Pageable pageable);
	
	Page<Post> findByTitleContains(String title,Pageable pageable);
	Page<Post> findByCreateDateKrContains(String createDateKr,Pageable pageable);
	Page<Post> findById(String Id,Pageable pageable);
	
	
	Page<Post> findByTypeAndDeleteFlagAndTitleContains(String type,String deleteFlag,String title,Pageable pageable);
	Page<Post> findByTypeAndDeleteFlagAndCreateDateKrContains(String type,String deleteFlag,String createDateKr,Pageable pageable);
	Page<Post> findByTypeAndDeleteFlagAndTypeCount(String type,String deleteFlag,String typeCount,Pageable pageable);
	
	Page<Post> findByDeleteFlagAndTitleContains(String deleteFlag,String title,Pageable pageable);
	Page<Post> findByDeleteFlagAndCreateDateKrContains(String deleteFlag,String createDateKr,Pageable pageable);
	Page<Post> findByDeleteFlagAndId(String deleteFlag,String Id,Pageable pageable);
}
