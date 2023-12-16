package com.data.filtro.repository;

import com.data.filtro.model.Category;
import com.data.filtro.model.EmailValidCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidCodeRepository extends JpaRepository<EmailValidCode, Integer> {

    @Query("select c from EmailValidCode c where c.email = :email")
    EmailValidCode findByEmail(@Param(("email")) String email);

    @Modifying
    @Query("update EmailValidCode c set c.validCode = :validCode where c.email = :email ")
    void changeValidCode(@Param("email") String email, @Param("validCode") String validCode);

}
