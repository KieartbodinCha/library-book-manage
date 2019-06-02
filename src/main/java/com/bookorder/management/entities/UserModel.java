package com.bookorder.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "username")
    private String username;

    @NotNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "birth_date")
    private Date bithdate;

    @Column(name = "role_name")
    private String roleName;

}
