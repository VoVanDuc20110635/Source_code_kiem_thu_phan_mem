package com.data.filtro.repository;

import com.data.filtro.model.OrderShipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderShipperRepository extends JpaRepository<OrderShipper, Integer> {
    @Query("select c from OrderShipper c where c.userId = :userId and c.status = :status")
    List<OrderShipper> findByShipperIdAndStatus(@Param("userId") int userId, @Param("status") int status);

    @Query("select c from OrderShipper c where c.orderId = :orderId")
    OrderShipper findByOrderId(@Param("orderId") int orderId);

    @Query("select c from OrderShipper c where c.status = 2")
    List<OrderShipper> findOrderDontDelivery();

//    @Modifying
//    @Query(value = "INSERT INTO DonHangShipper (orderId, userId, status) VALUES (:orderId, :userId, :status)", nativeQuery = true)
//    void add(@Param("orderId") int orderId, @Param("userId") int userId, @Param("status") int status);
//
//    @Modifying
//    @Query("update DonHangShipper o set o.status = :status where o.orderId =:orderId")
//    void update(@Param("status") int status, @Param("orderId") int orderId);

//    @Query("update OrderShipper o set o.status = :status where o.orderId =:orderId")
//    void update (@Param("status") int status, @Param("orderId") int orderId);
//
//    @Modifying
//    @Query(value = "insert into DonHangShipper (orderId, userId, status) values (:orderId, :userId, :status)", nativeQuery = true)
//    void add (@Param("orderId") int orderId, @Param("userId") int userId, @Param("status") int status);
}
