package com.microservice.comment.service;

import com.microservice.comment.payload.CommentDto;

import java.util.List;

public interface CommentService {
   CommentDto createComment(CommentDto commentDto);

   List<CommentDto> getAllCommentsByPostId(String postId);
}
