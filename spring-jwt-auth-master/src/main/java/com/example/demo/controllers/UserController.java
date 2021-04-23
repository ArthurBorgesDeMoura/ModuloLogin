package com.example.demo.controllers;

import com.example.demo.JWTSecurity.JWTService;
import com.example.demo.dtos.CredenciaisDTO;
import com.example.demo.dtos.TokenDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.LoginResponse;
import com.example.demo.entities.User;
import com.example.demo.exceptions.SenhaInvalidaException;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.providers.TokenProvider;
import com.example.demo.services.MyUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

//FIXME: #### quando tudo já estiver ok, e o usuário já criado, remover o mapeamento new-user ####

@Controller
public class UserController {

    @Autowired
    MyUserDetailService usuarioService;
    private JWTService jwtService;
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO user) {
        User usuario = new User();
        usuario.setUsername(user.username);
        usuario.setPassword(user.password);
        try {
            usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (UserAlreadyExistsException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message);
        }
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            User user = User.builder()
                    .username(credenciais.getLogin()).password(credenciais.getSenha()).build();
           UserDetails usuarioAutenticado = usuarioService.autenticar(user);
           String token = jwtService.geraToken(user);
           return new TokenDTO(user.getUsername(), token);
        }catch(UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}