package br.senai.sp.informatica.todolist.DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.informatica.todolist.modelo.Usuario;

@Repository
public class UsuarioDao {

	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void inserir(Usuario usr){
		manager.persist(usr);
	}
	
	
	public Usuario logar(Usuario usr) {
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.login = :login "
							+"and u.senha = :senha",Usuario.class);
		query.setParameter("login", usr.getLogin());
		query.setParameter("senha", usr.getSenha());
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
