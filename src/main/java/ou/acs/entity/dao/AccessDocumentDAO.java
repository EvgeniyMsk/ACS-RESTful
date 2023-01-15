package ou.acs.entity.dao;

import lombok.Data;

import java.util.List;

@Data
public class AccessDocumentDAO {
    private Long document_id;
    private Long object_id;
    private Long company_id;
    private String company_name;
    private String company_inn;
    private String company_ogrn;
    private String company_ogrn_ip;
    private Long department_id;
    private String department_name;
    private String subscription_number;
    private String subscription_date;
    private String perfomer_finished_datetime;
    private List<PersonDAO> peoples;
}
