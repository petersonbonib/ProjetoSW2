package com.ps2.petsfinder.petsfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import static android.support.v4.content.ContextCompat.startActivity;

public class PFUtil extends AppCompatActivity {

    public static String usuario;

    public String getNomeUsuario(String user){
        this.usuario = user;
        return user;
    }

    public static String nomeUsuario(){
        return usuario;
    }

}
