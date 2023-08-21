package org.desafio.controller;

import java.util.*;

import org.desafio.model.Aluno;
import org.desafio.model.Curso;
import org.desafio.repository.AlunoRepository;
import org.desafio.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/aluno")
public class AlunoController {
    @Autowired
    AlunoRepository _alunoRepository;

    @Autowired
    CursoRepository _cursoRepository;

    @GetMapping
    public ResponseEntity list() {
        return ResponseEntity.ok(_alunoRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity listByID(@PathVariable Long id) {
        if(_alunoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(_alunoRepository.getById(id));
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("message","Id informado não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(_alunoRepository.save(aluno));
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity put(@RequestBody Aluno aluno, @PathVariable Long id) {
        if(_alunoRepository.existsById(id)) {
            Aluno a = _alunoRepository.getById(id);
            a.setId(id);

            if(aluno.getNome() != null) a.setNome(aluno.getNome());
            if(aluno.getCpf() != null) a.setCpf(aluno.getCpf());
            if(aluno.getEmail() != null) a.setEmail(aluno.getEmail());
            if(aluno.getSenha() != null) a.setSenha(aluno.getSenha());
            if(aluno.getCursosMat() != null) a.setCursosMat(aluno.getCursosMat());

            return ResponseEntity.status(HttpStatus.CREATED).body(_alunoRepository.save(a));
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("message", "Id informado não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity delete(@PathVariable Long id) {
        if(_alunoRepository.existsById(id)) {
            _alunoRepository.deleteById(id);
            Map<String, String> body = new HashMap<>();
            body.put("message","Deletado com sucesso");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("message","Id informado não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @PostMapping(path = "/matricula")
    public ResponseEntity add(@RequestBody Map<String, Long> map) {
        if (map.containsKey("idAluno") && map.containsKey("idCurso")) {
            Long idAluno = map.get("idAluno");
            Long idCurso = map.get("idCurso");
            if (_alunoRepository.existsById(idAluno) && _cursoRepository.existsById(idCurso)) {
                Aluno a = _alunoRepository.getById(idAluno);
                Curso c = _cursoRepository.getById(idCurso);

                List<Curso> cursosMat = a.getCursosMat();
                if (!cursosMat.contains(c)) {
                    cursosMat.add(c);
                } else {
                    Map<String, String> body = new HashMap<>();
                    body.put("message", "Aluno já matriculado no curso informado");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
                }

                a.setCursosMat(cursosMat);
                Aluno b = _alunoRepository.save(a);

                Map<String, String> body = new HashMap<>();
                body.put("message", "Matrícula realizada com sucesso");
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            } else {
                Map<String, String> body = new HashMap<>();
                body.put("message", "Aluno e/ou Curso informado não existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
            }
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("message","Favor informe um ID para o Aluno e um ID pra o Curso");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

    }
}