package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Comment;
import ou.acs.entity.Person;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.repository.CommentRepository;
import ou.acs.requests.CommentRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AccessDocumentService {
    private final AccessDocumentRepository accessDocumentRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public AccessDocumentService(AccessDocumentRepository accessDocumentRepository,
                                 CommentRepository commentRepository) {
        this.accessDocumentRepository = accessDocumentRepository;
        this.commentRepository = commentRepository;
    }

    public List<AccessDocument> findAll() {
        return accessDocumentRepository.findAll();
    }

    public AccessDocument findById(Long id) {
        return accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<AccessObject> findAccessObjectsByAccessDocumentId(Long id) {
        return accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new)
                .getAccessObjects()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Person> findPeopleByAccessDocumentId(Long id) {
        return new ArrayList<>(accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new)
                .getPeoples());
    }

    public Comment addComment(Long accessDocumentId, CommentRequest commentRequest) {
        AccessDocument accessDocument = accessDocumentRepository.findById(accessDocumentId).orElseThrow(NoSuchElementException::new);
        Comment comment = new Comment(commentRequest.getComment(), accessDocument);
        return commentRepository.save(comment);
    }

    public void deleteComment(AccessDocument accessDocument, Comment comment) {
        commentRepository.deleteById(comment.getId());
        accessDocument.getComments().remove(comment);
        accessDocumentRepository.save(accessDocument);
    }

    public List<Comment> findCommentsByAccessDocumentId(Long id) {
        return accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new)
                .getComments()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
