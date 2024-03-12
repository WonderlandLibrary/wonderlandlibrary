// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.io.IOException;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;

public class FileUtils
{
    public static String readFile(final File file) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    public static void writeFile(final File file, final String content) {
        try {
            final FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String readInputStream(final InputStream inputStream) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
