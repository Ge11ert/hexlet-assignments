package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping(path = "")
  public List<Post> showAll() {
    return postRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  public Post show(@PathVariable long id) {
    return postRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
  }

  @PostMapping(path = "")
  @ResponseStatus(HttpStatus.CREATED)
  public Post create(@RequestBody Post post) {
    return postRepository.save(post);
  }

  @PutMapping(path = "/{id}")
  public Post update(@PathVariable long id, @RequestBody Post post) {
    var maybePost = postRepository.findById(id);

    if (maybePost.isEmpty()) {
      throw new ResourceNotFoundException("Post " + id + " not found");
    }

    Post postToUpdate = maybePost.get();
    postToUpdate.setTitle(post.getTitle());
    postToUpdate.setBody(post.getBody());

    postRepository.save(postToUpdate);

    return postToUpdate;
  }

  @DeleteMapping(path = "/{id}")
  public void delete(@PathVariable long id) {
    var maybePost = postRepository.findById(id);

    if (maybePost.isEmpty()) {
      throw new ResourceNotFoundException("Post " + id + " not found");
    }

    postRepository.deleteById(id);
    commentRepository.deleteByPostId(id);
  }
}
// END
