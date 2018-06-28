package com.ps2.petsfinder.petsfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.HashMap;
import java.util.Map;

import static com.ps2.petsfinder.petsfinder.PFUtil.getNomeUsuario;

public class RegistroAnimalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PFUtil util = new PFUtil();
    private int selectedColor;
    CorAnimal corAnimal;
    private RegistroAnimal registroAnimal;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference registroAnimalBase = databaseReference.child("registroAnimal");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://petsfinder-d9b91.appspot.com");


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
    private long regAnimalCount = 0;

    private ImageView imagem;
    private final int GALERIA_IMAGENS = 1;
    private final int PERMISSAO_REQUEST = 2;
    private Bitmap thumbnail;

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

        final long regAminalCount;

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
                                    finish();
                                break;
                            case 1:
                                break;
                            case 2: Toast.makeText(RegistroAnimalActivity.this, "Novo registro", Toast.LENGTH_SHORT).show();
                                break;
                            case 4: startActivity(new Intent(RegistroAnimalActivity.this, MainActivity.class));
                                    finish();
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
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        imagem = (ImageView) findViewById(R.id.imgAnimal);

        btnLoadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegistroAnimalActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RegistroAnimalActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    } else {
                        ActivityCompat.requestPermissions(RegistroAnimalActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSAO_REQUEST);
                    }
                }

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final RegistroAnimal mostraReg = (RegistroAnimal) getIntent().getSerializableExtra("mostraReg");
                    if(mostraReg.getTelaMostraRegistro().equals("TRUE"))
                        regAnimalCount = Integer.parseInt(mostraReg.getId());
                }catch(Exception e){}

                registroAnimal = new RegistroAnimal();
                adicionaRegistro();

                Map<String, String> mapRegistroAnimal = new HashMap<String, String>();
                mapRegistroAnimal.put("animal", registroAnimal.getAnimal());
                mapRegistroAnimal.put("nome", registroAnimal.getNome());
                mapRegistroAnimal.put("raca", registroAnimal.getRaca());
                mapRegistroAnimal.put("cor", String.valueOf(registroAnimal.getCor().getCor()));
                mapRegistroAnimal.put("coleira", registroAnimal.getColeira());
                mapRegistroAnimal.put("dono", registroAnimal.getDono());
                mapRegistroAnimal.put("telefone", registroAnimal.getTelefone());
                mapRegistroAnimal.put("latitude", registroAnimal.getLatitude());
                mapRegistroAnimal.put("longitude", registroAnimal.getLongitude());
                mapRegistroAnimal.put("usuario", registroAnimal.getUsuario());
                registroAnimalBase.child(String.valueOf(regAnimalCount)).setValue(mapRegistroAnimal);

                if(thumbnail != null){
                    imagem.setDrawingCacheEnabled(true);
                    imagem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    imagem.layout(0, 0, imagem.getMeasuredWidth(), imagem.getMeasuredHeight());
                    imagem.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(imagem.getDrawingCache());

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    String nomeImg = montaNomeImg(String.valueOf(regAnimalCount), registroAnimal.getUsuario());
                    UploadTask uploadTask = storageRef.child(nomeImg).putBytes(data);
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

                Intent intent = new Intent(RegistroAnimalActivity.this, MapsActivity.class);
                Toast.makeText(getApplicationContext(), "Registro adicionado", Toast.LENGTH_SHORT).show();
                startActivity(intent);

                finish();
            }
        });

        regAnimalSize();

        try {
            final RegistroAnimal mostraReg = (RegistroAnimal) getIntent().getSerializableExtra("mostraReg");
            if(mostraReg.getTelaMostraRegistro().equals("TRUE")){
                editAnimal.setText(mostraReg.getAnimal());
                editNome.setText(mostraReg.getNome());
                editRaca.setText(mostraReg.getRaca());
                //corPelo.setBackgroundColor(mostraReg.getCor().getCor());
                editPulseira.setText(mostraReg.getColeira());
                editDono.setText(mostraReg.getDono());
                editTelefone.setText(mostraReg.getTelefone());
                lat = mostraReg.getLatitude();
                lng = mostraReg.getLongitude();
            }
        }catch (Exception e){
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            lat = bundle.getString("lat");
            lng = bundle.getString("lng");
        }


    } //onCreate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            thumbnail = (BitmapFactory.decodeFile(picturePath));
            imagem.setImageBitmap(thumbnail);
        }
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
        registroAnimal.setUsuario(getNomeUsuario());
    }

    public void regAnimalSize(){
        registroAnimalBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                regAnimalCount = dataSnapshot.getChildrenCount() + 1;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public String montaNomeImg(String id, String usuario){
        return id + "_" + usuario;
    }

}
