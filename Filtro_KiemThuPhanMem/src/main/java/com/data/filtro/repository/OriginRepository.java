package com.data.filtro.repository;

import com.data.filtro.model.Origin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Integer> {

    @Query("select f from Origin f where f.id =:id")
    Origin findById(@Param("id") int id);

    @Modifying
    @Query("update Origin f set f.status=0 where f.id=:id")
    void deleteById(@Param("id") int id);

    Page<Origin> findAll(Pageable pageable);

    @Query("select a from Origin a where a.status = :status")
    List<Origin> activeMaterials(@Param("status") int status);
}
