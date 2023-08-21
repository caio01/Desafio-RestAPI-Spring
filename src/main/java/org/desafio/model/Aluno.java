package org.desafio.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Aluno {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    @ManyToMany
    @JoinTable(name="aluno_has_curso",
            joinColumns= {@JoinColumn(name="aluno_id")},
            inverseJoinColumns= {@JoinColumn(name="curso_id")})
    private List<Curso> cursosMat;
}
