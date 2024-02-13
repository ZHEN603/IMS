package com.ims.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "u_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String name;
    private String description;
    private String companyId;
    private Integer state;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<User>(0);


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="u_role_permission",
            joinColumns={@JoinColumn(name="role_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="permission_id",referencedColumnName="id")})
    private Set<Permission> permissions = new HashSet<Permission>(0);
}
