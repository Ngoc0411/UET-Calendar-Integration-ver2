package com.team2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;
    
    @Column(name = "uet_courses_id")
    private Long uet_courses_id;
    
    @Column(name = "google_id")
    private Long google_id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String password, Long uet_courses_id, Long google_id) {
        this.username = username;
        this.password = password;
        this.uet_courses_id = uet_courses_id;
        this.google_id = google_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUet_courses_id() {
        return uet_courses_id;
    }

    public void setUet_courses_id(Long uet_courses_id) {
        this.uet_courses_id = uet_courses_id;
    }
    
    public Long getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(Long id) {
        this.google_id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}