package com.apievento.app.Organizer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    Organizer findByEmail(String email);
    Organizer findByCpfCnpj(String cpfCnpj);
}
