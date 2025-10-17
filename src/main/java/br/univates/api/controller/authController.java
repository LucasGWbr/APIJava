package br.univates.api.controller;

import br.univates.api.model.users;
import br.univates.api.repositories.userRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class authController {
    private final br.univates.api.repositories.userRespository userRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private authController(userRespository userRespository) {
        this.userRespository = userRespository;
    }

    @PostMapping
    public ResponseEntity auth(@RequestBody users usersDTO){
        Optional<users> user = userRespository.findByEmail(usersDTO.getEmail());
        if(user.isPresent()){
            if (passwordEncoder.matches(usersDTO.getPassword(), user.get().getPassword())) {
                user.get().setPassword("");
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
    }
}
