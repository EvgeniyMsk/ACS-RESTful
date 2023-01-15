package ou.acs.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ObjectNameResponse {
    private String objectName;

    public ObjectNameResponse(String objectName) {
        this.objectName = objectName;
    }
}
