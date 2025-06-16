package com.apievento.app.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizerService {

    private final OrganizerRepository organizerRepository;

    @Autowired
    public OrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    // Criar um novo organizador
    public Organizer createOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    // Listar todos os organizadores
    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    // Buscar organizador por ID
    public Optional<Organizer> getOrganizerById(Long id) {
        return organizerRepository.findById(id);
    }

    // Atualizar organizador
    public Organizer updateOrganizer(Long id, Organizer organizerDetails) {
        Optional<Organizer> organizerOptional = organizerRepository.findById(id);

        if (organizerOptional.isPresent()) {
            Organizer organizer = organizerOptional.get();
            organizer.setName(organizerDetails.getName());
            organizer.setCpfCnpj(organizerDetails.getCpfCnpj());
            organizer.setEmail(organizerDetails.getEmail());
            organizer.setPhone(organizerDetails.getPhone());
            // Não atualizamos a senha diretamente aqui (deve ter um endpoint específico)
            return organizerRepository.save(organizer);
        }
        return null;
    }

    // Deletar organizador (soft delete)
    public void softDeleteOrganizer(Long id) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        if (organizer.getDeletedAt() == null) {
            organizer.setDeletedAt(new Date());
            organizerRepository.save(organizer);
        }
    }

    // Buscar por email (para login)
    public Organizer findByEmail(String email) {
        return organizerRepository.findByEmail(email);
    }

    // Buscar por CPF/CNPJ
    public Organizer findByCpfCnpj(String cpfCnpj) {
        return organizerRepository.findByCpfCnpj(cpfCnpj);
    }
}
