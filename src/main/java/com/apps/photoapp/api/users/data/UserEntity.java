package com.apps.photoapp.api.users.data;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    private String password;
    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false,  unique = true)
    private String userId;

    @Column(nullable = false,  unique = true)
    private String encPassword;
}
