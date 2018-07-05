package com.ps2.petsfinder.petsfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class RegistroAnimalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PFUtil util = new PFUtil();
    private int selectedColor;
    CorAnimal corAnimal;
    private RegistroAnimal registroAnimal;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference registroAnimalBase = databaseReference.child("registroAnimal");

    private EditText editAnimal;
    private EditText editNome;
    private EditText editRaca;
    private Spinner corPelo;
    private EditText editPulseira;
    private EditText editDono;
    private EditText editTelefone;
    private ImageView img;
    private Button btnLoadImg;
    private Button btnConcluir;

    private String lat = "";
    private String lng = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_animal);

        editAnimal = (EditText) findViewById(R.id.edAnimal);
        editNome = (EditText) findViewById(R.id.edNome);
        editRaca = (EditText) findViewById(R.id.edRaca);
        corPelo = (Spinner) findViewById(R.id.spinner);
        editPulseira = (EditText) findViewById(R.id.edColeira);
        editDono = (EditText) findViewById(R.id.edDono);
        editTelefone = (EditText) findViewById(R.id.edTelefone);
        img = (ImageView) findViewById(R.id.imgAnimal);
        btnLoadImg = (Button) findViewById(R.id.btnImagem);
        btnConcluir = (Button) findViewById(R.id.btnConcluir);

        PrimaryDrawerItem itemOpenMap = new PrimaryDrawerItem().withIdentifier(0).withName("Abrir mapa").withIcon(R.drawable.maps);
        @SuppressLint("ResourceType")
        PrimaryDrawerItem itemLostPets = new PrimaryDrawerItem().withIdentifier(1).withName("Animais perdidos").withIcon(R.drawable.saddog);

        @SuppressLint("ResourceType")
        SecondaryDrawerItem itemNewPet = new SecondaryDrawerItem().withIdentifier(2).withName("Novo registro").withIcon(R.drawable.new_pet);
        PrimaryDrawerItem itemSair = new PrimaryDrawerItem().withIdentifier(4).withName("Sair").withIcon(R.drawable.exit);

        toolbar = (Toolbar) findViewById(R.id.tb_registro);
        toolbar.setTitle("Registro de animais");
        setSupportActionBar(toolbar);

        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSelectedItem(0)
                .addDrawerItems(
                        itemOpenMap,
                        itemLostPets,
                        itemNewPet,
                        new DividerDrawerItem(),
                        itemSair
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }

                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position){
                            case 0: startActivity(new Intent(RegistroAnimalActivity.this, MapsActivity.class));
                                break;
                            case 1:
                                break;
                            case 2: Toast.makeText(RegistroAnimalActivity.this, "Novo registro", Toast.LENGTH_SHORT).show();
                                break;
                            case 4: startActivity(new Intent(RegistroAnimalActivity.this, MainActivity.class));
                        }

                        return false;
                    }
                })
                .build();

        // Obtém  o array de cores.
        String[] colors = getResources().getStringArray(R.array.colors);

        //Cria o adapter.
        final ColorsAdapter adapter = new ColorsAdapter(this, R.layout.spinner_row, colors);

        //Obtém a referência ao Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        //Atribui o adapter.
        mySpinner.setAdapter(adapter);

        //Listener a ser chamado quando uma cor for seleccionada no Spinner
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Obtém e guarda a cor selecionada
                selectedColor = adapter.getColor(position);

                switch (position){
                    case 0 : corAnimal = CorAnimal.VERMELHO;
                        break;
                    case 1 : corAnimal = CorAnimal.ROSA;
                        break;
                    case 2 : corAnimal = CorAnimal.ROXO;
                        break;
                    case 3 : corAnimal = CorAnimal.AZUL;
                        break;
                    case 4 : corAnimal = CorAnimal.CIANO;
                        break;
                    case 5 : corAnimal = CorAnimal.VERDE;
                        break;
                    case 6 : corAnimal = CorAnimal.VERDEAMARELADO;
                        break;
                    case 7 : corAnimal = CorAnimal.AMARELO;
                        break;
                    case 8 : corAnimal = CorAnimal.LARANJA;
                        break;
                    case 9 : corAnimal = CorAnimal.MARRON;
                        break;
                    case 10 : corAnimal = CorAnimal.CINZA;
                        break;
                    case 11 : corAnimal = CorAnimal.PRETO;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroAnimal = new RegistroAnimal();
                adicionaRegistro();

                registroAnimalBase.child(PFUtil.nomeUsuario()).child("animal").setValue(registroAnimal.getAnimal());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("nome").setValue(registroAnimal.getNome());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("raca").setValue(registroAnimal.getRaca());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("cor").setValue(String.valueOf(registroAnimal.getCor().getCor()));
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("coleira").setValue(registroAnimal.getColeira());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("dono").setValue(registroAnimal.getDono());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("telefone").setValue(registroAnimal.getTelefone());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("latitude").setValue(registroAnimal.getLatitude());
                registroAnimalBase.child(PFUtil.nomeUsuario()).child("longitude").setValue(registroAnimal.getLongitude());
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lat = bundle.getString("lat");
        lng = bundle.getString("lng");

    }

    public void adicionaRegistro(){
        registroAnimal.setAnimal(editAnimal.getText().toString());
        registroAnimal.setNome(editNome.getText().toString());
        registroAnimal.setRaca(editRaca.getText().toString());
        registroAnimal.setCor(corAnimal);
        registroAnimal.setColeira(editPulseira.getText().toString());
        registroAnimal.setDono(editDono.getText().toString());
        registroAnimal.setTelefone(editTelefone.getText().toString());
        registroAnimal.setLatitude(lat);
        registroAnimal.setLongitude(lng);
    }
}
