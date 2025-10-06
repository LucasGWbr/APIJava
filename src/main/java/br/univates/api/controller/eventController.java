package br.univates.api.controller;

import br.univates.api.model.Events;
import br.univates.api.repositories.eventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
