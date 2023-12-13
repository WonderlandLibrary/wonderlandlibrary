// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.cape;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Capes
{
    public static ArrayList<String> lines;
    
    public static void getUsersCape() {
        try {
            final URL url = new URL("https://www.pastebin.com/raw/MiWJDQRF");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Capes.lines.add(line);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean isCapeUser(final String name) {
        return Capes.lines.contains(name);
    }
    
    static {
        Capes.lines = new ArrayList<String>();
    }
}
