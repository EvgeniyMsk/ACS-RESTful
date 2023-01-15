package ou.acs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.Person;
import ou.acs.entity.dao.SearchResult;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByAccessDocumentAndNameAndBirthday(AccessDocument accessDocument, String name, String birthday);
}
