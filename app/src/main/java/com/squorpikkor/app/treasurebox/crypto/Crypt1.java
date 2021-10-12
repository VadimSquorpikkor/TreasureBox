package com.squorpikkor.app.treasurebox.crypto;

import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**НЕ РАБОТАЕТ*/
public class Crypt1 {

    public static void start() {
        String s = "Строка для шифрования";
        // Set up secret key spec for 128-bit AES encryption and decryption
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("Crypto", "AES secret key spec error");
        }

        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(s.getBytes());
        } catch (Exception e) {
            Log.e("Crypto", "AES encryption error");
        }


        Log.e("TAG", "crypt1: encodedBytes - "+encodedBytes);

        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("Crypto", "AES decryption error");
        }

        Log.e("TAG", "crypt1: decodedBytes - "+decodedBytes);
    }
}
