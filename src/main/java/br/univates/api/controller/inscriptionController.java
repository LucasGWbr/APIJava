package br.univates.api.controller;

import br.univates.api.dtos.inscriptionDTO;
import br.univates.api.model.Events;
import br.univates.api.model.inscription;
import br.univates.api.model.users;
import br.univates.api.repositories.eventRepository;
import br.univates.api.repositories.inscriptionRepository;
import br.univates.api.repositories.userRespository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inscription")
public class inscriptionController {

    private final br.univates.api.repositories.inscriptionRepository inscriptionRepository;
    private final br.univates.api.repositories.userRespository userRespository;
    private final br.univates.api.repositories.eventRepository eventRepository;

    public inscriptionController(inscriptionRepository inscriptionRepository, userRespository userRespository, eventRepository eventRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.userRespository = userRespository;
        this.eventRepository = eventRepository;
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody inscriptionDTO dto) {
        inscription inscription = new inscription();
        BeanUtils.copyProperties(dto, inscription);
        Optional<users> user = userRespository.findById(dto.user());
        Optional<Events> event = eventRepository.findById(dto.event());
        if(user.isPresent() && event.isPresent()) {
            inscription.setUserId(user.get().getId());
            inscription.setEventId(event.get().getEventId());
            if(!inscriptionRepository.existsByEventIdAndUserId(inscription.getEventId(),inscription.getUserId())){
                inscription temp = inscriptionRepository.save(inscription);
                return ResponseEntity.status(HttpStatus.CREATED).body(temp);
            }else{
                inscription = inscriptionRepository.findByEventIdAndUserId(inscription.getEventId(),inscription.getUserId());
                inscription.setStatus("ACTIVE");
                return ResponseEntity.status(200).body(inscriptionRepository.save(inscription));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") int id){
        Optional<inscription> inscription = inscriptionRepository.findById(id);
        if(inscription.isPresent()) {
            inscriptionDTO dto = new inscriptionDTO(inscription.get().getUserId(),inscription.get().getEventId(),inscription.get().getStatus());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<inscription>> getAllPeopleInscription(@PathVariable(value = "id") int id){
        List<inscription> inscriptions = inscriptionRepository.findAllByuserId(id);
        if(!inscriptions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(inscriptions);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PatchMapping("/{id}") // Usando PATCH para atualização parcial
    public ResponseEntity<?> updateStatus(
            @PathVariable int id,
            @RequestBody inscriptionDTO dto) {

        Optional<inscription> optionalInscription = inscriptionRepository.findById(id);

        if (optionalInscription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        inscription inscription = optionalInscription.get();

        inscription.setStatus(dto.status());

        inscription updatedInscription = inscriptionRepository.save(inscription);

        return ResponseEntity.ok(updatedInscription);
    }
}
