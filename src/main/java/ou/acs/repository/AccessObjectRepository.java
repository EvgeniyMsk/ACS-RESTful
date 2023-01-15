package ou.acs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ou.acs.entity.AccessObject;

import java.util.List;

@Repository
public interface AccessObjectRepository extends JpaRepository<AccessObject, Long> {
    AccessObject findByTitle(String title);
}
