package ou.acs.entity.dao;

import lombok.Data;

@Data
public class PersonDAO {
    private String lastname;
    private String firstname;
    private String patronymic;
    private String name;
    private String birthday_iso;
    private String birthday;
    private String serial;
    private String number;
    private String passport;
    private String foreign_serial;
    private String foreign_number;
    private String foreign_passport;
    private String birth_name;
    private String reason_id;
    private String reason_description;
    private String reason_result;

    @Override
    public String toString() {
        return "PersonDAO{" +
                "lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", name='" + name + '\'' +
                ", birthday_iso='" + birthday_iso + '\'' +
                ", birthday='" + birthday + '\'' +
                ", serial='" + serial + '\'' +
                ", number='" + number + '\'' +
                ", passport='" + passport + '\'' +
                ", foreign_serial='" + foreign_serial + '\'' +
                ", foreign_number='" + foreign_number + '\'' +
                ", foreign_passport='" + foreign_passport + '\'' +
                ", birth_name='" + birth_name + '\'' +
                ", reason_id='" + reason_id + '\'' +
                ", reason_description='" + reason_description + '\'' +
                ", reason_result='" + reason_result + '\'' +
                '}';
    }
}
