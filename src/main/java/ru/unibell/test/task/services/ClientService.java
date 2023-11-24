package ru.unibell.test.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.unibell.test.task.entities.Client;
import ru.unibell.test.task.entities.Contact;
import ru.unibell.test.task.exceptions.UserNotFoundException;
import ru.unibell.test.task.repo.ClientRepository;
import ru.unibell.test.task.repo.ContactRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    public Client addNewClient(Client clientRequest) {
        Client newClient = new Client();
        newClient.setName(clientRequest.getName());
        newClient.setBirthDate(clientRequest.getBirthDate());
        newClient.setFavoriteColor(clientRequest.getFavoriteColor());
        newClient.setAdditionalInfo(clientRequest.getAdditionalInfo());

        // Если есть контакты, установим их связь с клиентом
        if (clientRequest.getContacts() != null) {
            for (Contact contact : clientRequest.getContacts()) {
                contact.setClient(newClient);
            }
            newClient.setContacts(clientRequest.getContacts());
        }

        return clientRepository.save(newClient);
    }


    public Contact addNewContactToClient(Long clientId, String type, String value) {
        Client client = getClientById(clientId);
        Contact contact = new Contact();
        contact.setType(type);
        contact.setValue(value);
        contact.setClient(client);
        client.getContacts().add(contact);
        clientRepository.save(client);
        return contact;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException("Client not found with id: " + clientId));
    }

    public List<Contact> getClientContacts(Long clientId) {
        Client client = getClientById(clientId);
        return client.getContacts();
    }

    public List<Contact> getClientContactsByType(Long clientId, String contactType) {
        Client client = getClientById(clientId);
        return client.getContacts().stream()
                .filter(contact -> contact.getType().equalsIgnoreCase(contactType))
                .collect(Collectors.toList());
    }
}
