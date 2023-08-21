package org.desafio.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Avaliacao {
    @Id
    @GeneratedValue
    private Long id;
    private Long id_aluno;
    private Double nota;
    private String feedback;
}
