package board.domain.comment;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import board.domain.ReComment.ReComment;
import board.domain.post.Post;

@Data
@Entity
public class Comment {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private String content;
	    
	    private String password;
	    
	    private String deleteFlag;
	    
	    private String deleteType;

	    private LocalDateTime createDate;
	    
	    private LocalDateTime updateDate;
	    
	    private LocalDateTime deleteDate;

	    @JoinColumn(name = "postId")
	    @ManyToOne(fetch = FetchType.LAZY)
	    private Post post;

	    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
	    private List<ReComment> reComments = new ArrayList<>();
	    

	    @PrePersist
	    public void setCreateAndUpdateDate() {
	        if(this.createDate == null) this.createDate = LocalDateTime.now();
	        this.updateDate = LocalDateTime.now();
	    }
	    
	    public void setDeleteDate() {
	    	this.deleteDate = LocalDateTime.now();
	    }
	    

}
