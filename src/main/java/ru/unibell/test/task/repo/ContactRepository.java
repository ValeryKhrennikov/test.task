package ru.unibell.test.task.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unibell.test.task.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
