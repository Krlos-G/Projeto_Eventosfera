package com.apievento.app.Controller;

import com.apievento.app.Organizer.Organizer;
import com.apievento.app.Organizer.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {

    private final OrganizerService organizerService;

    @Autowired
    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    // Criar novo organizador
    @PostMapping
    public ResponseEntity<Organizer> createOrganizer(@RequestBody Organizer organizer) {
        Organizer newOrganizer = organizerService.createOrganizer(organizer);
        return new ResponseEntity<>(newOrganizer, HttpStatus.CREATED);
    }

    // Listar todos os organizadores
    @GetMapping
    public ResponseEntity<List<Organizer>> getAllOrganizers() {
        List<Organizer> organizers = organizerService.getAllOrganizers();
        return new ResponseEntity<>(organizers, HttpStatus.OK);
    }

    // Buscar organizador por ID
    @GetMapping("/{id}")
    public ResponseEntity<Organizer> getOrganizerById(@PathVariable Long id) {
        Optional<Organizer> organizer = organizerService.getOrganizerById(id);
        return organizer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualizar organizador
    @PutMapping("/{id}")
    public ResponseEntity<Organizer> updateOrganizer(@PathVariable Long id, @RequestBody Organizer organizerDetails) {
        Organizer updatedOrganizer = organizerService.updateOrganizer(id, organizerDetails);
        if (updatedOrganizer != null) {
            return new ResponseEntity<>(updatedOrganizer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Deletar organizador (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteOrganizer(@PathVariable Long id) {
        organizerService.softDeleteOrganizer(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint adicional para buscar por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Organizer> getOrganizerByEmail(@PathVariable String email) {
        Organizer organizer = organizerService.findByEmail(email);
        if (organizer != null) {
            return new ResponseEntity<>(organizer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint adicional para buscar por CPF/CNPJ
    @GetMapping("/cpf-cnpj/{cpfCnpj}")
    public ResponseEntity<Organizer> getOrganizerByCpfCnpj(@PathVariable String cpfCnpj) {
        Organizer organizer = organizerService.findByCpfCnpj(cpfCnpj);
        if (organizer != null) {
            return new ResponseEntity<>(organizer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
