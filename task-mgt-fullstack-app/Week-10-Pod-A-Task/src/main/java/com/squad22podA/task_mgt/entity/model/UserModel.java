package com.squad22podA.task_mgt.entity.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.squad22podA.task_mgt.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel extends BaseClass implements UserDetails {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private  String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Transient
    private String confirmPassword; // this field won't be persisted in the database,  it's used transiently for validation purposes only.

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = false;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Task> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "userModel")
    private List<JToken> jtokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
        return enabled;
    }
}
