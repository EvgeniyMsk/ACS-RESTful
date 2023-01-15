package ou.acs.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {
    private String username;
    private String password;
    private String department;
    private String role;
}
