package com.data.filtro.repository;

import com.data.filtro.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    @Query("select a from PaymentMethod a where a.id = :id ")
    PaymentMethod getMethodById(@Param("id") int id);
}
