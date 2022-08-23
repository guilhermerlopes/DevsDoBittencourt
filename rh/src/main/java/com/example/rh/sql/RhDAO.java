package com.example.rh.sql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rh.entity.Funcionarios;

@Repository
public class RhDAO {
	
	@PersistenceContext
	EntityManager entityManager;

	/**
	 * se funcionario existe, se está na ativa e se tem histórico de aumento de salario
	 */
	@SuppressWarnings("unchecked")
	public List<Funcionarios> situacaoFuncionario(Funcionarios funcionarios) {
		Query query = entityManager.createNativeQuery("select func.cdFuncionario cadastro\r\n"
				+ "      ,func.dt_demissao\r\n"
				+ "      ,hist.cdFuncionario historico\r\n"
				+ "  from funcionarios func\r\n"
				+ "  left join hist_sal_funcionarios hist\r\n"
				+ "    on (func.cdFuncionario = hist.cdFuncionario)\r\n"
				+ " where func.cdFuncionario = :p_cdFuncionario");

		query.setParameter("p_cdFuncionario", funcionarios.getCdFuncionario());
		return query.getResultList();
	}

	/**
	 * insere funcionario
	 */
	@Transactional
	public int insereFuncionario(Funcionarios funcionarios) {
		Query query = entityManager.createNativeQuery("insert into funcionarios\r\n"
				+ "(nome, dtContratacao, cpf, salario) values\r\n"
				+ "(:p_nome, :p_dtContratacao, :p_cpf, :p_salario)");
		
		query.setParameter("p_nome", funcionarios.getNome());
		query.setParameter("p_dtContratacao", funcionarios.getDtContratacao());
		query.setParameter("p_cpf", funcionarios.getCpf());
		query.setParameter("p_salario", funcionarios.getSalario());
		return query.executeUpdate();
	}

	/**
	 * demite funcionario
	 */
	@Transactional
	public int demiteFuncionario(Funcionarios funcionarios) {
		Query query = entityManager.createNativeQuery("update funcionarios\r\n"
				+ "   set dt_demissao = :p_dt_demissao\r\n"
				+ " where cpf = :p_cpf");

		query.setParameter("p_dt_demissao", funcionarios.getDt_demissao());
		query.setParameter("p_cpf", funcionarios.getCpf());
		return query.executeUpdate();
	}

	@Transactional
	public int insereHistoricoDemissaoFunc(Funcionarios funcionarios) {
		Query query = entityManager.createNativeQuery("insert into hist_sal_funcionarios (cdFuncionario, datahora_alteracao, salario)\r\n"
				+ "select cdFuncionario, sysdate() as datahora_alteracao, salario\r\n"
				+ "  from funcionarios where cpf = :p_cpf");

		query.setParameter("p_cpf", funcionarios.getCpf());
		return query.executeUpdate();
	}
	
	@Transactional
	public int insereHistoricoAumenSalFunc(Funcionarios funcionarios) {
		Query query = entityManager.createNativeQuery("insert into hist_sal_funcionarios (cdFuncionario, datahora_alteracao, salario)\r\n"
				+ "select func.cdFuncionario\r\n"
				+ "      ,sysdate() as datahora_alteracao\r\n"
				+ "      ,case when\r\n"
				+ "       hist.salario is null then func.salario + ((func.salario/100) * :p_aumento)\r\n"
				+ "       else hist.salario + ((hist.salario/100) * :p_aumento) end as salario\r\n"
				+ "  from funcionarios func\r\n"
				+ "  left join hist_sal_funcionarios hist\r\n"
				+ "    on (func.cdFuncionario = hist.cdFuncionario)\r\n"
				+ " where func.cpf = :p_cpf\r\n"
				+ " order by hist.id_hist_sal desc\r\n"
				+ "       limit 1");

		query.setParameter("p_cpf", funcionarios.getCpf());
		query.setParameter("p_aumento", funcionarios.getAumento());
		return query.executeUpdate();
	}

	@Transactional
	public int insereHistoricoAumenSalarioTodosFunc(String cpf, String aumento) {
		Query query = entityManager.createNativeQuery("insert into hist_sal_funcionarios (cdFuncionario, datahora_alteracao, salario)\r\n"
				+ "select func.cdFuncionario\r\n"
				+ "      ,sysdate() as datahora_alteracao\r\n"
				+ "      ,case when\r\n"
				+ "       hist.salario is null then func.salario + ((func.salario/100) * :p_aumento)\r\n"
				+ "       else hist.salario + ((hist.salario/100) * :p_aumento) end as salario\r\n"
				+ "  from funcionarios func\r\n"
				+ "  left join hist_sal_funcionarios hist\r\n"
				+ "    on (func.cdFuncionario = hist.cdFuncionario)\r\n"
				+ " where func.cpf = :p_cpf\r\n"
				+ " order by hist.id_hist_sal desc\r\n"
				+ "       limit 1");

		query.setParameter("p_cpf", cpf);
		query.setParameter("p_aumento", aumento);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Funcionarios> historicoSalario(Funcionarios funcionarios){

		Query query = entityManager.createNativeQuery("select hist.datahora_alteracao\r\n"
				+ "      ,hist.salario\r\n"
				+ "  from funcionarios func\r\n"
				+ "  left join hist_sal_funcionarios hist\r\n"
				+ "    on (func.cdFuncionario = hist.cdFuncionario)\r\n"
				+ " where func.cpf = :p_cpf\r\n"
				+ " order by hist.datahora_alteracao");
		
		query.setParameter("p_cpf", funcionarios.getCpf());		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Funcionarios> funcionariosAtivos(){

		Query query = entityManager.createNativeQuery("select  cdFuncionario\r\n"
				+ "       ,nome\r\n"
				+ "       ,dtContratacao\r\n"
				+ "       ,cpf\r\n"
				+ "       ,salario\r\n"
				+ "  from funcionarios func\r\n"
				+ " where func.dt_demissao is null\r\n"
				+ " order by func.dtContratacao");

		return query.getResultList();
	}
}
