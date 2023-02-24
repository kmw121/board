package board.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import board.config.auth.PrincipalDetails;
import board.domain.DTO.CommentDto;
import board.domain.comment.Comment;
import board.domain.post.Post;
import board.service.CommentService;
import board.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {
	
	private final PostService postService;
	private final CommentService commentService;
	
	@PostMapping("/comment/{postId}")
	public String saveComment(@ModelAttribute CommentDto commentDto,Model model,@PathVariable Long postId) {
		Comment commentEntity = commentDto.toEntity();
		Post post = postService.find(postId);
		commentEntity.setPost(post);
		commentService.save(commentEntity);
		
		return "redirect:/post/"+postId.toString();
	}
	@GetMapping("/comment/delete/{commentId}")
	public String deleteComment(@RequestParam String postId, @RequestParam String password, @PathVariable Long commentId) {
		
		if(commentService.passwordCheck(commentId, password)) {
			commentService.deleteComment(commentId,"password");
			return "redirect:/post/"+postId.toString();
		}
		else {
			return "redirect:/post/"+postId.toString()+"?deleteStatus=false";
		}
	}
	
	@GetMapping("/comment/managerDelete/{commentId}")
	public String reCommnetManagerDelete(@RequestParam String postId,@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long commentId) {
		
		if(principalDetails==null|| !principalDetails.getUser().getRole().equals("manager")) {
			return "redirect:/post/"+postId.toString();
		}
		else {
			commentService.deleteComment(commentId,"manager");
			return "redirect:/post/"+postId.toString();
		}

	}
}
