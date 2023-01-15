package ou.acs.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Person;
import ou.acs.entity.User;
import ou.acs.requests.ObjectNameRequest;
import ou.acs.requests.SearchPersonRequest;
import ou.acs.responses.ObjectNameResponse;
import ou.acs.service.DataService;
import ou.acs.service.ExportService;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {
    private final DataService dataService;
    private final ExportService exportService;

    @Autowired
    public DataController(DataService dataService,
                          ExportService exportService) {
        this.dataService = dataService;
        this.exportService = exportService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> profile(Principal principal) {
        return ResponseEntity.ok(dataService.getProfile(principal));
    }

    @GetMapping("/accessobjects")
    public ResponseEntity<List<AccessObject>> findAllAccessObjects(Principal principal) {
        return ResponseEntity.ok(dataService.getAccessObjects(principal));
    }

    @GetMapping("/accessdocuments")
    public ResponseEntity<List<AccessDocument>> findAllAccessDocuments(Principal principal) {
        return ResponseEntity.ok(dataService.getAccessDocuments(principal));
    }

    @GetMapping("/accessobjects/{id}")
    public ResponseEntity<AccessObject> findAccessObjectById(@PathVariable Long id,
                                                             Principal principal) {
        return ResponseEntity.ok(dataService.getAccessObjectById(principal, id));
    }

    @GetMapping("/accessdocuments/{id}")
    public ResponseEntity<AccessDocument> findAccessDocumentById(@PathVariable Long id,
                                                                 Principal principal) {
        return ResponseEntity.ok(dataService.getAccessDocumentById(principal, id));
    }

    @GetMapping("/accessdocuments/{id}/pdf")
    public void exportToPDF(@PathVariable("id") Long id,
                            Principal principal,
                            HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"pdf_" + principal.getName() + "_" + currentDateTime + ".pdf\"";
        response.addHeader(headerKey, headerValue);
        this.exportService.exportToPDF(id, response, principal);
    }

    @GetMapping("/accessdocuments/{id}/excel")
    public void exportToExcel(@PathVariable("id") Long id,
                              Principal principal,
                              HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=people_" + principal.getName() + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        this.exportService.exportToExcel(id, response);
    }

    @GetMapping("/accessdocuments/{id}/excelPassport")
    public void exportToExcelWithPassport(@PathVariable("id") Long id,
                                          Principal principal,
                                          HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=people_" + principal.getName() + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        this.exportService.exportToExcelWithPassport(id, response);
    }

    @GetMapping("/accessdocuments/{id}/objects")
    public ResponseEntity<List<AccessObject>> findObjectsByAccessDocumentId(@PathVariable Long id,
                                                                            Principal principal) {
        return ResponseEntity.ok(dataService.getObjectsByAccessDocumentId(principal, id));
    }

    @GetMapping("/accessdocuments/{id}/people")
    public ResponseEntity<List<Person>> findPeopleByAccessDocumentId(@PathVariable Long id,
                                                                       Principal principal) {
        return ResponseEntity.ok(dataService.getPeopleByAccessDocumentById(principal, id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Person>> findPeopleByRequest(Principal principal,
                                                            @RequestBody SearchPersonRequest searchPersonRequest) {
        return ResponseEntity.ok(dataService.findPeopleByRequest(principal, searchPersonRequest));
    }

    @PostMapping("/objectName")
    public ResponseEntity<ObjectNameResponse> findObjectName(@RequestBody ObjectNameRequest objectNameRequest) {
        return ResponseEntity.ok(dataService.findObjectName(objectNameRequest));
    }
}
