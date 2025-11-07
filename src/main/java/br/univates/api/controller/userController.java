package br.univates.api.controller;

import br.univates.api.dtos.userDTO;
import br.univates.api.model.users;
import br.univates.api.repositories.userRespository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class userController {
    private final br.univates.api.repositories.userRespository userRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private userController(userRespository userRespository) {
        this.userRespository = userRespository;
    }


    @GetMapping
    public ResponseEntity<?> getUserByEmail(@RequestParam String email){
        Optional<users> user = userRespository.findByEmail(email);
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @PostMapping
    public ResponseEntity<users> save(@RequestBody userDTO dto) {
        users usuario = new users();
        BeanUtils.copyProperties(dto, usuario);
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        users savedUser = userRespository.save(usuario);


        // Retorna o status 201 Created e o usuário salvo no corpo da resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    @PutMapping
    public ResponseEntity<users> update(@RequestBody users user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        users savedUser = userRespository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}