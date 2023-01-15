package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.Comment;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AccessDocumentRepository accessDocumentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          AccessDocumentRepository accessDocumentRepository) {
        this.commentRepository = commentRepository;
        this.accessDocumentRepository = accessDocumentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null)
        {
            AccessDocument accessDocument = comment.getAccessDocument();
            accessDocument.getComments().remove(comment);
            commentRepository.deleteById(id);
            accessDocumentRepository.save(accessDocument);
        }
    }
}
