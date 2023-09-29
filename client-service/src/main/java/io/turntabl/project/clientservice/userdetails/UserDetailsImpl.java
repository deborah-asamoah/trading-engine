package io.turntabl.project.clientservice.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.turntabl.project.clientprocessingapi.enums.Role;
import io.turntabl.project.persistence.entities.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {

    private static final UUID serialVersionUID = UUID.fromString("56654bcf-1133-4cd6-97a5-503d85c961eb");

    private final UUID id;

    private final String name;

    private final String email;

    private final Role role;

    @JsonIgnore
    private final String password;

    public UserDetailsImpl(UUID id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserDetailsImpl build(Client client) {

        return new UserDetailsImpl(client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPassword(),
                client.getRole()
        );
    }


    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
