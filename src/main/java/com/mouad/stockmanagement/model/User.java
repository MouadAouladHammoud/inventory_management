package com.mouad.stockmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.mouad.stockmanagement.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "password")
    private String password;

    @Embedded
    private Address address;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}
