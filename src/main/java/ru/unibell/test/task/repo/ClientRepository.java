package ru.unibell.test.task.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unibell.test.task.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
