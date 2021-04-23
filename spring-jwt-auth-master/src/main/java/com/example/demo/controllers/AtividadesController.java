package com.example.demo.controllers;
import com.example.demo.entities.Atividades;
import com.example.demo.repositories.AtividadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/atividades")
public class AtividadesController {

    @Autowired
    private AtividadesRepository atividadesRepository;

    @GetMapping
    public List<Atividades> listar(){
        return atividadesRepository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity findById(@PathVariable long id){
        return atividadesRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Atividades add (@RequestBody Atividades atividades){
        return atividadesRepository.save(atividades);
    }

    @RequestMapping("/id_da_atividade")
    @DeleteMapping
    public void delete (@RequestBody Atividades atividades){
        atividadesRepository.delete(atividades);
    }

    @PutMapping("/id_da_atividade")
    public Atividades change (@RequestBody Atividades atividades){
        atividades.setUltima_atualizacao(Date.from(Instant.now()));
        return atividadesRepository.save(atividades);
    }





}
