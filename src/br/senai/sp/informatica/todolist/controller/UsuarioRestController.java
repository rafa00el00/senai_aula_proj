package br.senai.sp.informatica.todolist.controller;

import java.net.URI;
import java.util.HashMap;

import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import br.senai.sp.informatica.todolist.DAO.UsuarioDao;
import br.senai.sp.informatica.todolist.modelo.Usuario;

@RestController
public class UsuarioRestController {

	public static final String SECRET = "todolistum";
	public static final String ISSUER = "http://www.sp.senai.br";

	@Autowired
	private UsuarioDao dao;

	@RequestMapping(value = "/Usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> inserir(@RequestBody Usuario usr) {
		try {
			dao.inserir(usr);
			URI location = new URI("/Usuario/" + usr.getId());
			return ResponseEntity.created(location).body(usr);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.badRequest().body(usr);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST
			, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> logar(@RequestBody Usuario usr) {
		try {
			usr = dao.logar(usr);
			if (usr != null) {
				long iat = System.currentTimeMillis() / 1000;
				long exp = iat + 60;
				JWTSigner signer = new JWTSigner(SECRET);
				HashMap<String, Object> claims = new HashMap<>();
				claims.put("iss", ISSUER);
				claims.put("exp", exp);
				claims.put("iat", iat);
				claims.put("id_usuario", usr.getId());
				// Gerar tolken
				String jwt = signer.sign(claims);
				JSONObject token = new JSONObject();
				token.put("token", jwt);
				return ResponseEntity.ok(token.toString());
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
