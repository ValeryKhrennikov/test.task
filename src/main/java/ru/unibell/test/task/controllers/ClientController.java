package ru.unibell.test.task.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.unibell.test.task.entities.Client;
import ru.unibell.test.task.entities.Contact;
import ru.unibell.test.task.exceptions.InvalidInputException;
import ru.unibell.test.task.services.ClientService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    //1) Добавление нового клиента
    @PostMapping("/clients")
    public ResponseEntity<Client> addNewClient(@RequestBody Client client) {
        //validate Name, BirthDate, email

        validateClientInput(client);
        Client newClient = clientService.addNewClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);

    }

    //2) Добавление нового контакта клиента (телефон или email)
    @PostMapping("/clients/{clientId}/contacts")
    public ResponseEntity<Contact> addContact(
            @PathVariable Long clientId,
            @RequestParam String type,
            @RequestParam String value
    ) {
        Contact newContact = clientService.addNewContactToClient(clientId, type, value);
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    //3) Получение списка клиентов
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    //4) Получение информации по заданному клиенту (по id)
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }


    //5) Получение списка контактов заданного клиента
    @GetMapping("/clients/{clientId}/contacts")
    public ResponseEntity<List<Contact>> getContactsByClientId(@PathVariable Long clientId) {
        List<Contact> contacts = clientService.getClientContacts(clientId);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    //6) Получение списка контактов заданного типа заданного клиента
    @GetMapping("/clients/{clientId}/contacts/{contactType}")
    public ResponseEntity<List<Contact>> getContactsByClientIdAndType(
            @PathVariable Long clientId,
            @PathVariable String contactType
    ) {
        List<Contact> contacts = clientService.getClientContactsByType(clientId, contactType);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    public void validateClientInput(Client clientInput) {
        String nameRegex = "\\p{L}+";
        if (!clientInput.getName().matches(nameRegex)) {
            throw new InvalidInputException("Please use letters only");
        }

        if (clientInput.getBirthDate() == null || clientInput.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Incorrect date of birth");
        }

        if (clientInput.getContacts() != null) {
            for (Contact contact : clientInput.getContacts()) {
                if (contact.getType().equalsIgnoreCase("email")) {
                    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                    if (!contact.getValue().matches(emailRegex))
                        throw new InvalidInputException("Invalid email address");
                }
                if (contact.getType().equalsIgnoreCase("telephone")) {
                    String telephoneRegex = "^\\+7\\d{10}$";
                    if (!contact.getValue().matches(telephoneRegex))
                        throw new InvalidInputException("Invalid telephone number." +
                                " It should be like +7 beyond 10 digits");
                }
            }

        }
    }
}
