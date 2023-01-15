package ou.acs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ou.acs.entity.dao.UserDAO;
import ou.acs.entity.roles.ERole;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "usr",
uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Getter
@Setter
public class User implements UserDetails, Comparable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ERole> roles;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH },
            fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AccessObject> objects;

    private boolean active;

    private Date lastDate;

    public User() {
        this.objects = new HashSet<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        Set<ERole> roles = new HashSet<>();
        if (username.equals("admin"))
            roles.add(ERole.ROLE_ADMIN);
        else
            roles.add(ERole.ROLE_USER);
        this.roles = roles;
        this.objects = new HashSet<>();
    }

    public User(UserDAO userDAO) {
        this.username = userDAO.getUsername();
        this.password = userDAO.getPassword();
        this.objects = new HashSet<>();
        Set<ERole> roles = new HashSet<>();
        switch (userDAO.getRole())
        {
            case "admin" : { roles.add(ERole.ROLE_ADMIN); break; }
            case "customer" : { roles.add(ERole.ROLE_CUSTOMER); break; }
            default: { roles.add(ERole.ROLE_USER); break; }
        }
        this.roles = roles;
    }

    public boolean hasObject(String title) {
        for (AccessObject object : this.getObjects())
            if (object.getTitle().equals(title))
                return true;
        return false;
    }

    public void addObject(AccessObject object) {
        object.addUser(this);
        objects.add(object);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (ERole role : roles)
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ERole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public int compareTo(User o) {
        {
            return Long.compare(this.id, o.id);
        }
    }
}
