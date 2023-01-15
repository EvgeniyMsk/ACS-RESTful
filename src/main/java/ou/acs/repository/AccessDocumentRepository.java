package ou.acs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.dao.SearchResult;

import java.util.List;
import java.util.Set;

@Repository
public interface AccessDocumentRepository extends JpaRepository<AccessDocument, Long> {
//    Set<AccessDocument> findByDocumentId(Long id);
    List<AccessDocument> findByDepartmentId(Long id);

    @Query(value = "select company_name, lastname, firstname, patronymic, birthday, birth_name, passport, reason_result from access_document\n" +
            "    inner join person p on access_document.id = p.access_document_id\n" +
            "         where access_document.id in\n" +
            "    (select access_document_id from access_document_access_objects\n" +
            "         where access_document_access_objects.access_objects_id in\n" +
            "               (select id from access_object where user_id=:userId))", nativeQuery = true)
    List<SearchResult> findTheFuckingResult(@Param("userId") Long userId);

    AccessDocument findByDocumentId(Long docId);
}
