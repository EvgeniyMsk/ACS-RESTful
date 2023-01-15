package ou.acs.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchPersonRequest {
    private String lastname;
    private String firstname;
    private String patronymic;
}
