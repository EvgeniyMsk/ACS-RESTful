package ou.acs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Comment;
import ou.acs.entity.Person;
import ou.acs.requests.CommentRequest;
import ou.acs.service.AccessDocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/acessdocuments")
@PropertySource("classpath:/application.yaml")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccessDocumentController {
    private final AccessDocumentService accessDocumentService;

    @Autowired
    public AccessDocumentController(AccessDocumentService accessDocumentService) {
        this.accessDocumentService = accessDocumentService;
    }

    @GetMapping()
    public ResponseEntity<List<AccessDocument>> findAll() {
        return ResponseEntity.ok(accessDocumentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessDocument> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accessDocumentService.findById(id));
    }

    @GetMapping("/{id}/objects")
    public ResponseEntity<List<AccessObject>> findAccessObjectsByAccessDocumentId(@PathVariable Long id) {
        return ResponseEntity.ok(accessDocumentService.findAccessObjectsByAccessDocumentId(id));
    }

    @GetMapping("/{id}/people")
    public ResponseEntity<List<Person>> findPeopleByAccessDocumentId(@PathVariable Long id) {
        return ResponseEntity.ok(accessDocumentService.findPeopleByAccessDocumentId(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> findCommentsByAccessDocumentId(@PathVariable Long id) {
        return ResponseEntity.ok(accessDocumentService.findCommentsByAccessDocumentId(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> findCommentsByAccessDocumentId(@PathVariable Long id,
                                                                  @RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(accessDocumentService.addComment(id, commentRequest), HttpStatus.CREATED);
    }
}
