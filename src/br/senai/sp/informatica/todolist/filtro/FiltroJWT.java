package br.senai.sp.informatica.todolist.filtro;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.auth0.jwt.JWTVerifier;

import br.senai.sp.informatica.todolist.controller.UsuarioRestController;
@WebFilter("/*")
public class FiltroJWT implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		//Libera acesso ao Login
		if(request.getRequestURI().contains("login")){
			chain.doFilter(req, resp);
			return;
		}
		String token = request.getHeader("Authorization");
		try {
			
			JWTVerifier verifier = new JWTVerifier(UsuarioRestController.SECRET);//descriptografa
			Map<String, Object> claims = verifier.verify(token);
			System.out.println(claims);
			chain.doFilter(req, resp);
		} catch (Exception e) {
			if (token == null){
				response.sendError(HttpStatus.UNAUTHORIZED.value());
			}else{
				response.sendError(HttpStatus.FORBIDDEN.value());
			}
		}
		
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
