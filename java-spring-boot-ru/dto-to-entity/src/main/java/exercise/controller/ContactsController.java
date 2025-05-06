package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO createContact(@RequestBody ContactCreateDTO contactData) {
        var contact = toEntity(contactData);
        contactRepository.save(contact);
        return toDTO(contact);
    }

    private Contact toEntity(ContactCreateDTO dto) {
        var contact = new Contact();
        contact.setPhone(dto.getPhone());
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        return contact;
    }

    private ContactDTO toDTO(Contact contact) {
        var contactDTO = new ContactDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setPhone(contact.getPhone());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setCreatedAt(contact.getCreatedAt());
        contactDTO.setUpdatedAt(contact.getUpdatedAt());
        return contactDTO;
    }
    // END
}
