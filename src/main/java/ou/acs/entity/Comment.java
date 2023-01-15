package ou.acs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Comment implements Comparable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE })
    @JsonIgnore
    private AccessDocument accessDocument;

    public Comment(String text, AccessDocument accessDocument) {
        this.text = text;
        this.accessDocument = accessDocument;
    }

    @Override
    public int compareTo(Comment o) {
        return Long.compare(this.id, o.id);
    }
}
