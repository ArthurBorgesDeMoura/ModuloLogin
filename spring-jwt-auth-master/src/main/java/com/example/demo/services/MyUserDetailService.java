package com.example.demo.services;

import java.util.Optional;

import com.example.demo.MyUserPrincipal;
import com.example.demo.entities.User;
import com.example.demo.exceptions.SenhaInvalidaException;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "usuarioService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepo;    

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails autenticar(User user){
        UserDetails usuario = loadUserByUsername(user.getUsername());
        boolean verificaSenha = bcryptEncoder.matches(user.getPassword(),usuario.getPassword());
        if(verificaSenha){
            return usuario;
        } else{
            throw new SenhaInvalidaException();
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> opt = usuarioRepo.findByUsername(username);
        User user = null;
        if(opt.isPresent()){
            user = opt.get();
        }
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

    public User save(User usuario) throws UserAlreadyExistsException {
        Optional<User> opt = usuarioRepo.findByUsername(usuario.username);
        User user = null;
        if(opt.isPresent()){
            user = opt.get();
            throw new UserAlreadyExistsException(usuario.username);
        }

        usuario.setPassword(bcryptEncoder.encode(usuario.getPassword()));
        return usuarioRepo.save(usuario);
    }
}