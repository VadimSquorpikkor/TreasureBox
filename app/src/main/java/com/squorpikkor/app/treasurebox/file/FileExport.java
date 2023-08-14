package com.squorpikkor.app.treasurebox.file;

import android.util.Log;

import com.squorpikkor.app.treasurebox.Entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FileExport {

    public static final String PATTERN_FILE_DATE = "yyyyMMdd";

    public static void export(ArrayList<Entity> list, File dir) {
        Log.e("", "" + list);
        if (list == null) return;
        Executors.newSingleThreadExecutor().submit(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_FILE_DATE, Locale.US);

            File file = new File(dir.getPath(), "TreasureBox_" + dateFormat.format(new Date()) + ".txt");
            //noinspection IOStreamConstructor
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))
            {
                String text = textFromList(list);
                writer.write('\ufeff');
                writer.newLine();
                writer.write(text);

            } catch (Exception e) {
                Log.e("", "error");
            }
        });
    }

    private static String textFromList(ArrayList<Entity> list) {
        StringBuilder s = new StringBuilder();
        for (Entity entity : list) {
            s.append(getString("", entity.getName()));
            s.append(getString("login", entity.getLogin()));
            s.append(getString("pass", entity.getPass()));
            s.append(getString("email", entity.getEmail()));
            s.append(getString("", entity.getAdds()));
            s.append("-----------------------------------------\n");
        }

        return s.toString();
    }

    private static String getString(String cat, String e) {
        if (e == null || e.equals("")) return "";
        else if (cat.equals("")) return e + "\n";
        else return cat + ": " + e + "\n";
    }
}
