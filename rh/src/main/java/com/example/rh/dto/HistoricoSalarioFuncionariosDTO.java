package com.example.rh.dto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public interface HistoricoSalarioFuncionariosDTO {

	public Long getId_hist_sal();
	
	public Long getCdfuncionario();
	
	public LocalDateTime getDatahora_alteracao();

	public DecimalFormat getSalario();
}
