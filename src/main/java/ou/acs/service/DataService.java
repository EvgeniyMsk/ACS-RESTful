package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ou.acs.constants.AccessConstants;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Person;
import ou.acs.entity.User;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.repository.AccessObjectRepository;
import ou.acs.repository.CommentRepository;
import ou.acs.repository.UserRepository;
import ou.acs.requests.ObjectNameRequest;
import ou.acs.requests.SearchPersonRequest;
import ou.acs.responses.ObjectNameResponse;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SessionScope
public class DataService {
    private final UserRepository userRepository;
    private final AccessObjectRepository accessObjectRepository;
    private final AccessDocumentRepository accessDocumentRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public DataService(UserRepository userRepository,
                       AccessObjectRepository accessObjectRepository,
                       AccessDocumentRepository accessDocumentRepository,
                       CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.accessObjectRepository = accessObjectRepository;
        this.accessDocumentRepository = accessDocumentRepository;
        this.commentRepository = commentRepository;
    }

    public User getProfile(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    @Cacheable(value = "profile",key = "{ #principal.name}")
    public List<AccessObject> getAccessObjects(Principal principal) {
        return getProfile(principal).getObjects()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Cacheable(value = "profile",key = "{ #principal.name, #id}")
    public List<AccessDocument> getAccessDocuments(Principal principal) {
        List<AccessObject> accessObjects = getAccessObjects(principal);
        List<AccessDocument> accessDocuments = new ArrayList<>();
        for (AccessObject accessObject : accessObjects)
            accessDocuments.addAll(accessObject.getAccessDocuments());
        return accessDocuments.stream().sorted().collect(Collectors.toList());
    }


    public AccessObject getAccessObjectById(Principal principal, Long id) {
        return getProfile(principal).getObjects()
                .stream()
                .filter(accessObject -> Objects.equals(accessObject.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public AccessDocument getAccessDocumentById(Principal principal,
                                                Long id) {
        return getAccessDocuments(principal)
                .stream()
                .filter(accessDocument -> Objects.equals(accessDocument.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Cacheable(value = "profile",key = "{ #principal.name, #id }")
    public List<Person> getPeopleByAccessDocumentById(Principal principal,
                                                      Long id) {
        AccessDocument accessDocument = getAccessDocumentById(principal, id);
        if (accessDocument != null)
            return accessDocument.getPeoples()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());
        return null;
    }

    public List<AccessObject> getObjectsByAccessDocumentId(Principal principal,
                                                           Long id) {
        AccessDocument accessDocument = getAccessDocumentById(principal, id);
        if (accessDocument != null)
            return accessDocument.getAccessObjects()
                    .stream()
                    .filter(accessObject -> getProfile(principal).getObjects().contains(accessObject))
                    .collect(Collectors.toList());
        return null;
    }

    public List<Person> findPeopleByRequest(Principal principal,
                                            SearchPersonRequest searchPersonRequest) {
        List<AccessDocument> accessDocuments = getAccessDocuments(principal);
        Set<Person> people = new HashSet<>();
        for (AccessDocument accessDocument : accessDocuments)
            people.addAll(accessDocument.getPeoples());
        searchPersonRequest.setLastname(searchPersonRequest.getLastname() == null ? "" : searchPersonRequest.getLastname());
        searchPersonRequest.setFirstname(searchPersonRequest.getFirstname() == null ? "" : searchPersonRequest.getFirstname());
        searchPersonRequest.setPatronymic(searchPersonRequest.getPatronymic() == null ? "" : searchPersonRequest.getPatronymic());
        return people
                .stream()
                .filter(person -> person.getLastname().toLowerCase(Locale.ROOT).startsWith(searchPersonRequest.getLastname().toLowerCase(Locale.ROOT)))
                .filter(person -> person.getFirstname().toLowerCase(Locale.ROOT).contains(searchPersonRequest.getFirstname().toLowerCase(Locale.ROOT)))
                .filter(person -> person.getPatronymic().toLowerCase(Locale.ROOT).contains(searchPersonRequest.getPatronymic().toLowerCase(Locale.ROOT)))
                .sorted()
                .collect(Collectors.toList());
    }

    public ObjectNameResponse findObjectName(ObjectNameRequest objectNameRequest) {
        return new ObjectNameResponse(AccessConstants.getTitle(objectNameRequest.getObjectName()));
    }
}
