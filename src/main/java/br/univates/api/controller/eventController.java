package br.univates.api.controller;

import br.univates.api.dtos.eventInscriptionDTO;
import br.univates.api.model.Events;
import br.univates.api.repositories.eventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class eventController {

    private final br.univates.api.repositories.eventRepository eventRepository;

    public eventController(eventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Events>> get() {
        List<Events> lista = eventRepository.findAll();
        return ResponseEntity.status(200).body(lista);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") int id) {
        if(eventRepository.existsById(id)) {
            return ResponseEntity.status(200).body(eventRepository.findById(id).get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Id não encontrado");
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getByUser(@PathVariable(value = "id") int id) {
        List<eventInscriptionDTO> lista = eventRepository.findAllByUserId(id);
        if(!lista.isEmpty()) {
            return ResponseEntity.status(200).body(lista);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Id não encontrado");
    }

}
