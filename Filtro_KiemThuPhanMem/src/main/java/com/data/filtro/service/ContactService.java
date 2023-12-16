package com.data.filtro.service;

import com.data.filtro.model.Contact;
import com.data.filtro.model.Feedback;
import com.data.filtro.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    public void createContact(Contact contact) {
        contactRepository.save(contact);
    }
    public List<Contact> loadAllContact(){
        List<Contact> contacts = new ArrayList<>();
        contacts = contactRepository.loadContact();
        return contacts;
    }
}
