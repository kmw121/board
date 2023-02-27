package board.service;


import org.springframework.stereotype.Service;

import board.domain.imageUrl.ImageUrl;
import board.domain.imageUrl.ImageUrlRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageUrlService {
	private final ImageUrlRepository imageUrlRepository;
	
	public void save(ImageUrl imageUrl) {
		imageUrlRepository.save(imageUrl);
	}
	
	public void deleteByPostId(Long postId) {
		imageUrlRepository.deleteByPostId(postId);
	}
	
	public void deleteByImageId(Long imageId,String type) {
		ImageUrl imageUrl = imageUrlRepository.findById(imageId).get();
		imageUrl.setDeleteFlag("checked");
		imageUrl.setDeleteDate();
		imageUrl.setDeleteType(type);
		imageUrlRepository.flush();
	}
	

}
