package com.ps2.petsfinder.petsfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

public final class ConfigConexao {

    private SQLiteDatabase conexao;
    private BaseInterna base;

    public SQLiteDatabase criarConexao(Context context){
        try{
            base = new BaseInterna(context);
            conexao = base.getWritableDatabase();
            return conexao;
        }catch(SQLiteException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }
        return null;
    }


}
