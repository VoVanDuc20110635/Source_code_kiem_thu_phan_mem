package com.data.filtro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "donhangdagiaouser")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShipper {
    @Id
    @Column(name = "orderId")
    private int orderId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "trangthai")
    private int status;
}
