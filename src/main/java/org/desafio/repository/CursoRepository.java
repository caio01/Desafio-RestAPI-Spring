package org.desafio.repository;

import org.desafio.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query(value = "select count(*) from ALUNO_HAS_CURSO where CURSO_ID = ?1", nativeQuery = true)
    Integer getNumMatriculas(Long idCurso);
}