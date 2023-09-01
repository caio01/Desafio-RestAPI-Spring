package org.desafio.controller;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.models.auth.In;
import org.desafio.model.Aluno;
import org.desafio.model.Avaliacao;
import org.desafio.model.Instrutor;
import org.desafio.repository.AlunoRepository;
import org.desafio.repository.CursoRepository;
import org.desafio.repository.InstrutorRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
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

import org.desafio.model.Curso;

@RestController
@RequestMapping("/curso")
public class CursoController {
	@Autowired
	CursoRepository _cursoRepository;

	@Autowired
	AlunoRepository _alunoRepository;

	@Autowired
	InstrutorRepository _instrutorRepository;

	@GetMapping
	public List<Curso> list() {
		return _cursoRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity listByID(@PathVariable Long id) {
		if(_cursoRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(_cursoRepository.getById(id));
		} else {
			Map<String, String> body = new HashMap<>();
			body.put("message","Id informado não existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Curso add(@RequestBody Curso curso) {
		return _cursoRepository.save(curso);
	}

	@PutMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity put(@RequestBody Curso curso, @PathVariable Long id) {
		if(_cursoRepository.existsById(id)) {
			Curso c = new Curso();
			c.setId(id);
			c.setTitulo(curso.getTitulo());
			c.setDescricao(curso.getDescricao());
			c.setDuracao(curso.getDuracao());
			c.setConteudo(curso.getConteudo());

			return ResponseEntity.status(HttpStatus.CREATED).body( _cursoRepository.save(c));
		} else {
		Map<String, String> body = new HashMap<>();
		body.put("message", "Id informado não existe");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		}
	}

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity delete(@PathVariable Long id) {
		if(_cursoRepository.existsById(id)) {
			_cursoRepository.deleteById(id);
			Map<String, String> body = new HashMap<>();
			body.put("message","Deletado com sucesso");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		} else {
			Map<String, String> body = new HashMap<>();
			body.put("message","Id informado não existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		}
	}

	@PostMapping(path = "/avaliacao")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity avaliacao(@RequestBody Map<?, ?> map) {
		if (map.containsKey("idAluno") && map.containsKey("idCurso") && map.containsKey("nota")) {
			Long idAluno = Long.valueOf((String.valueOf(map.get("idAluno"))));
			Long idCurso = Long.valueOf((String.valueOf(map.get("idCurso"))));
			Double nota = (Double) map.get("nota");
			String feedback = null;

			if (map.containsKey("feedback")) {
				feedback = (String) map.get("feedback");
			}

			if (_alunoRepository.existsById(idAluno) && _cursoRepository.existsById(idCurso)) {
				Aluno a = _alunoRepository.getById(idAluno);
				Curso c = _cursoRepository.getById(idCurso);

				List<Curso> cursosMat = a.getCursosMat();
				if (!cursosMat.contains(c)) {
					Map<String, String> body = new HashMap<>();
					body.put("message", "Aluno não matriculado no curso informado");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
				}
				Avaliacao av = new Avaliacao();
				av.setId_aluno(a.getId());
				av.setNota(nota);
				av.setFeedback(feedback);

				List<Avaliacao> listAv = c.getAvaliacoes();
				listAv.add(av);

				c.setAvaliacoes(listAv);
				_cursoRepository.save(c);

				Map<String, String> body = new HashMap<>();
				body.put("message", "Feedback realizado com sucesso");
				return ResponseEntity.status(HttpStatus.CREATED).body(body);
			} else {
				Map<String, String> body = new HashMap<>();
				body.put("message", "Aluno e/ou Curso informado não existe");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
			}
		} else {
			Map<String, String> body = new HashMap<>();
			body.put("message","Favor informe o ID do Aluno, o ID Curso e uma nota para a Avaliacao");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		}
	}

	@GetMapping(path = "/pesquisa/{pesquisa}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity pesquisa(@PathVariable String pesquisa) {
		List<Curso> cursos = _cursoRepository.findAll();
		List<Curso> cursosPesquisa = new ArrayList<>();
		for(Curso c : cursos) {
			if (c.getTitulo().toUpperCase().contains(pesquisa.toUpperCase()) ||
				c.getDescricao().toUpperCase().contains(pesquisa.toUpperCase())) {
				cursosPesquisa.add(c);
			}
		}

		List<Instrutor> instrutores = _instrutorRepository.findAll();
		List<Instrutor> instrutoresPesquisa = new ArrayList<>();
		for(Instrutor i : instrutores) {
			if (i.getNome().toUpperCase().contains(pesquisa.toUpperCase())) {
				for(Curso c : i.getCursosList()) {
					cursosPesquisa.add(c);
				}
			}
		}
		if (!cursosPesquisa.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(cursosPesquisa);
		} else {
			Map<String, String> body = new HashMap<>();
			body.put("message", "Nenhum curso encontrado");
			return ResponseEntity.status(HttpStatus.CREATED).body(body);
		}
	}

	@GetMapping(path = "/melhorAvaliacao")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity melhorAvaliacao() {
		List<Curso> cursos = _cursoRepository.findAll();
		Collections.sort(cursos);
		if(cursos.size() > 5) {
			List<Curso> cursosMaisPopulares = new ArrayList<>();
			for(int i = 0; i < 5; i++) {
				cursosMaisPopulares.add(cursos.get(i));
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(cursosMaisPopulares);
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body(cursos);
		}
	}

	@GetMapping(path = "/popularMatricula")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity popularMatricula() {
		List<Curso> cursos = _cursoRepository.findAll();
		Map<Long, Integer> numMatriculas = new HashMap<>();

		for(Curso c : cursos) {
			numMatriculas.put(c.getId(), _cursoRepository.getNumMatriculas(c.getId()));
		}

		//ordena o map em ordem decrescente
		Map<Long, Integer> numMatriculasOrd = numMatriculas.entrySet()
			.stream()
			.sorted((Map.Entry.<Long, Integer>comparingByValue().reversed()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		//pega apenas o id dos cursos e os adiciona em uma lista
		ArrayList<Curso> cursosOrd = new ArrayList<>();
		numMatriculasOrd.forEach((key, value) -> cursosOrd.add(_cursoRepository.getById(key)));

		//pega apenas o 5 primeiros valores da lista
		return ResponseEntity.status(HttpStatus.CREATED).body(cursosOrd.stream().limit(5).collect(Collectors.toList()));
	}
}