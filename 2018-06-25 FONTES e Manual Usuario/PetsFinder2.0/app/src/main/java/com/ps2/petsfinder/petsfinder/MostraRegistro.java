package com.ps2.petsfinder.petsfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MostraRegistro extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView img;
    private TextView animal;
    private TextView nome;
    private TextView raca;
    private TextView cor;
    private TextView coleira;
    private TextView dono;
    private TextView telefone;
    private Button btnLigar;
    private Button btnEditar;
    private TextView encontrado;
    private ImageView imgPet;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference bdRegAnimal = databaseReference.child("registroAnimal");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://petsfinder-d9b91.appspot.com");
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_registro);

        //Menu
        PrimaryDrawerItem itemOpenMap = new PrimaryDrawerItem().withIdentifier(0).withName("Abrir mapa").withIcon(R.drawable.maps);
        @SuppressLint("ResourceType")
        PrimaryDrawerItem itemLostPets = new PrimaryDrawerItem().withIdentifier(1).withName("Animais perdidos").withIcon(R.drawable.saddog);

        @SuppressLint("ResourceType")
        SecondaryDrawerItem itemNewPet = new SecondaryDrawerItem().withIdentifier(2).withName("Novo registro").withIcon(R.drawable.new_pet);
        PrimaryDrawerItem itemSair = new PrimaryDrawerItem().withIdentifier(4).withName("Sair").withIcon(R.drawable.exit);

        toolbar = (Toolbar) findViewById(R.id.tb_registro);
        toolbar.setTitle("Animal perdido");
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

                        switch (position) {
                            case 0:
                                startActivity(new Intent(MostraRegistro.this, MapsActivity.class));
                                break;
                            case 1:
                                break;
                            case 2:
                                Toast.makeText(MostraRegistro.this, "Novo registro", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                startActivity(new Intent(MostraRegistro.this, MainActivity.class));
                        }

                        return false;
                    }
                })
                .build();
        // FIM Menu

        // ************ Inicialização de componentes

        animal = (TextView) findViewById(R.id.idAnimal);
        nome = (TextView) findViewById(R.id.idNome);
        raca = (TextView) findViewById(R.id.idRaca);
        cor = (TextView) findViewById(R.id.idCor);
        coleira = (TextView) findViewById(R.id.idColeira);
        dono = (TextView) findViewById(R.id.idDono);
        telefone = (TextView) findViewById(R.id.idTelefone);
        btnEditar = (Button) findViewById(R.id.idEditar);
        btnLigar = (Button) findViewById(R.id.idLigar);
        encontrado = (TextView) findViewById(R.id.tvEncontrado);
        imgPet = (ImageView) findViewById(R.id.imgPet);

        // ************ Fim Inicialização de componentes

        final RegistroAnimal regAnimal = (RegistroAnimal) getIntent().getSerializableExtra("regAnimal");

        animal.setText("Animal: " + regAnimal.getAnimal());
        nome.setText("Nome: " + regAnimal.getNome());
        raca.setText("Raça: " + regAnimal.getRaca());
        cor.setText("Cor: " + regAnimal.getCor());
        coleira.setText("Coleira: " + regAnimal.getColeira());
        dono.setText("Dono: " + regAnimal.getDono());
        telefone.setText("Fone: " + regAnimal.getTelefone());

        String nomeImg = regAnimal.getId() + "_" + regAnimal.getUsuario();

        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.child(nomeImg).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgPet.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        if (regAnimal.getIsYou().equals("TRUE")) {
            encontrado.setText("Seu animal já foi encontrado?");
            btnLigar.setText("Remover publicação");
            btnEditar.setText("Editar publicação");
            btnLigar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MostraRegistro.this);
                    builder.setTitle("Confirmação");
                    builder.setMessage("Deseja realmente excluir esta publicação?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            RegistroAnimal regLatLgn = new RegistroAnimal();
                            regLatLgn.setLatitude("");
                            regLatLgn.setLongitude("");
                            Map<String, String> mapLatLng = new HashMap<String, String>();
                            mapLatLng.put("latitude", regLatLgn.getLatitude());
                            mapLatLng.put("longitude", regLatLgn.getLongitude());
                            bdRegAnimal.child(regAnimal.getId()).setValue(mapLatLng);
                            Toast.makeText(MostraRegistro.this, "Publicação excuida", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    alerta = builder.create();
                    alerta.show();

                }
            });
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MostraRegistro.this, RegistroAnimalActivity.class);
                    regAnimal.setTelaMostraRegistro("TRUE");
                    regAnimal.setLatitude(regAnimal.getLatitude());
                    regAnimal.setLongitude(regAnimal.getLongitude());
                    regAnimal.setId(regAnimal.getId());
                    regAnimal.setUsuario(regAnimal.getUsuario());
                    intent.putExtra("mostraReg", regAnimal);
                    startActivity(intent);
                }
            });
        } else {
            btnEditar.setVisibility(View.INVISIBLE);
            encontrado.setText("Você encontrou este animal?");
            btnLigar.setText("Ligar");
            btnLigar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fone = telefone.getText().toString();
                    Uri uri = Uri.parse("tel:" + fone);
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);

                    if (ActivityCompat.checkSelfPermission(MostraRegistro.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MostraRegistro.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return;
                    }
                    startActivity(intent);
                }
            });

        }

    }// onCreate

    private void removeConfirmation() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirmação");
        //define a mensagem
        builder.setMessage("Deseja realmente excluir esta publicação?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MostraRegistro.this, "Publicação excuida", Toast.LENGTH_SHORT).show();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MostraRegistro.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
