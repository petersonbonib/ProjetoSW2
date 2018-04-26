package webservice;

import CRUD.CRUDUsuario;

public class WebService {

	CRUDUsuario CRUD = new CRUDUsuario();
	
	public boolean testaUsuarioSenha(String usuario, String senha){
		boolean resposta = false;
		resposta = CRUD.testaUsuarioSenha(usuario, senha);		
		return resposta;
	}
	
	public boolean novoUsuarioSenha(String usuario, String senha, int nivel){
		boolean resposta = false;
		resposta = CRUD.novoUsuarioSenha(usuario, senha, nivel);
		return resposta;
	}
	
	public boolean editarUsuarioSenha(String usuario, String senha, int nivel, int idUsuario){
		boolean resposta = false;
		resposta = CRUD.editarUsuarioSenha(usuario, senha, nivel, idUsuario);
		return resposta;
	}
	
	public boolean removeUsuarioSenha(int idUsuario){
		boolean resposta = false;
		resposta = CRUD.removeUsuarioSenha(idUsuario);
		return resposta;
	}	
	
	public String selecionaUsuarioSenhas(){
		String resposta = null;
		resposta = CRUD.selecionaUsuarioSenhas();		
		return resposta;
	}
}
