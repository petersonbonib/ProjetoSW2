package com.ps2.petsfinder.petsfinder;

public class Scripts {

    public static String getCreateTableUserToken(){
        StringBuilder sql = new StringBuilder();

       /* sql.append("CREATE TABLE IF NOT EXISTS TOKEN ( ");
        sql.append("    VALOR VARCHAR(255) NOT NULL ) ");
        sql.append("    USUARIO VARCHAR(50) NOT NULL DEFAULT(''), ");
        sql.append("    HASH VARCHAR(255) NOT NULL, ");
        sql.append("    SALT VARCHAR(20) NOT NULL ) ");*/

        sql.append("CREATE TABLE IF NOT EXISTS TOKEN ( ");
        //sql.append("    CODIGO INTEGER AUTOINCREMENT, ");
        sql.append("    USUARIO VARCHAR(255) NOT NULL DEFAULT(''), ");
        sql.append("    TIME INTEGER NOT NULL ) ");

        return sql.toString();
    }
}
