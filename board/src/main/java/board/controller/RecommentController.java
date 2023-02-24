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
import board.domain.DTO.RecommentDto;
import board.domain.ReComment.ReComment;
import board.domain.comment.Comment;
import board.service.CommentService;
import board.service.ReCommentService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecommentController {
	private final CommentService commentService;
	private final ReCommentService reCommentService;
	
	@PostMapping("/reComment/{commentId}")
	public String saveRecomment(@ModelAttribute RecommentDto reCommentDto,Model model,@PathVariable Long commentId) {
		ReComment reCommentEntity = reCommentDto.toEntity();
		Comment comment = commentService.find(commentId);
		reCommentEntity.setComment(comment);
		reCommentService.save(reCommentEntity);
		return "redirect:/post/"+reCommentDto.getPostId();
	}
	
	@GetMapping("/reComment/delete/{reCommentId}")
	public String deleteReCommnet(@RequestParam String postId, @RequestParam String password, @PathVariable Long reCommentId) {
		if(reCommentService.passwordCheck(reCommentId, password)) {
			reCommentService.deleteReComment(reCommentId,"password");
			return "redirect:/post/"+postId.toString();
		}
		else {
			return "redirect:/post/"+postId.toString()+"?deleteStatus=false";
		}
	}
	
	@GetMapping("/reComment/managerDelete/{reCommentId}")
	public String reCommnetManagerDelete(@RequestParam String postId,@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long reCommentId) {
		
		if(principalDetails==null|| !principalDetails.getUser().getRole().equals("manager")) {
			return "redirect:/post/"+postId.toString();
		}
		else {
			reCommentService.deleteReComment(reCommentId,"manager");
			return "redirect:/post/"+postId.toString();
		}

	}
}
