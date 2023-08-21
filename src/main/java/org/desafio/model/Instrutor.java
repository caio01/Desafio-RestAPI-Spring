package org.desafio.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Instrutor {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String biografia;
    @ManyToMany
    @JoinTable(name="instrutor_has_curso",
            joinColumns= {@JoinColumn(name="instrutor_id")},
            inverseJoinColumns= {@JoinColumn(name="curso_id")})
    private List<Curso> cursosList;
}