package com.ps2.petsfinder.petsfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button meuPet;
    private Button bMapa;
    private Button btnTeste;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference regAnimal = databaseReference.child("registroAnimal");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://petsfinder-d9b91.appspot.com").child("labrador.png");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

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
                            case 0: startActivity(new Intent(HomeMenuActivity.this, MapsActivity.class));
                                finish();
                                break;
                            case 1:
                                break;
                            case 2: Toast.makeText(HomeMenuActivity.this, "Novo registro", Toast.LENGTH_SHORT).show();
                                break;
                            case 4: startActivity(new Intent(HomeMenuActivity.this, MainActivity.class));
                                finish();
                        }

                        return false;
                    }
                })
                .build();
        //End Menu

        meuPet = (Button) findViewById(R.id.btnMyPets);
        bMapa = (Button) findViewById(R.id.btnMaps);
        btnTeste = (Button) findViewById(R.id.button5);
        final Button btnTeste2 = (Button) findViewById(R.id.button4);

        final ImageView mImageView = (ImageView) findViewById(R.id.imageView);

        //download
        btnTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            mImageView.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                } catch (IOException e ) {}
            }
        });

        //upload
        btnTeste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setDrawingCacheEnabled(true);
                mImageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mImageView.layout(0, 0, mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight());
                mImageView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(mImageView.getDrawingCache());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] data = outputStream.toByteArray();

                UploadTask uploadTask = storageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
            }
        });

        final RegistroAnimal animal = new RegistroAnimal();
        final List<RegistroAnimal> listaReg = new ArrayList<>();

        meuPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regAnimal.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(int i = 1; i < dataSnapshot.getChildrenCount(); i++){
                            String usuario = dataSnapshot.child(String.valueOf(i)).child("usuario").getValue().toString();
                            if(PFUtil.getNomeUsuario().equals(usuario)){
                                animal.setAnimal(dataSnapshot.child(String.valueOf(i)).child("animal").getValue().toString());
                                animal.setNome(dataSnapshot.child(String.valueOf(i)).child("nome").getValue().toString());
                                animal.setRaca(dataSnapshot.child(String.valueOf(i)).child("raca").getValue().toString());
                                //animal.setCor(dataSnapshot.child(String.valueOf(i)).child("animal").getValue().toString());
                                animal.setColeira(dataSnapshot.child(String.valueOf(i)).child("coleira").getValue().toString());
                                animal.setDono(dataSnapshot.child(String.valueOf(i)).child("dono").getValue().toString());
                                animal.setTelefone(dataSnapshot.child(String.valueOf(i)).child("telefone").getValue().toString());
                                animal.setLatitude(dataSnapshot.child(String.valueOf(i)).child("latitude").getValue().toString());
                                animal.setLongitude(dataSnapshot.child(String.valueOf(i)).child("longitude").getValue().toString());
                                animal.setUsuario(usuario);
                                listaReg.add(animal);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                Intent intent = new Intent(HomeMenuActivity.this, InicialLogadoActivity.class);
                intent.putExtra("listaRegistros", (Parcelable) listaReg);
            }
        }); // END onClick

        bMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenuActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
