package com.data.filtro.controller.user;

import com.data.filtro.model.Contact;
import com.data.filtro.model.Feedback;
import com.data.filtro.model.User;
import com.data.filtro.service.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @GetMapping
    public String loadContactPage(){
        return "user/boot1/contact";
    }

    @PostMapping
    public String addContact(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("subject") String subject,
                             @RequestParam("message") String message,
                             HttpSession session, Model model) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setSubject(subject);
        contact.setMessage(message);
        contactService.createContact(contact);
        return "redirect:/contact";
    }
}
