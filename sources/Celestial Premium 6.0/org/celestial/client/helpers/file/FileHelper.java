/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.file;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.Minecraft;
import org.celestial.client.Celestial;
import org.celestial.client.helpers.Helper;

public class FileHelper
implements Helper {
    public static void downloadFileFromUrl(String url, File file) {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();
            try (InputStream is = con.getInputStream();
                 FileOutputStream fos = new FileOutputStream(file);){
                int readedLen;
                byte[] buff = new byte[8192];
                while ((readedLen = is.read(buff)) > -1) {
                    fos.write(buff, 0, readedLen);
                }
            }
            con.disconnect();
        }
        catch (IOException e) {
            System.exit(-1);
        }
    }

    private static File getConfigDir() {
        File file = new File(Minecraft.getMinecraft().gameDir, Celestial.name);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getConfigFile(String name) {
        File file = new File(FileHelper.getConfigDir(), String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void showURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

