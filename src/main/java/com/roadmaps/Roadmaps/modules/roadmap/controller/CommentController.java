package com.roadmaps.Roadmaps.modules.roadmap.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.CommentRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentReplyResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CommentResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.CommentMapper;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.UpvoteMapper;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import com.roadmaps.Roadmaps.modules.roadmap.service.UpvoteService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/roadmaps")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final UpvoteService upvoteService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UpvoteMapper upvoteMapper;

    @GetMapping("/{roadmapId}/comments")
    public ResponseEntity<ApiResponse<?>> getRootComments(@PathVariable String roadmapId, @PageableDefault(size = 1000) Pageable pageable){
        Page<Comment> comments = commentService.getRootCommentsByRoadmapId(roadmapId, pageable);
        ApiResponse<Page<CommentResponseDto>> apiResponse = ApiResponse.success(
                comments.map(commentMapper::toCommentResponseDto),
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<?>> getCommentReplies(@PathVariable String commentId, @PageableDefault(size = 1000) Pageable pageable){
        Page<Comment> comments = commentService.getRepliesByCommentId(commentId, pageable);
        ApiResponse<Page<CommentReplyResponseDto>> apiResponse = ApiResponse.success(
                comments.map(commentMapper::toCommentReplyResponseDto),
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/comments/{commentId}/upvotes")
    public ResponseEntity<ApiResponse<?>> upvoteToComment(Authentication authentication, @PathVariable String commentId) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        List<Upvote> upvotes = upvoteService.upvoteByUserEmailAndCommentId(user.getEmail(), commentId);

        ApiResponse<?> apiResponse = ApiResponse.success(
                upvoteMapper.toUpvoteResponseDtoList(upvotes),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{roadmapId}/comments")
    public ResponseEntity<ApiResponse<?>> commentToRoadmap(Authentication authentication, @PathVariable String roadmapId, @ModelAttribute CommentRequestDto commentDto) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        Comment comment = commentService.addComment(user.getEmail(), UUID.fromString(roadmapId), commentDto);

        ApiResponse<?> apiResponse = ApiResponse.success(
                commentMapper.toCommentResponseDto(comment),
                "New comment added."
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/{roadmapId}/comments/{commentId}/comments")
    public ResponseEntity<ApiResponse<?>> replyToRoadmapComment(Authentication authentication, @PathVariable String roadmapId, @PathVariable String commentId, @ModelAttribute CommentRequestDto commentDto) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        Comment comment = commentService.addCommentReply(user.getEmail(), UUID.fromString(roadmapId), UUID.fromString(commentId), commentDto);

        ApiResponse<?> apiResponse = ApiResponse.success(
                commentMapper.toCommentReplyResponseDto(comment),
                "New comment reply added."
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(Authentication authentication, @PathVariable String commentId) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        commentService.deleteUserComment(user.getEmail(), commentId);

        return ResponseEntity.noContent().build();
    }

}
