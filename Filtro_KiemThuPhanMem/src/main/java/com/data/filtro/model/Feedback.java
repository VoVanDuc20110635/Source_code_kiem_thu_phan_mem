package com.data.filtro.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "phanhoi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "masp")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "makh")
    @JsonManagedReference
    private User user;

    @Column(name = "noidung")
    private String content;

    @Column(name = "ngayph")
    private Date date;

    @Column(name = "sosao")
    private int stars;

}