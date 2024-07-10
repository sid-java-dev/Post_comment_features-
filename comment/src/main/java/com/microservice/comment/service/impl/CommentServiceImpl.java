package com.microservice.comment.service.impl;

import com.microservice.comment.entity.Comment;
import com.microservice.comment.payload.CommentDto;
import com.microservice.comment.payload.Post;
import com.microservice.comment.repository.CommentRepository;
import com.microservice.comment.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Post post = restTemplate.getForObject("http://POST-SERVICE/api/posts/" + commentDto.getPostId(), Post.class);
       if(post!=null){
           String commentId = UUID.randomUUID().toString();
           Comment comment = mapToEntity(commentDto);
           comment.setCommentId(commentId);
           Comment savedComment = commentRepository.save(comment);
           CommentDto dto = mapToDto(savedComment);
           return dto;
       }
        return null;
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }

    public Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }
    public CommentDto mapToDto(Comment comment){
        CommentDto dto= modelMapper.map(comment, CommentDto.class);
        return dto;
    }
}
