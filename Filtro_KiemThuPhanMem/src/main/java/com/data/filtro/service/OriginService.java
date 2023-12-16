package com.data.filtro.service;

import com.data.filtro.model.Origin;
import com.data.filtro.repository.OriginRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OriginService {

    @Autowired
    OriginRepository materialRepository;

    public Origin getMaterialById(int id) {
        return materialRepository.findById(id);
    }

    public Page<Origin> getAllPaging(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    public void create(Origin flavor) {
        materialRepository.save(flavor);
    }


    public void update(Origin flavor) {
        Origin newFlavor = getMaterialById(flavor.getId());
        newFlavor.setOriginName(flavor.getOriginName());
        newFlavor.setDescription(flavor.getDescription());
        newFlavor.setStatus(flavor.getStatus());
        materialRepository.save(newFlavor);
    }

    @Transactional
    public void delete(int id) {
        materialRepository.deleteById(id);
    }

    public List<Origin> getAll() {
        return materialRepository.findAll();
    }
    public List<Origin> getActiveMaterial(int status){
        return materialRepository.activeMaterials(status);
    }
}
