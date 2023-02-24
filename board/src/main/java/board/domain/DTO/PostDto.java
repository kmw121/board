package board.domain.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import board.domain.post.Post;

import lombok.Data;


@Data
public class PostDto {
	

	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private String title;
	private String content;
	private String type;
	private String password;
	private String id;
	private List<String> deleteImage;
	private List<MultipartFile> files = new ArrayList<>();
	
	
	 public Post toEntity(){
	        Post post = new Post();
	        post.setContent(this.content);
	        post.setTitle(this.title);
	        post.setType(this.type);
	        String encPassword = bCryptPasswordEncoder.encode(this.password);
	        post.setPassword(encPassword);
	        

	        return post;
	    }

}
