package ou.acs.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Department {
    @Id
    private Long id;
    @Column(unique = true)
    private String departmentName;

    public Department(Long id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }
}
