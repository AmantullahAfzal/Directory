package com.mymoneykarma.phonebook.directory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="account")
public class account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name="auth_id")
    private String auth_id;
    @Column(name="username")
    private String username;

}
