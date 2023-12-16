package com.data.filtro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Table(name = "xuatxu")
@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maxuatxu")
    private Integer id;

    @Column(name = "tenxuatxu")
    private String originName;

    @Column(name = "mota")
    private String description;

    @Column(name = "tinhtrang")
    private Integer status;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Product> products;

}
