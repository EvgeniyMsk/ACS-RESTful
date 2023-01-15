package ou.acs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.acs.entity.Comment;
import ou.acs.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
@PropertySource("classpath:/application.yaml")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> findAll() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Comment> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
