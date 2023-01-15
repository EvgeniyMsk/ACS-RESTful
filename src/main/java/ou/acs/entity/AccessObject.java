package ou.acs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
public class AccessObject implements Comparable<AccessObject> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH },
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH },
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AccessDocument> accessDocuments;

    public AccessObject(String title) {
        this.title = title;
        this.users = new ArrayList<>();
    }

    public void addUser(User user)
    {
        this.users.add(user);
    }

    public AccessObject() {
        this.accessDocuments = new LinkedList<>();
    }


    public boolean hasUser(User user) {
        for (User iterUser : this.getUsers())
            if (iterUser.getUsername().equals(user.getUsername()))
                return true;
        return false;
    }

    @Override
    public int compareTo(AccessObject o) {
        return Long.compare(this.id, o.id);
    }
}
