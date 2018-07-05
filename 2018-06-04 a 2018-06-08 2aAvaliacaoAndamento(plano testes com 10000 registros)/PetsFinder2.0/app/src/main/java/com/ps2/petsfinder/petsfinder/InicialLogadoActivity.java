package com.ps2.petsfinder.petsfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import static com.ps2.petsfinder.petsfinder.R.layout.activity_inicial_logado;

public class InicialLogadoActivity extends AppCompatActivity {

    private TextView tv;
    private Toolbar toolbar;
    private RegistroAnimal registroAnimal;
    private AccountHeader header;
    PrimaryDrawerItem itemOpenMap = new PrimaryDrawerItem().withIdentifier(0).withName("Abrir mapa").withIcon(R.drawable.maps);
    @SuppressLint("ResourceType")
    PrimaryDrawerItem itemLostPets = new PrimaryDrawerItem().withIdentifier(1).withName("Animais perdidos").withIcon(R.drawable.saddog);

    @SuppressLint("ResourceType")
    SecondaryDrawerItem itemNewPet = new SecondaryDrawerItem().withIdentifier(2).withName("Novo registro").withIcon(R.drawable.new_pet);
    PrimaryDrawerItem itemSair = new PrimaryDrawerItem().withIdentifier(4).withName("Sair").withIcon(R.drawable.exit);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial_logado);

        //tv = (TextView) findViewById(R.id.textView);

        //if (registroAnimal != null) {
         //   tv.setText(registroAnimal.getDono());
        //}


        toolbar = (Toolbar) findViewById(R.id.tb_inicial);
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
                            case 0: startActivity(new Intent(InicialLogadoActivity.this, MapsActivity.class));
                                break;
                            case 1: startActivity(new Intent(InicialLogadoActivity.this, RegistroAnimalActivity.class));
                                break;
                            case 2: Toast.makeText(InicialLogadoActivity.this, "Novo registro", Toast.LENGTH_SHORT).show();
                                break;
                            case 4: startActivity(new Intent(InicialLogadoActivity.this, MainActivity.class));
                        }

                        return false;
                    }
                })
                .build();
    }

    public void getRegistrosAnimais(RegistroAnimal ra){
        registroAnimal = ra;
    }


}
