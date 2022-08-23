package com.example.rh.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionarios {
	
	@Id
	@Column(name = "cdFuncionario", unique = true, nullable = false)
	private Long cdFuncionario;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "dtContratacao")
	private LocalDateTime dtContratacao;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "salario")
	private Float salario;
	
	@Column(name = "dt_demissao")
	private LocalDateTime dt_demissao;

	public Object getNome() {
		// TODO Auto-generated method stub
		return nome;
	}
	
	public Object getCdFuncionario() {
		// TODO Auto-generated method stub
		return cdFuncionario;
	}

	public Object getDtContratacao() {
		// TODO Auto-generated method stub
		return dtContratacao;
	}

	public String getCpf() {
		// TODO Auto-generated method stub
		return cpf;
	}

	public Float getSalario() {
		// TODO Auto-generated method stub
		return salario;
	}

	public Object getDt_demissao() {
		// TODO Auto-generated method stub
		return dt_demissao;
	}

	public Long aumento;

	public Long getAumento() {
		return aumento;
	}
	
}
