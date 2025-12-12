
package br.com.cadastroaluno.dal;

import java.sql.*;
/**
 *
 * @author Dev. ECSousa
 */
public class ModuloConexao {
    
    //metodo responsavel por estabelecer conexao com o banco de dados

    public static Connection conector(){
        java.sql.Connection conexao = null;
        //A linha abaixo chamar o driver que eu importei para bibliotecas
        // String driver = "com.mysql.jdbc.Driver"; esta classe de driver esta absoleto
           String driver = "com.mysql.cj.jdbc.Driver";
        //Armazenando informacoes referente ao banco de dados
        String url = "jdbc:mysql://localhost:3306/db_escola?characterEncoding=utf-8";
        String user = "dbaes";
        String password = "admin1234";
        //Estabelecendo a conexao com BD
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e){
            //Alinha abaixo serve para esclarece o erro
            // System.out.println("e");
            return null;
        }
    }
    
}
