package com.example.rh.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.rh.dto.FuncionariosDTO;
import com.example.rh.entity.Funcionarios;
import com.example.rh.repo.FuncionariosRepository;
import com.example.rh.sql.RhDAO;

@Controller
@RequestMapping("/rh")
public class RhController {

	@Autowired
	RhDAO rhDAO;
	
	@Autowired
	FuncionariosRepository funcionariosRepository;

	@PutMapping(path = "/insereFuncionario", produces = "application/json")
	public ResponseEntity<?> insereFuncionario(@RequestBody Funcionarios funcionarios){

		//List<Funcionarios> func = rhDAO.consultaCpf(funcionarios);
		//FuncionariosDTO funcDTO = rhDAO.consultaCpf(funcionarios);
		
		//System.out.println("cpf requisição: " + funcionarios.getCpf());
		FuncionariosDTO dtoFunc = funcionariosRepository.consultaCpf(funcionarios.getCpf());

		if (dtoFunc != null) {
			return new ResponseEntity<>("CPF já cadastrado!", HttpStatus.BAD_REQUEST);
		} else {
			int retorno = rhDAO.insereFuncionario(funcionarios);
			return new ResponseEntity<>(retorno, HttpStatus.OK);
		}

//		System.out.println("cpf: " + dtoFunc.getCdFuncionario());
//		if (dtoFunc.getCdFuncionario() == null || dtoFunc.getCdFuncionario().equals("")) {
//			int retorno = rhDAO.insereFuncionario(funcionarios);
//			return new ResponseEntity<>(retorno, HttpStatus.OK);			
//		} else {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
	}
	
	@PutMapping(path = "/demiteFuncionario", produces = "application/json")
	public ResponseEntity<?> demiteFuncionario(@RequestBody Funcionarios funcionarios){

		FuncionariosDTO dtoHistFunc = funcionariosRepository.consultaHistorico(funcionarios.getCpf());
		
		if (dtoHistFunc == null) {
			return new ResponseEntity<>("Funcionário não encontrado!", HttpStatus.BAD_REQUEST);
		} else if (dtoHistFunc.getDt_demissao() != null) {
			return new ResponseEntity<>("Funcionário já demitido!", HttpStatus.BAD_REQUEST);
		} else if (dtoHistFunc.getId_hist_sal() == null) {
			@SuppressWarnings("unused")
			int retornoH = rhDAO.insereHistoricoDemissaoFunc(funcionarios);
			int retorno = rhDAO.demiteFuncionario(funcionarios);
			return new ResponseEntity<>(retorno, HttpStatus.OK);
		} else {
			int retorno = rhDAO.demiteFuncionario(funcionarios);
			return new ResponseEntity<>(retorno, HttpStatus.OK);			
		}
	}
	
	@PutMapping(path = "/aumentaSalarioFuncionario", produces = "application/json")
	public ResponseEntity<?> aumentaSalarioFuncionario(@RequestBody Funcionarios funcionarios){

		FuncionariosDTO dtoHistFunc = funcionariosRepository.consultaHistorico(funcionarios.getCpf());
		
		if (dtoHistFunc == null) {
			return new ResponseEntity<>("Funcionário não encontrado!", HttpStatus.BAD_REQUEST);
		} else if (dtoHistFunc.getDt_demissao() != null) {
			return new ResponseEntity<>("Funcionário já demitido!", HttpStatus.BAD_REQUEST);
		} else if ( funcionarios.getAumento() > 20) {
			return new ResponseEntity<>("Resjustes de salários não podem ser acima de 20%!", HttpStatus.BAD_REQUEST);
		} else if ( funcionarios.getAumento() < 0) {
			return new ResponseEntity<>("Salários não podem ser reduzidos!", HttpStatus.BAD_REQUEST);
		} else {
			int retorno = rhDAO.insereHistoricoAumenSalFunc(funcionarios);
			return new ResponseEntity<>(retorno, HttpStatus.OK);
		}
	}
	
	@PutMapping(path = "/aumentaSalarioTodosFuncionarios", produces = "application/json")
	public ResponseEntity<?> aumentaSalarioTodosFuncionarios(){

		List<FuncionariosDTO> funcinariosAtivos = funcionariosRepository.consultaFuncionariosAtivos();
		int i = 0;
		
		for (FuncionariosDTO funcionariosDTO : funcinariosAtivos) {
			System.out.println("Nome: " + funcionariosDTO.getCdFuncionario());
			System.out.println("CPF: " + funcionariosDTO.getCpf());
			int retorno = rhDAO.insereHistoricoAumenSalarioTodosFunc(funcionariosDTO.getCpf(), "5");
			i = i + retorno;
		}
		if (i > 0) {
			return new ResponseEntity<>("Quatidade de funcionários que tiveram reajustes de salário: " + i, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Nenhum funcionário obteve reajuste salarial.", HttpStatus.OK);
		}

	}

	@GetMapping(path = "/historicoSalario", produces = "application/json")
	public ResponseEntity<List<Funcionarios>> historicoSalario(@RequestBody Funcionarios funcionarios){
		return new ResponseEntity<>(rhDAO.historicoSalario(funcionarios), HttpStatus.OK);
	}
	
	@GetMapping(path = "/funcionariosAtivos", produces = "application/json")
	public ResponseEntity<List<Funcionarios>> funcionariosAtivos(){
		return new ResponseEntity<>(rhDAO.funcionariosAtivos(), HttpStatus.OK);
	}

}
