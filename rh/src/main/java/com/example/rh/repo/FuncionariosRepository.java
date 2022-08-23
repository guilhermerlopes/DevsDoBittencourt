package com.example.rh.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.rh.dto.FuncionariosDTO;
import com.example.rh.entity.Funcionarios;

public interface FuncionariosRepository extends JpaRepository<Funcionarios, Integer> {

	@Query(value = "select func.cdFuncionario\r\n"
				 + "  from funcionarios func\r\n"
				 + " where func.cpf = :p_cpf\r\n"
				 + " limit 1", nativeQuery = true)
	FuncionariosDTO consultaCpf(String p_cpf);
	
	@Query(value = "select func.cdFuncionario\r\n"
				 + "      ,func.dt_demissao\r\n"
				 + "      ,case when\r\n"
				 + "       hist.salario is null then func.salario\r\n"
				 + "       else hist.salario end as salario\r\n"
				 + "      ,hist.id_hist_sal\r\n"
				 + "  from funcionarios func\r\n"
				 + "  left join hist_sal_funcionarios hist\r\n"
				 + "    on (func.cdFuncionario = hist.cdFuncionario)\r\n"
				 + " where func.cpf = :p_cpf\r\n"
				 + " order by hist.id_hist_sal desc\r\n"
				 + "       limit 1", nativeQuery = true)
	FuncionariosDTO consultaHistorico(String p_cpf);

	@Query(value = "select func.cdFuncionario\r\n"
				 + "      ,func.cpf\r\n"
				 + "  from funcionarios func\r\n"
				 + " where func.dt_demissao is null\r\n"
				 + " order by func.cdFuncionario", nativeQuery = true)
	List<FuncionariosDTO> consultaFuncionariosAtivos();
	
}
