package org.desafio.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Curso implements Comparable<Curso> {

	@Id
	@GeneratedValue
	private Long id;
	private String titulo;
	private String descricao;
	private String duracao;
	private String conteudo;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="avaliacao_id")
	private List<Avaliacao> avaliacoes;

	@Override
	public int compareTo(Curso outroCurso) {
		if(this.mediaAvaliacoes() > outroCurso.mediaAvaliacoes()) {
			return -1;
		} else if (this.mediaAvaliacoes() < outroCurso.mediaAvaliacoes()) {
			return 1;
		} else {
			return 0;
		}
	}

	public double mediaAvaliacoes () {
		double media = 0;
		double total = 0;
		int qtd = 0;
		for(Avaliacao a : this.avaliacoes) {
			qtd++;
			total += a.getNota();
		}
		if(qtd > 0) media = total/qtd;
		return media;
	}
}

