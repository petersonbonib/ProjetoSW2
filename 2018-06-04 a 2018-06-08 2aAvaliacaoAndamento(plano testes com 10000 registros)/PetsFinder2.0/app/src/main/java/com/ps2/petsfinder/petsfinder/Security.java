package com.ps2.petsfinder.petsfinder;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class Security {


    public String GeraHash(String senha, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        String hash = senha + salt;
        byte[] array = md.digest(hash.getBytes("UTF-8"));
        String result = new String(array, "UTF-8");
        return result;
    }

    public String GeraSalt(){
        int salt = new Random().nextInt(2344367);
        salt = Integer.valueOf(String.valueOf(salt), 16);
        return String.valueOf(salt);
    }
}
