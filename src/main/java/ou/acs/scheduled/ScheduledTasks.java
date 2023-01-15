package ou.acs.scheduled;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ou.acs.entity.*;
import ou.acs.entity.dao.AccessDocumentDAO;
import ou.acs.repository.DepartmentRepository;
import ou.acs.repository.UserRepository;
import ou.acs.service.AccessObjectService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@PropertySource("classpath:/application.yaml")
public class ScheduledTasks {
    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Value("${upload.directory}")
    private String uploadDirectory;

    private final UserRepository userRepository;

    private final AccessObjectService accessObjectService;

    private final DepartmentRepository departmentRepository;

    private static final List<String> accounts = new ArrayList<>();

    @Autowired
    public ScheduledTasks(UserRepository userRepository,
                          AccessObjectService accessObjectRepository,
                          DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.accessObjectService = accessObjectRepository;
        this.departmentRepository = departmentRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void reportCurrentTime() throws IOException {
        prepareDir();
        updateUserBase();
        updatePersonBase();
        log.info("Base has been updated! " + dateFormat.format(new Date()));
    }

    private void updatePersonBase() throws IOException {
        File mainDirectory = new File(uploadDirectory);
        if (mainDirectory.exists() && mainDirectory.listFiles() != null) {
            if (mainDirectory.listFiles().length > 0) {
                for (File iterObject : mainDirectory.listFiles()) {
                    if (iterObject.isDirectory() && iterObject.listFiles() != null && accounts.contains(iterObject.getName()))
                    {
                        for (File file : Objects.requireNonNull(iterObject.listFiles())) {
                            if (file.getName().startsWith(".DS"))
                                continue;
                            String objectName = file.getName().substring(0, file.getName().indexOf("."));
                            Reader reader = new FileReader(file);
                            Gson gson = new Gson();
                            AccessDocumentDAO[] documentDAOList = gson.fromJson(reader, AccessDocumentDAO[].class).clone();
                            for (AccessDocumentDAO accessDocumentDAO : documentDAOList) {
                                AccessDocument accessDocument = new AccessDocument(accessDocumentDAO);
                                Department department = new Department(accessDocument.getDepartmentId(),
                                        accessDocument.getDepartmentName());
                                departmentRepository.save(department);
                                for (Person person : accessDocument.getPeoples())
                                    person.setAccessDocument(accessDocument);
                                accessObjectService.addAccessDocument(objectName, accessDocument);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateUserBase() {
        User admin = new User("admin", "admin");
        User tmp = userRepository.findByUsername("admin");
        if (tmp == null)
            userRepository.save(admin);
        File fileDir = new File(uploadDirectory);
        if (fileDir.exists() && fileDir.listFiles() != null) {
            if (fileDir.listFiles().length > 0) {
                for (File dir : Objects.requireNonNull(fileDir.listFiles())) {
                    if (dir.isDirectory() && accounts.contains(dir.getName())
                    ) {
                        User user = new User(dir.getName(), "password");
                        User temp = userRepository.findByUsername(user.getUsername());
                        if (!user.equals(temp))
                            userRepository.save(user);
                        temp = userRepository.findByUsername(user.getUsername());
                        if (temp != null)
                        {
                            for (File iterObject : Objects.requireNonNull(dir.listFiles())) {
                                if (iterObject.getName().endsWith(".DS"))
                                    continue;
                                String object = iterObject.getName().substring(0, iterObject.getName().indexOf("."));
                                if (!temp.hasObject(object))
                                    temp.addObject(new AccessObject(object));
                                if (!admin.hasObject(object))
                                    admin.addObject(new AccessObject(object));
                            }
                            userRepository.save(temp);
                        }
                    }
                }
            }
        }
    }

    private void prepareDir() {
        createProfile("УОМК СКМК ФСО России", "skmk");
        createProfile("УОО СОМ ФСО России" + "/" + "1 отдел", "som-uoo-1");
        createProfile("УОО СОМ ФСО России" + "/" + "2 отдел", "som-uoo-2");
        createProfile("УОО СОМ ФСО России" + "/" + "3 отдел", "som-uoo-3");
        createProfile("УОО СОМ ФСО России" + "/" + "4 отдел", "som-uoo-4");
        createProfile("УОО СОМ ФСО России" + "/" + "6 отдел", "som-uoo-6");
        createProfile("УОО СОМ ФСО России" + "/" + "8 отдел", "som-uoo-8");
        createProfile("УОО СОМ ФСО России" + "/" + "9 отдел", "som-uoo-9");
        createProfile("ЦОИ «Энергия» ФСО России", "energia");
        createProfile("1 УО СБП ФСО России", "sbp-uo-1");
        createProfile("2 УО СБП ФСО России", "sbp-uo-2");
        createProfile("3 УО СБП ФСО России", "sbp-uo-3");
        createProfile("Академия ФСО России", "academy");
        createProfile("ВИПС (филиал) Академии ФСО России", "vips");
    }

    private void createProfile(String from, String dest) {
        try {
            new File(uploadDirectory + "/" + dest).mkdir();
            for (File file : new File(uploadDirectory + "/" + from).listFiles())
                file.renameTo(new File(uploadDirectory + "/" + dest + "/" + file.getName()));
            accounts.add(dest);
        } catch (Exception e) {}
    }

}
