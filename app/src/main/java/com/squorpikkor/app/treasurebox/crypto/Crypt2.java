package com.squorpikkor.app.treasurebox.crypto;

import android.util.Base64;
import android.util.Log;

/**РАБОТАЕТ, ОЧЕНЬ ПРОСТОЙ КЛАСС, ЭТО ВСЁ, ЧТО НУЖНО!*/
public class Crypt2 {
    public static void start() {
        String s = "hello world";
        decodeSomeText(codeSomeText(s));
    }

    public static String codeSomeText(String text) {
        text = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        Log.e("TAG", "crypt2: code: " + text);
        return text;
    }

    public static void decodeSomeText(String text) {
        text = new String(Base64.decode(text, Base64.DEFAULT));
        Log.e("TAG", "crypt2: decode: " + text);
    }
}
