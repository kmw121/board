package board.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import board.config.auth.PrincipalDetails;
import board.domain.post.Post;
import board.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final PostService postService;
	@GetMapping("/")
	public String index(Model model,@PageableDefault(size=10, page=0, sort="id",direction=Sort.Direction.DESC)Pageable pageable,@RequestParam(required=false) String type,
			 @AuthenticationPrincipal PrincipalDetails principalDetails,@RequestParam(required = false) String deleteStatus) {
		Pageable paging;
		
		if(type==null||type=="") {
		paging = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("id").descending());
		}
		else {
		paging = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("typeCount").descending());	
		}
		
		
		if(principalDetails!=null) {
			model.addAttribute("manager",principalDetails.getUser());
		}
		
		if(deleteStatus!=null) {
			model.addAttribute("deleteStatus",deleteStatus);
		}
		
		Page<Post> posts;

		if(principalDetails !=null && principalDetails.getUser().getRole().equals("manager") && type!=null) {
			posts = postService.findTypeManager(type,paging);
		}
		
		else if(principalDetails != null && principalDetails.getUser().getRole().equals("manager") && type == null) {
			posts = postService.findAllPageManager(paging);
		}
		
		else {
			if(type!=null) {
			posts = postService.findType(type,paging);
			}
			
			else {
				posts = postService.findAllPage(paging);
			}
		}
		
		model.addAttribute("postList", posts);
		model.addAttribute("type",type);
		return "index";
	}
	@GetMapping("/search")
	public String search(Model model,@PageableDefault(size=10, page=0, sort="id",direction=Sort.Direction.DESC)Pageable pageable,
						@RequestParam(required=false) String type,@RequestParam String content,@RequestParam String condition, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Page<Post> posts;
		Pageable paging;
		
		if(type==null||type=="") {
		paging = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("id").descending());
		}
		else {
		paging = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("typeCount").descending());	
		}
		
		if(principalDetails !=null && principalDetails.getUser().getRole().equals("manager") && type!="") {
			posts = postService.findSearchTypeManager(type,condition,content,paging);
		}
		
		else if(principalDetails != null && principalDetails.getUser().getRole().equals("manager") && type == "") {
			posts = postService.findSearchManager(condition, content, paging);
		}
		
		else {	
			
			if(type!="") {
			posts = postService.findSearchType(type,condition,content,paging);
			}
			
			else { 
			posts = postService.findSearch(condition, content, paging);
			}
			
		}
		model.addAttribute("condition",condition);
		model.addAttribute("postList", posts);
		model.addAttribute("type",type);
		
		if(principalDetails!=null) {
			model.addAttribute("manager",principalDetails.getUser());
		}
		
		return "index";
	}

	
}
