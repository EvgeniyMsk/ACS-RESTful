package ou.acs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.User;
import ou.acs.service.AccessObjectService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/objects")
@PropertySource("classpath:/application.yaml")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccessObjectController {
    private final AccessObjectService accessObjectService;

    @Autowired
    public AccessObjectController(AccessObjectService accessObjectService) {
        this.accessObjectService = accessObjectService;
    }

    @GetMapping()
    public ResponseEntity<List<AccessObject>> findAll() {
        return ResponseEntity.ok(accessObjectService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<AccessObject> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accessObjectService.findById(id));
    }

    @GetMapping("{id}/users")
    public ResponseEntity<List<User>> findAllUsersByAccessObjectId(@PathVariable Long id) {
        return ResponseEntity.ok(accessObjectService.findAllUsersByAccessObjectId(id));
    }

    @GetMapping("{id}/accessdocuments")
    public ResponseEntity<List<AccessDocument>> findAllAccessDocumentsByAccessObjectId(@PathVariable Long id) {
        return ResponseEntity.ok(accessObjectService.findAllAccessDocumentsByAccessObjectId(id));
    }
}
