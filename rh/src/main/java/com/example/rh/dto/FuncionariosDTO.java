package com.example.rh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FuncionariosDTO {

	public Long getCdFuncionario();
	
	public String getNome();
	
	public LocalDateTime getDtContratacao();
	
	public String getCpf();
	
	public Float getSalario();
	
	public LocalDate getDt_demissao();
	
	public Long getId_hist_sal();
}
