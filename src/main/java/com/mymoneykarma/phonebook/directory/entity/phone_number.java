package com.mymoneykarma.phonebook.directory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "phone_number")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="phone_number")
public class phone_number implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name="id")
    private int id ; // NOT NULL,
    @Column(name="number")
    private String number ; // character //varying(40),
    @Column(name="account_id")
    private int account_id ;// integer
}
