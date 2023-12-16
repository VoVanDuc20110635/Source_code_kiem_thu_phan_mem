package com.data.filtro.repository;

import com.data.filtro.model.Contact;
import com.data.filtro.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query("SELECT f FROM Contact f")
    List<Contact> loadContact();

}
