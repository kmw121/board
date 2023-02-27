package board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import board.domain.post.Post;
import board.domain.post.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final EntityManager em;
	
	  public static boolean isStringDouble(String s) {
		    try {
		        Double.parseDouble(s);
		        return true;
		    } catch (NumberFormatException e) {
		        return false;
		    }
		  }
	
	public Post find(Long id) {
		Post post = postRepository.findById(id).get();
		return post;
	}
	
	public Page<Post> findAllPageManager(Pageable pageable){
		return postRepository.findAll(pageable);
	}
	
	public Page<Post> findTypeManager(String type,Pageable pageable){
		return postRepository.findByType(type,pageable);
	}
	
	public Page<Post> findAllPage(Pageable pageable){
		return postRepository.findByDeleteFlag(null,pageable);
	}
	
	public Page<Post> findType(String type,Pageable pageable){
		return postRepository.findByTypeAndDeleteFlag(type,null,pageable);
	}
	
	
	public void save(Post post) {
		postRepository.save(post);
	}
	
	public Long findCount(String type) {
	    TypedQuery<Post> posts = em.createQuery("SELECT p FROM Post p  where type = '"+ type +"' ORDER BY typeCount DESC LIMIT 1", Post.class);
        return posts.getResultList().size()!=0 ? posts.getResultList().get(0).getTypeCount() : 0L ;
	}
	
	public Page<Post> findSearchTypeManager(String type,String condition,String content,Pageable pageable){
		switch(condition) {
		case "번호" : if(!isStringDouble(content)) {
						return postRepository.findByTypeAndDeleteFlag("데이터존재안함",null,pageable);
		             }
					return postRepository.findByTypeAndTypeCount(type, content, pageable);
		case "제목" : return postRepository.findByTypeAndTitleContains(type, content, pageable);
		case "작성시간" : return postRepository.findByTypeAndCreateDateKrContains(type, content, pageable);
		}
		return null;
	}
	
	public Page<Post> findSearchManager(String condition,String content,Pageable pageable){
		switch(condition) {
		case "번호" : if(!isStringDouble(content)) {
						return postRepository.findByTypeAndDeleteFlag("데이터존재안함",null,pageable);
		             }
					return postRepository.findById(content, pageable);
		case "제목" : return postRepository.findByTitleContains(content, pageable);
		case "작성시간" : return postRepository.findByCreateDateKrContains(content, pageable);
		}
		return null;
	}
	

	
	public Page<Post> findSearchType(String type,String condition,String content,Pageable pageable){
		switch(condition) {
		case "번호" : if(!isStringDouble(content)) {
						return postRepository.findByTypeAndDeleteFlag("데이터존재안함",null,pageable);
		             }
					return postRepository.findByTypeAndDeleteFlagAndTypeCount(type, null, content, pageable);
		case "제목" : return postRepository.findByTypeAndDeleteFlagAndTitleContains(type, null, content, pageable);
		case "작성시간" : return postRepository.findByTypeAndDeleteFlagAndCreateDateKrContains(type, null, content, pageable);
		}
		return null;
	}
	
	public Page<Post> findSearch(String condition,String content,Pageable pageable){
		switch(condition) {
		case "번호" : if(!isStringDouble(content)) {
						return postRepository.findByTypeAndDeleteFlag("데이터존재안함",null,pageable);
		             }
					return postRepository.findByDeleteFlagAndId(null,content, pageable);
		case "제목" : return postRepository.findByDeleteFlagAndTitleContains(null, content, pageable);
		case "작성시간" : return postRepository.findByDeleteFlagAndCreateDateKrContains(null, content, pageable);
		}
		return null;
	}
	

	
	public boolean passwordCheck(Long postId,String password) {
		Post post = find(postId);
		return bCryptPasswordEncoder.matches(password,post.getPassword());	
	}

	public void deletePost(Long postId,String type) {
		Post post = find(postId);
		post.setDeleteFlag("checked");
		post.setDeleteDate();
		post.setDeleteType(type);
		postRepository.flush();
	}
	
	public void flush() {
		postRepository.flush();
	}
	
}
