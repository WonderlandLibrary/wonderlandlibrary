// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils
{
    public static final String SERVER_URL = "http://s.optifine.net";
    public static final String POST_URL = "http://optifine.net";
    
    public static byte[] get(final String urlStr) throws IOException {
        HttpURLConnection conn = null;
        try {
            final URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();
            if (conn.getResponseCode() / 100 != 2) {
                throw new IOException("HTTP response: " + conn.getResponseCode());
            }
            final InputStream in = conn.getInputStream();
            final byte[] bytes = new byte[conn.getContentLength()];
            int pos = 0;
            do {
                final int len = in.read(bytes, pos, bytes.length - pos);
                if (len < 0) {
                    throw new IOException("Input stream closed: " + urlStr);
                }
                pos += len;
            } while (pos < bytes.length);
            final byte[] len2 = bytes;
            return len2;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    
    public static String post(final String urlStr, final Map headers, final byte[] content) throws IOException {
        HttpURLConnection conn = null;
        try {
            final URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            conn.setRequestMethod("POST");
            if (headers != null) {
                final Set os = headers.keySet();
                for (final String isr : os) {
                    final String br = "" + headers.get(isr);
                    conn.setRequestProperty(isr, br);
                }
            }
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Content-Length", "" + content.length);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            final OutputStream os2 = conn.getOutputStream();
            os2.write(content);
            os2.flush();
            os2.close();
            final InputStream in2 = conn.getInputStream();
            final InputStreamReader isr2 = new InputStreamReader(in2, "ASCII");
            final BufferedReader br2 = new BufferedReader(isr2);
            final StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br2.readLine()) != null) {
                sb.append(line);
                sb.append('\r');
            }
            br2.close();
            final String var11 = sb.toString();
            return var11;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
