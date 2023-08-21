package org.desafio.controller;

import java.util.List;
import java.util.Optional;

import org.desafio.model.Instrutor;
import org.desafio.repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instrutor")
public class InstrutorController {
    @Autowired
    InstrutorRepository _instrutorRepository;

    @GetMapping
    public List<Instrutor> list() {
        return _instrutorRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Optional<Instrutor> listByID(@PathVariable Long id) {
        return _instrutorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instrutor add(@RequestBody Instrutor instrutor) {
        return _instrutorRepository.save(instrutor);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Instrutor put(@RequestBody Instrutor instrutor, @PathVariable Long id) {
        if(_instrutorRepository.existsById(id)) {
            Instrutor i = new Instrutor();
            i.setId(id);
            i.setNome(instrutor.getNome());
            i.setCpf(instrutor.getCpf());
            i.setEmail(instrutor.getEmail());
            i.setBiografia(instrutor.getBiografia());

            return _instrutorRepository.save(i);
        }
        return null;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String delete(@PathVariable Long id) {
        _instrutorRepository.deleteById(id);
        return "Deleted";
    }

}