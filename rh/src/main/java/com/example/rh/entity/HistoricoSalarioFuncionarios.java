package com.example.rh.entity;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hist_sal_funcionarios")
public class HistoricoSalarioFuncionarios {

	@Id
	@Column(name = "id_hist_sal", unique = true, nullable = false)
	private Long id_hist_sal;
	
	@Column(name = "cdfuncionario")
	private Long cdfuncionario;
	
	@Column(name = "datahora_alteracao")
	private LocalDateTime datahora_alteracao;

	@Column(name = "salario")
	private DecimalFormat salario;
}
