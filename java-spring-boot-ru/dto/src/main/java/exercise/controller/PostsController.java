package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;

import exercise.model.Post;
import exercise.model.Comment;
import exercise.repository.PostRepository;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping(path = "/posts")
public class PostsController {
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping(path = "")
  public List<PostDTO> getPosts() {
    List<Post> allPosts = postRepository.findAll();

    return allPosts.stream().map((post) -> {
      var postComments = commentRepository.findByPostId(post.getId());
      return getPostDTO(post, postComments);
    }).toList();
  }

  @GetMapping(path = "/{id}")
  public PostDTO getPost(@PathVariable long id) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isEmpty()) {
      throw new ResourceNotFoundException("Post with id " + id + " not found");
    }

    Post post = maybePost.get();
    List<Comment> postComments = commentRepository.findByPostId(post.getId());

    return getPostDTO(post, postComments);
  }

  private PostDTO getPostDTO(Post post, List<Comment> comments) {
    var commentDTOs = comments.stream().map(this::getCommentDTO).toList();
    var postDTO = new PostDTO();

    postDTO.setId(post.getId());
    postDTO.setBody(post.getBody());
    postDTO.setTitle(post.getTitle());
    postDTO.setComments(commentDTOs);

    return postDTO;
  }

  private CommentDTO getCommentDTO(Comment comment) {
    var commentDTO = new CommentDTO();
    commentDTO.setId(comment.getId());
    commentDTO.setBody(comment.getBody());
    return commentDTO;
  }
}
// END
