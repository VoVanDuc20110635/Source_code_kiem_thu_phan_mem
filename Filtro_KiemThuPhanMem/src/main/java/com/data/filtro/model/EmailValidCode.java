package com.data.filtro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;

@Entity
@Table(name = "emailcode")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailValidCode {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "maxacthuc")
    private String validCode;
}
