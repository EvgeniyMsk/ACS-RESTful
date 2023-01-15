package ou.acs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ou.acs.entity.dao.PersonDAO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Comparable<Person> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastname;
    private String firstname;
    private String patronymic;
    private String name;
    private String birthdayIso;
    private String birthday;
    private String serial;
    private String number;
    private String passport;
    private String foreignSerial;
    private String foreignNumber;
    private String foreignPassport;
    private String birthName;
    private String reasonId;
    private String reasonDescription;
    private String reasonResult;
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH },
            fetch = FetchType.LAZY)
//    @JsonIgnore
    private AccessDocument accessDocument;

    public Person(PersonDAO personDAO) {
        this.lastname = personDAO.getLastname();
        this.firstname = personDAO.getFirstname();
        this.patronymic = personDAO.getPatronymic();
        this.name = personDAO.getName();
        this.birthdayIso = personDAO.getBirthday_iso();
        this.birthday = personDAO.getBirthday();
        this.serial = personDAO.getSerial();
        this.number = personDAO.getNumber();
        this.passport = personDAO.getPassport();
        this.foreignSerial = personDAO.getForeign_serial();
        this.foreignNumber = personDAO.getForeign_number();
        this.foreignPassport = personDAO.getForeign_passport();
        this.birthName = personDAO.getBirth_name();
        this.reasonId = personDAO.getReason_id();
        this.reasonDescription = personDAO.getReason_description();
        this.reasonResult = personDAO.getReason_result();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) &&
                Objects.equals(getBirthdayIso(), person.getBirthdayIso()) &&
                Objects.equals(getPassport(), person.getPassport()) &&
                Objects.equals(getForeignPassport(), person.getForeignPassport()) &&
                Objects.equals(getAccessDocument(), person.getAccessDocument());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getBirthdayIso(),
                getPassport(),
                getForeignPassport(),
                getAccessDocument());
    }

    @Override
    public int compareTo(Person o) {
        return name.compareToIgnoreCase(o.getName());
    }
}
