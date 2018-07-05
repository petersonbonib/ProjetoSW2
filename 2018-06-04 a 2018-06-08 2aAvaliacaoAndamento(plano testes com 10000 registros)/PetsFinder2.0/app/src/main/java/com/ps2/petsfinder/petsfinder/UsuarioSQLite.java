package com.ps2.petsfinder.petsfinder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsuarioSQLite {

    private SQLiteDatabase conexao;

    public UsuarioSQLite(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Usuario user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("USUARIO", user.getEmail());
        contentValues.put("HASH", user.getHash());
        contentValues.put("SALT", user.getSalt());

        conexao.insertOrThrow("TOKEN", null, contentValues);
    }

    public void excluir(int codigo){
        String[] params = new String[1];
        params[0] = String.valueOf(codigo);

        conexao.delete("TOKEN", "CODIGO = ?", params);
    }

    public void alterar(Usuario user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("USUARIO", user.getEmail());
        //contentValues.put("SENHA", user.getSenha());

        String[] params = new String[1];
        params[0] = String.valueOf(user.getCodigo());

        conexao.update("TOKEN", contentValues, "CODIGO = ?", params);
    }

    public List<Usuario> buscarAll(){

        List<Usuario> user = new ArrayList<Usuario>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from TOKEN");

        Cursor result = conexao.rawQuery(sql.toString(), null);
        if(result.getCount() > 0){
            result.moveToFirst();
            do{
                Usuario usuario = new Usuario();
                usuario.setCodigo(result.getInt(result.getColumnIndexOrThrow("CODIGO")));
                usuario.setEmail(result.getString(result.getColumnIndexOrThrow("USUARIO")));
                //usuario.setSenha(result.getString(result.getColumnIndexOrThrow("SENHA")));
                user.add(usuario);
            }while(result.moveToNext());
        }

        return user;
    }

    public Usuario getUserSQL(String usuario){
        Usuario user = new Usuario();
        StringBuilder sql = new StringBuilder();
        sql.append("select USUARIO, HASH, SALT from TOKEN ");
        sql.append("where USUARIO = " + usuario);

        Cursor result = conexao.rawQuery(sql.toString(), null);
        if(result.getCount() > 0) {
            result.moveToFirst();

            user.setEmail(result.getString(result.getColumnIndexOrThrow("USUARIO")));
            user.setHash(result.getString(result.getColumnIndexOrThrow("HASH")));
            user.setSalt(result.getString(result.getColumnIndexOrThrow("SALT")));

            return user;
        }

        return null;
    }

    public void getToken(String usuario) {

        StringBuilder sql = new StringBuilder();
        sql.append("select TIME from TOKENS ");
        sql.append("where USUARIO = " + usuario);

        Cursor result = conexao.rawQuery(sql.toString(), null);
        if (result.getCount() > 0) {
            result.moveToFirst();
        }
    }

    public void addToken(String usuario) {
        long time = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USUARIO", usuario);
        contentValues.put("TIME", time);

        conexao.insertOrThrow("TOKEN", null, contentValues);
    }

    public void alterarToken(String user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("USUARIO", user);

        String[] params = new String[1];
        params[0] = user;

        conexao.update("TOKEN", contentValues, "USUARIO = ?", params);
    }
}
