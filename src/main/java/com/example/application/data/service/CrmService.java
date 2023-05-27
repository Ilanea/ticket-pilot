package com.example.application.data.service;

import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Priority;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.PriorityRepository;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final StatusRepository statusRepository;

    private final PriorityRepository priorityRepository;

    public CrmService(ContactRepository contactRepository,
                      StatusRepository statusRepository,
                      PriorityRepository priorityRepository) {
        this.contactRepository = contactRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }


    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
    public List<Priority> findAllPriorities(){
        return priorityRepository.findAll();
    }


}