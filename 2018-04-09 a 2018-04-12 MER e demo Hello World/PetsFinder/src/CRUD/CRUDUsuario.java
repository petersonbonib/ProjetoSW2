package CRUD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import banco.Conexao;

public class CRUDUsuario {
	
	Conexao c = new Conexao();
	
	public boolean testaUsuarioSenha(String usuario, String senha){
		boolean resposta = false;
		ResultSet dados = null;
		String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";
		try {
			PreparedStatement stmt = c.getConexao().prepareStatement(sql);
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
			dados = stmt.executeQuery();
			stmt.execute();
			stmt.close();
			
			if(dados.next())
				resposta = true;
			else
				resposta = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resposta;		
	}
	
	public boolean novoUsuarioSenha(String usuario, String senha, int nivel){
		boolean resposta = false;
		String sql = "INSERT INTO usuarios (usuario, senha, nivel) VALUES (?,?,?)";
		try {
			PreparedStatement stmt = c.getConexao().prepareStatement(sql);
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
			stmt.setInt(3, nivel);
			stmt.execute();
			stmt.close();
			resposta = true;
		} catch (SQLException e) {
			e.printStackTrace();
			resposta = false;
		}	
		return resposta;
	}
	
	public boolean editarUsuarioSenha(String usuario, String senha, int nivel, int idUsuario){
		boolean resposta = false;
		String sql = "UPDATE usuarios SET usuario=?, senha=?, nivel=? WHERE idusuarios=?";
		try {
			PreparedStatement stmt = c.getConexao().prepareStatement(sql);
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
			stmt.setInt(3, nivel);
			stmt.setInt(4, idUsuario);
			stmt.execute();
			stmt.close();
			resposta = true;
		} catch (SQLException e) {
			e.printStackTrace();
			resposta = false;
		}
		
		return resposta;
	}
	
	public boolean removeUsuarioSenha(int idUsuario){
		boolean resposta = false;
		String sql = "DELETE FROM usuarios WHERE idusuarios=?";
		try {
			PreparedStatement stmt = c.getConexao().prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			stmt.execute();
			stmt.close();		
			resposta = true;
		} catch (SQLException e) {
			e.printStackTrace();
			resposta = false;
		}	
		return resposta;
	}
	
	public String selecionaUsuarioSenhas(){
		ResultSet dados = null;
		String resposta = null;
		String sql = "SELECT * FROM usuarios";
		try {
			PreparedStatement stmt = c.getConexao().prepareStatement(sql);
			dados = stmt.executeQuery();
			stmt.execute();
			stmt.close();
			int cont = 0;
			while(dados.next()){
				if(cont++ == 0)
					resposta = dados.getString("idusuarios") + ";";
				else
					resposta += dados.getString("idusuarios") + ";";
				
				resposta += dados.getString("usuario") + ";";
				resposta += dados.getString("senha") + ";";
				resposta += dados.getString("nivel") + ":";				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erro ao selecionar os usuários";
		}		
		return resposta;
	}
	
	public static void main(String[] args) {
		CRUDUsuario user = new CRUDUsuario();
		System.out.println(user.selecionaUsuarioSenhas());
	}
	
}
