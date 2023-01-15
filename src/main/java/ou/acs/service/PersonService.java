package ou.acs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ou.acs.constants.AccessConstants;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Person;
import ou.acs.entity.User;
import ou.acs.entity.roles.ERole;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.repository.DepartmentRepository;
import ou.acs.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;

    private final AccessDocumentRepository accessDocumentRepository;

    @Autowired
    public PersonService(UserRepository userRepository,
                         DepartmentRepository departmentRepository,
                         AccessDocumentRepository accessDocumentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.accessDocumentRepository = accessDocumentRepository;
    }

    public List<Person> findPersonByTags(Principal principal, String tags) {
        List<Person> personList = findAllPersonBySecurityUnit(userRepository.findByUsername(principal.getName()));
        Set<Person> result = new LinkedHashSet<>();
        try {
            String[] tagList = tags.split(" ");
            for (Person person : personList) {
                boolean contains = false;
                for (String i : tagList) {
                    if (person.getName().toLowerCase().contains(i.toLowerCase()) ||
                            person.getAccessDocument().getSubscriptionNumber().toLowerCase().contains(i.toLowerCase()) ||
                            person.getAccessDocument().getCompanyName().toLowerCase().contains(i.toLowerCase()))
                        contains = true;
                    else
                    {
                        contains = false;
                        break;
                    }
                }
                if (contains)
                    result.add(person);
            }
            return new ArrayList<>(result);
        } catch (Exception ignored) {
        }
        return null;
    }

    public List<Person> findAllPersonBySecurityUnit(User user) {
        List<Person> personList = new ArrayList<>();
        for (AccessObject accessObject : user.getObjects())
            for (AccessDocument accessDocument : accessObject.getAccessDocuments())
                personList.addAll(accessDocument.getPeoples());
        return personList;
    }

//    public List<Person> findPeopleFromDocument(Principal principal,
//                                               Long number) {
//        List<Person> personList = new ArrayList<>();
//        User user = userRepository.findByUsername(principal.getName());
//        if (user.getRoles().contains(ERole.ROLE_USER)) {
//            for (AccessObject accessObject : user.getObjects())
//                for (AccessDocument accessDocument : accessObject.getAccessDocuments())
//                    personList.addAll(accessDocument.getPeoples().stream()
//                            .filter(person -> person.getAccessDocument().getDocumentId().equals(number)).collect(Collectors.toList()));
//            return personList;
//        } else if (user.getRoles().contains(ERole.ROLE_CUSTOMER)) {
//            Set<AccessDocument> accessDocuments = accessDocumentRepository.findByDocumentId(number);
//            for (AccessDocument accessDocument : accessDocuments)
//                personList.addAll(accessDocument.getPeoples().stream()
//                        .filter(person -> person.getAccessDocument().getDocumentId().equals(number)).collect(Collectors.toList()));
//            return personList;
//        } else
//            return personList;
//    }

    public List<Person> findPersonByTagsToCheck(Principal principal, String tags) {
        List<Person> personList = new ArrayList<>();
        String departmentId = getDepartmentId(AccessConstants.getTitle(principal.getName()));
        List<AccessDocument> documentList = accessDocumentRepository.findByDepartmentId(Long.parseLong(departmentId));
        for (AccessDocument accessDocument : documentList)
            personList.addAll(accessDocument.getPeoples());
        Set<Person> result = new LinkedHashSet<>();
        try {
            String[] tagList = tags.split(" ");
            for (Person person : personList) {
                boolean contains = false;
                for (String i : tagList) {
                    if (person.getName().toLowerCase().contains(i.toLowerCase()) ||
                            person.getAccessDocument().getSubscriptionNumber().toLowerCase().contains(i.toLowerCase()) ||
                            person.getAccessDocument().getCompanyName().toLowerCase().contains(i.toLowerCase()))
                        contains = true;
                    else
                    {
                        contains = false;
                        break;
                    }
                }
                if (contains)
                    result.add(person);
            }
            return new ArrayList<>(result);
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getDepartmentId(String department) {
        return departmentRepository.findByDepartmentName(department).getId().toString();
    }
}
