package com.ps2.petsfinder.petsfinder;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.ps2.petsfinder.petsfinder.modelFB.LoginUsuario;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Runnable{

    private EditText edEmail;
    private EditText edSenha;
    private Button entrar;
    private TextView criarConta;
    private Thread tarefa;
    private SQLiteDatabase conexao;
    private BaseInterna base;
    private ConfigConexao conSQLite;
    private UsuarioSQLite usuarioSQLite;
    private Usuario user;
    private String retEmail;
    private String retSenha;
    private List<Usuario> teste;
    private Toolbar toolbar;
    private int tam;
    private WSClient client = new WSClient();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference login = databaseReference.child("login");
    private LoginUsuario loginUsuario;
    private List<LoginUsuario> listLoginUsuario = new ArrayList<>();
    private Security geraHash;
    private PFUtil unit = new PFUtil();

    public Intent cadastro;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conSQLite = new ConfigConexao();
        conexao = conSQLite.criarConexao(MainActivity.this);
        usuarioSQLite = new UsuarioSQLite(conexao);

        login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 1; i < dataSnapshot.getChildrenCount() + 1; i++){
                    loginUsuario = new LoginUsuario();
                    loginUsuario.setUsuario(dataSnapshot.child(String.valueOf(i)).child("usuario").getValue().toString());
                    loginUsuario.setHash(dataSnapshot.child(String.valueOf(i)).child("hash").getValue().toString());
                    loginUsuario.setSalt(dataSnapshot.child(String.valueOf(i)).child("salt").getValue().toString());
                    listLoginUsuario.add(loginUsuario);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Erro ao acessar a base de dados", Toast.LENGTH_SHORT).show();
            }
        });

        edEmail = (EditText) findViewById(R.id.edEmail);
        edSenha = (EditText) findViewById(R.id.edSenha);
        entrar = findViewById(R.id.btnEntrar);
        criarConta = (TextView) findViewById(R.id.txtCriarConta);

        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroUsuario();
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validaCampos()){
                    if(comparaSenha(edEmail.getText().toString())){
                        //usuarioSQLite.addToken(edEmail.getText().toString());
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                //tarefa = new Thread(MainActivity.this);
                //tarefa.start();

                //String senhaHash = edSenha.getText().toString();
                //String emailHash = edEmail.getText().toString();
                /*boolean entra = false;
                List<Usuario> user = usuarioSQLite.buscarAll();
                for(int i = 0; i < user.size(); i++){
                    if(user.get(i).getEmail().equals(edEmail.getText().toString()) && edSenha.getText().toString().equals("admin"))
                        entra = true;
                }
                if(entra)
                    abrirCadastroUsuario();*/
                /*if(validaCampos()){
                    try {
                        if(isEqualHash(senhaHash, emailHash)){
                            Toast.makeText(getApplicationContext(), senhaHash, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
    }// FIM ONCREATE

    private boolean isEqualHash(String senha, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        boolean result = false;
        Security sec = new Security();
        String hash = "";
        if (emailValido(email)){
            Usuario user = usuarioSQLite.getUserSQL(email);
            hash = sec.GeraHash(senha, user.getSalt());
            if(hash.equals(user.getHash()))
                result = true;
        }else
            Toast.makeText(getApplicationContext(),"Usuario inválido", Toast.LENGTH_SHORT).show();

        return result;
    }

    //Verifica se o usuario existe
    public boolean emailValido(String email){
        List<Usuario> user = usuarioSQLite.buscarAll();
        for(int i = 0; i < user.size(); i++){
            if(user.get(i).getEmail().equals(email))
                return true;
        }
        return false;
    }

   //Verifica se campo não está vazio
    private boolean validaCampos(){
        boolean result = false;
        int codigoErro = 0;
        String context = "";

        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();

        if(result = isCampoVazio(email)){
            edEmail.requestFocus();
            codigoErro = 1;
        }
        else if(result = isCampoVazio(senha)){
            edSenha.requestFocus();
            codigoErro = 2;
        }

        switch (codigoErro){
            case 1: context = "Campo email não pode ser vazio!";
                break;
            case 2: context = "Campo senha não pode ser vazio!";
                break;
        }

        if (codigoErro != 0) {
            Toast.makeText(getApplicationContext(), context, Toast.LENGTH_SHORT).show();
            result = false;
        }else
            result = true;

        return result;
    }

    public boolean isCampoVazio(String valor){
        boolean result = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return result;
    }

    //Vai para cadastro
    public void abrirCadastroUsuario(){
        Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

        public boolean comparaSenha(String usuario){
        boolean retorno = false;
        String hashGerado = "";
        String hashBanco = "";
        String senha = "";
        geraHash = new Security();
        for(int i = 0; i < listLoginUsuario.size(); i++){
            if(listLoginUsuario.get(i).getUsuario().equals(usuario)){
                hashBanco = listLoginUsuario.get(i).getHash();
                senha = edSenha.getText().toString();
                try {
                    hashGerado = geraHash.GeraHash(senha, listLoginUsuario.get(i).getSalt());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if(hashGerado.equals(hashBanco) && hashGerado != "")
            retorno = true;
        if(!retorno)
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();

        return retorno;
    }

    @Override
    public void run() {
        WSClient client = new WSClient();
        try {
            final boolean resposta = client.testaUsuarioSenha("Guiga", "123");
            h.post(new Runnable() {
                @Override
                public void run() {
                    if(resposta) {
                        cadastro = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                        cadastro.putExtra("usuario", edEmail.getText().toString());
                        startActivity(cadastro);
                    }else
                        Toast.makeText(MainActivity.this, "email ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
