package board.domain.imageUrl;

import java.time.LocalDateTime;


import board.domain.post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class ImageUrl {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    
    private String deleteFlag;
    
    private String deleteType;
    
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;
    
    private LocalDateTime deleteDate;
    
    @JoinColumn(name = "postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
    @PrePersist
    public void setCreateAndUpdateDate() {
        if(this.createDate == null) this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }
    
    public void setDeleteDate() {
    	this.deleteDate = LocalDateTime.now();
    }
    
    

}
