package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import CRUD.CRUDUsuario;

public class Conexao {
	
	static String URL = "jdbc:mysql://us-cdbr-iron-east-05.cleardb.net/heroku_4b09a6c99bdb348";
	//static String PORTA = "3306";
	//static String URL = "jdbc:mysql://localhost:3306/petsfinder";
	static String USUARIO = "b9449d0146a5ef";
	static String SENHA = "9fd52b4c";
	//static String NOMEBANCO = "heroku_4b09a6c99bdb348";
	static String DRIVER = "com.mysql.jdbc.Driver";	
	
	public Connection getConexao(){
		Connection c = null;
		try {
			Class.forName(DRIVER);
			try {
				c = DriverManager.getConnection(URL,USUARIO,SENHA);
				System.out.println("deu");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Erro ao conectar-se ao banco");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	
}
