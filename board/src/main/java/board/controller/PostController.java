package board.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import board.config.auth.PrincipalDetails;
import board.domain.DTO.PostDto;
import board.domain.imageUrl.ImageUrl;
import board.domain.post.Post;
import board.service.ImageUrlService;
import board.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	private final PostService postService;
	private final ImageUrlService imageUrlService;
	

	@GetMapping("/post")
	public String post(Model model,@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestParam(required = false) String type) {
		
		if(principalDetails!=null) {
			model.addAttribute("manager",principalDetails.getUser());
		}
		if(type==null) {
			model.addAttribute("type"," ");
		}
		else {
			model.addAttribute("type",type);
		}
		
		return "postForm";
	}
	
	@PostMapping("/post")
	public String doPost(@RequestParam(required = false) String content,@ModelAttribute PostDto postDto,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails) {
		postDto.setContent(postDto.getContent().replaceAll("\r\n", "<br>"));
		Post post = postDto.toEntity();
		
		if(post.getType().equals("공지")&&(principalDetails==null || !principalDetails.getUser().getRole().equals("manager"))) {
			return "redirect:/";
		}
		
		String type = post.getType();
		Long count = postService.findCount(type);
		post.setTypeCount(count+1);
		postService.save(post);
		
		List<MultipartFile> files = postDto.getFiles();
		
        if(files!=null&&files.size()!=0&&files.get(0).getOriginalFilename().length()!=0){
        	
        	for(MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String imageFileName = uuid +"_"+file.getOriginalFilename();
            Path imageFilePath = Paths.get("C:\\Users\\qbic\\Desktop\\image\\"+imageFileName);
            try{
                Files.write(imageFilePath,file.getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl("/image/"+imageFileName);
            imageUrl.setPost(post);
            imageUrlService.save(imageUrl);
            }

        }
		
		return "redirect:/";
	}
	
	@GetMapping("/post/{postId}")
	public String postView(Model model,@PathVariable Long postId
							,@RequestParam(required = false) String deleteStatus
							,@RequestParam(required = false) String updateStatus
							,@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Post post = postService.find(postId);
		
		String deleteFlag = post.getDeleteFlag();
		
		
		if(deleteFlag !=null && deleteFlag.equals("checked") && (principalDetails==null || !principalDetails.getUser().getRole().equals("manager"))) {
			return "redirect:/";
		}
		
		String imgExist = "false";
		for(ImageUrl imageUrl : post.getImageUrls()) {
			if(imageUrl.getDeleteFlag()==null) {
				imgExist = "true";
				break;
			}
		}
		
		model.addAttribute("imgExist",imgExist);
		model.addAttribute("post",post);
		model.addAttribute("deleteStatus",deleteStatus);
		model.addAttribute("updateStatus",updateStatus);
		if(principalDetails!=null) {
			model.addAttribute("manager",principalDetails.getUser());
		}
		return "postView";
	}
	
	@GetMapping("/post/delete/{postId}")
	public String postDelete(Model model,@PathVariable Long postId,@RequestParam String password) {
		if(postService.passwordCheck(postId, password)) {
			postService.deletePost(postId,"password");
			return "redirect:/"+"?deleteStatus=true";
		}
		else {
		  return "redirect:/post/"+postId.toString()+"?deleteStatus=false";
		}
	}
	
	@GetMapping("/post/managerDelete/{postId}")
	public String postManagerDelete(Model model,@PathVariable Long postId,@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(principalDetails==null|| !principalDetails.getUser().getRole().equals("manager")) {
			return "redirect:/";
		}
		else {
			postService.deletePost(postId,"manager");
			return "redirect:/post/"+postId.toString();
		}
	}
	
	@GetMapping("/post/managerResotre/{postId}")
	public String postManagerRestore(Model model,@PathVariable Long postId,@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(principalDetails==null|| !principalDetails.getUser().getRole().equals("manager")) {
			return "redirect:/";
		}
		else {
			Post post = postService.find(postId);
			post.setDeleteDate(null);
			post.setDeleteFlag(null);
			postService.flush();
			return "redirect:/post/"+postId.toString();
		}
	}
	
	@GetMapping("/post/update/{postId}")
	public String postUpdate(Model model,@PathVariable Long postId,@ModelAttribute PostDto sendPostDto,@RequestParam(required = false) String updateStatus,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
			
			
			PostDto postDto = new PostDto();
			
			if(sendPostDto.getTitle()!=null||sendPostDto.getContent()!=null) {
				postDto = sendPostDto;
			}
			
			Post post = postService.find(postId);
			
			String deleteFlag = post.getDeleteFlag();
			
			if(deleteFlag !=null && deleteFlag.equals("checked") && (principalDetails==null || !principalDetails.getUser().getRole().equals("manager"))) {
				return "redirect:/";
			}
			
			else if(post.getType().equals("공지")&&(principalDetails==null || !principalDetails.getUser().getRole().equals("manager"))) {
				return "redirect:/";
			}
			
			String imgExist = "false";
			for(ImageUrl imageUrl : post.getImageUrls()) {
				if(imageUrl.getDeleteFlag()==null) {
					imgExist = "true";
					break;
				}
			}
			
			model.addAttribute("imgExist",imgExist);
			model.addAttribute("postDto",postDto);
			model.addAttribute("updateStatus",updateStatus);
		    model.addAttribute("post", post);
		    
			if(principalDetails!=null) {
				model.addAttribute("manager",principalDetails.getUser());
			}
			
			return "postUpdateForm";
	}
	
	@PostMapping("/post/update/{postId}")
	public String postUpdate(@ModelAttribute PostDto postDto,Model model,@PathVariable Long postId) {
			
			if(postService.passwordCheck(postId, postDto.getPassword())) {
				Post post = postService.find(postId);
				post.setTitle(postDto.getTitle());
				post.setContent(postDto.getContent().replaceAll("\r\n", "<br>"));
				post.setType(postDto.getType());
				if(post.getType().equals(postDto.getType())) post.setTypeCount(postService.findCount(postDto.getType())+1);
				if(postDto.getDeleteImage()!=null) {
				for(String imageId : postDto.getDeleteImage()) {
					imageUrlService.deleteByImageId(Long.parseLong(imageId),"password");
				};
				}
				
				List<MultipartFile> files = postDto.getFiles();
						
				if(files!=null&&files.size()!=0&&files.get(0).getOriginalFilename().length()!=0) {
					
					for(MultipartFile file : files) {
						UUID uuid = UUID.randomUUID();
						String imageFileName = uuid +"_"+file.getOriginalFilename();
						Path imageFilePath = Paths.get("C:\\Users\\qbic\\Desktop\\image\\"+imageFileName);
						try{
							Files.write(imageFilePath,file.getBytes());
						}catch (Exception e){
							e.printStackTrace();
						}
						ImageUrl imageUrl = new ImageUrl();
						imageUrl.setImageUrl("/image/"+imageFileName);
						imageUrl.setPost(post);
						imageUrlService.save(imageUrl);
					}

				}
				
				postService.flush();
				
			return "redirect:/post/"+postId.toString()+"?updateStatus=true";
			
	       }
			else {
				StringBuilder deleteImage = new StringBuilder();
				if(postDto.getDeleteImage()!=null) {
					deleteImage.append("&deleteImage=");
					for(String id : postDto.getDeleteImage()) {
					deleteImage.append(id+",");
				}
					deleteImage.deleteCharAt(deleteImage.length()-1);
				}

				
				String title="";
				String content="";
				
				try {
					title = URLEncoder.encode(postDto.getTitle(), "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					content = URLEncoder.encode(postDto.getContent(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				
				return "redirect:/post/update/"+postId.toString()+"?updateStatus=false"+"&title="+title+"&content="+content+"&id="+postId.toString()+deleteImage;
			}
	}

}
	
