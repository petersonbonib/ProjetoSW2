package com.ps2.petsfinder.petsfinder;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps2.petsfinder.petsfinder.modelFB.LoginUsuario;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edEmail;
    private EditText edSenha;
    private EditText edConfSenha;
    private Button btnCriar;
    private LoginUsuario user;
    private UsuarioSQLite usuarioSQLite;
    private Security security;
    private ConfigConexao conSQLite;
    private SQLiteDatabase conexao;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference cadUsuario = databaseReference.child("login");
    public static List<LoginUsuario> listLoginUsuario = new ArrayList<>();
    private LoginUsuario loginUsuario = new LoginUsuario();
    private long childrenCount;
    private List<String> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edEmail = (EditText) findViewById(R.id.txtEmail);
        edSenha = (EditText) findViewById(R.id.txtSenha);
        edConfSenha = (EditText) findViewById(R.id.txtConfirmaSenha);
        btnCriar = (Button) findViewById(R.id.btnCriar);

        cadUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childrenCount = dataSnapshot.getChildrenCount();
                usuarios = new ArrayList<>();
                for(int i = 1; i < dataSnapshot.getChildrenCount() + 1; i++){
                    usuarios.add(dataSnapshot.child(String.valueOf(i)).child("usuario").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*conSQLite = new ConfigConexao();
        conexao = conSQLite.criarConexao(CadastroUsuarioActivity.this);

        usuarioSQLite = new UsuarioSQLite(conexao);*/

        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new LoginUsuario();
                if(existsUsuario(edEmail.getText().toString()))
                    Toast.makeText(CadastroUsuarioActivity.this, "Usuário já existe", Toast.LENGTH_SHORT).show();
                else if(validaCampos()){
                    Map<String, String> mapLogin = new HashMap<String, String>();
                    mapLogin.put("usuario", user.getUsuario());
                    mapLogin.put("hash", user.getHash());
                    mapLogin.put("salt", user.getSalt());
                    cadUsuario.child(String.valueOf(childrenCount + 1)).setValue(mapLogin);
                    Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);

                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Usuário criado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

   /* private void confirmar() {
        if(validaCampos()){
            usuarioSQLite.inserir(user);
        }else
            Toast.makeText(getApplicationContext(), "Erro na confirmação dos dados", Toast.LENGTH_SHORT).show();
    }*/

    private boolean validaCampos(){
        boolean result = false;
        int codigoErro = 0;
        String context = "";

        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();
        String confSenha = edConfSenha.getText().toString();

        if(result = isCampoVazio(email)){
            edEmail.requestFocus();
            codigoErro = 1;
        }
        else if(result = isCampoVazio(senha)){
            edSenha.requestFocus();
            codigoErro = 2;
        }
        else if(result = isCampoVazio(confSenha)){
            edConfSenha.requestFocus();
            codigoErro = 3;
        }
        else if(!edConfSenha.getText().toString().equals(edSenha.getText().toString())){
            edConfSenha.setText("");
            edSenha.setText("");
            edSenha.requestFocus();
            codigoErro = 4;
        }

        switch (codigoErro){
            case 1: context = "Campo email não pode ser vazio!";
                break;
            case 2: context = "Campo senha não pode ser vazio!";
                break;
            case 3: context = "Campo Confirmar senha não pode ser vazio!";
                break;
            case 4: context = "Senha não é igual";
        }

        if (codigoErro != 0) {
            Toast.makeText(getApplicationContext(), context, Toast.LENGTH_SHORT).show();
            result = false;
        }else{
            security = new Security();
            user.setUsuario(email);
            if(edSenha.getText().toString().equals(edConfSenha.getText().toString())){
                String salt = security.GeraSalt();
                String hash = null;
                try {
                    hash = security.GeraHash(senha, salt);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                user.setHash(hash);
                user.setSalt(salt);
                result = true;
            }
        }
        return result;
    }

    public boolean isCampoVazio(String valor){
        boolean result = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return result;
    }


    public boolean existsUsuario(String user){
        for(int i = 0; i < usuarios.size(); i++){
            if(user.equals(usuarios.get(i)))
                return true;
        }
        return false;
    }



}
