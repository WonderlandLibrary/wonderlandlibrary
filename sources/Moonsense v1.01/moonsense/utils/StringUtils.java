// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtils
{
    public static URL sneakyParse(final String url) {
        try {
            return new URL(url);
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
    
    public static String urlToString(final URL url) throws IOException {
        final URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", System.getProperty("http.agent"));
        final InputStream in = connection.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
    
    public static String getRelativeToPackFolder(final File packFile) {
        String relative = new File(Minecraft.getMinecraft().mcDataDir, "resourcepacks").toPath().toAbsolutePath().relativize(packFile.toPath().toAbsolutePath()).toString();
        if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            relative = relative.replace("\\", "/");
        }
        return relative;
    }
    
    public static String title(final String inString) {
        String ns = "";
        final String[] split = inString.split(" ");
        String[] array;
        for (int length = (array = split).length, i = 0; i < length; ++i) {
            String s = array[i];
            final char c = s.charAt(0);
            s = s.substring(1).toLowerCase();
            s = String.valueOf(new StringBuilder().append(c).toString().toUpperCase()) + s;
            ns = String.valueOf(ns) + s + " ";
        }
        ns = ns.trim();
        return ns;
    }
}
