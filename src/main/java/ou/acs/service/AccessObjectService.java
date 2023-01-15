package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.User;
import ou.acs.exception.NotFoundException;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.repository.AccessObjectRepository;

import java.util.List;

@Service
public class AccessObjectService {
    private final AccessObjectRepository accessObjectRepository;

    private final AccessDocumentRepository accessDocumentRepository;

    @Autowired
    public AccessObjectService(AccessObjectRepository accessObjectRepository,
                               AccessDocumentRepository accessDocumentRepository) {
        this.accessObjectRepository = accessObjectRepository;
        this.accessDocumentRepository = accessDocumentRepository;
    }

    public List<AccessObject> findAll() {
        return accessObjectRepository.findAll();
    }

    public AccessObject findById(Long id) {
        return accessObjectRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<User> findAllUsersByAccessObjectId(Long id) {
        return accessObjectRepository.findById(id).orElseThrow(NotFoundException::new).getUsers();
    }

    public List<AccessDocument> findAllAccessDocumentsByAccessObjectId(Long id) {
        return accessObjectRepository.findById(id).orElseThrow(NotFoundException::new).getAccessDocuments();
    }

    @Transactional
    public void addAccessDocument(String title, AccessDocument accessDocument) {
        AccessObject accessObject = findByTitle(title);
        AccessDocument tempAccessDocument = accessDocumentRepository.findByDocumentId(accessDocument.getDocumentId());
        if (tempAccessDocument != null)
            accessDocument = tempAccessDocument;
        AccessDocument finalAccessDocument = accessDocument;

        if (accessObject.getAccessDocuments()
                .stream()
                .noneMatch(accessDocument1 -> accessDocument1.equals(finalAccessDocument)))
        {
            accessObject.getAccessDocuments().add(finalAccessDocument);
            finalAccessDocument.getAccessObjects().add(accessObject);
            accessDocumentRepository.save(finalAccessDocument);
            accessObjectRepository.save(accessObject);
        }
    }

    public AccessObject findByTitle(String title) {
        return accessObjectRepository.findByTitle(title);
    }
}
