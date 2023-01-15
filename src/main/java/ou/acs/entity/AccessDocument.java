package ou.acs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ou.acs.entity.dao.AccessDocumentDAO;
import ou.acs.entity.dao.PersonDAO;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class AccessDocument implements Comparable<AccessDocument> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long documentId;
    private Long objectId;
    private Long companyId;
    private String companyName;
    private String companyInn;
    private String companyOgrn;
    private String companyOgrnIp;
    private Long departmentId;
    private String departmentName;
    private String subscriptionNumber;
    private String subscriptionDate;
    private String performerFinishedDatetime;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "accessDocument",
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Person> peoples;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH },
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AccessObject> accessObjects;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "accessDocument",
            fetch = FetchType.EAGER)
    private List<Comment> comments;

    public AccessDocument() {
        this.peoples = new HashSet<>();
        this.accessObjects = new LinkedList<>();
        this.comments = new ArrayList<>();
    }

    public AccessDocument(AccessDocumentDAO accessDocumentDAO) {
        this.documentId = accessDocumentDAO.getDocument_id();
        this.objectId = accessDocumentDAO.getObject_id();
        this.companyId = accessDocumentDAO.getCompany_id();
        this.companyName = accessDocumentDAO.getCompany_name();
        this.companyInn = accessDocumentDAO.getCompany_inn();
        this.companyOgrn = accessDocumentDAO.getCompany_ogrn();
        this.companyOgrnIp = accessDocumentDAO.getCompany_ogrn_ip();
        this.departmentId = accessDocumentDAO.getDepartment_id();
        this.departmentName = accessDocumentDAO.getDepartment_name();
        this.subscriptionNumber = accessDocumentDAO.getSubscription_number();
        this.subscriptionDate = accessDocumentDAO.getSubscription_date();
        this.performerFinishedDatetime = accessDocumentDAO.getPerfomer_finished_datetime();
        this.peoples = new HashSet<>();
        this.accessObjects = new LinkedList<>();
        for (PersonDAO personDAO : accessDocumentDAO.getPeoples())
            peoples.add(new Person(personDAO));
        this.comments = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessDocument)) return false;
        AccessDocument that = (AccessDocument) o;
        return getSubscriptionNumber().equals(that.getSubscriptionNumber()) &&
                getSubscriptionDate().equals(that.getSubscriptionDate()) &&
                getDocumentId().equals(that.getDocumentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubscriptionNumber(), getSubscriptionDate(), getDocumentId());
    }

    @Override
    public int compareTo(AccessDocument accessDocument) {
        return Long.compare(this.id, accessDocument.id);
    }
}
