package board.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import board.domain.comment.Comment;
import board.domain.imageUrl.ImageUrl;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    private String content;
    
    private String password;

    private String type;
        
    private Long typeCount;

    private String deleteFlag;
    
    private String deleteType;
    
    private String createDateKr;
    
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;
    
    private LocalDateTime deleteDate;
    
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<ImageUrl> imageUrls;
    
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    


    @PrePersist
    public void setCreateDate() {
        if(this.createDate == null) {
        	this.createDate = LocalDateTime.now();
            this.createDateKr = this.createDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시mm분"));
        };
        this.updateDate = LocalDateTime.now();
    }
    
    public void setDeleteDate() {
    	this.deleteDate = LocalDateTime.now();
    }
    
}
