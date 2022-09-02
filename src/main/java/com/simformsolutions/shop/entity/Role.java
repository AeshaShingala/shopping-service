package com.simformsolutions.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"users"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<User> users;
}
