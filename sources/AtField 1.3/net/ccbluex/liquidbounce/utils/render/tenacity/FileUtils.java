/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class FileUtils {
    public static void unpackFile(File file, String string) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.copy((InputStream)FileUtils.class.getClassLoader().getResourceAsStream(string), (OutputStream)fileOutputStream);
        fileOutputStream.close();
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string).append('\n');
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String string;
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string).append('\n');
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

