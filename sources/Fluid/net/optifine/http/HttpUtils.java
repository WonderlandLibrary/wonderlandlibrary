// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.http;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils
{
    private static String playerItemsUrl;
    public static final String SERVER_URL = "http://s.optifine.net";
    public static final String POST_URL = "http://optifine.net";
    
    static {
        HttpUtils.playerItemsUrl = null;
    }
    
    public static byte[] get(final String urlStr) throws IOException {
        HttpURLConnection httpurlconnection = null;
        byte[] abyte2;
        try {
            final URL url = new URL(urlStr);
            httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();
            if (httpurlconnection.getResponseCode() / 100 != 2) {
                if (httpurlconnection.getErrorStream() != null) {
                    Config.readAll(httpurlconnection.getErrorStream());
                }
                throw new IOException("HTTP response: " + httpurlconnection.getResponseCode());
            }
            final InputStream inputstream = httpurlconnection.getInputStream();
            final byte[] abyte = new byte[httpurlconnection.getContentLength()];
            int i = 0;
            do {
                final int j = inputstream.read(abyte, i, abyte.length - i);
                if (j < 0) {
                    throw new IOException("Input stream closed: " + urlStr);
                }
                i += j;
            } while (i < abyte.length);
            abyte2 = abyte;
        }
        finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        if (httpurlconnection != null) {
            httpurlconnection.disconnect();
        }
        return abyte2;
    }
    
    public static String post(final String urlStr, final Map headers, final byte[] content) throws IOException {
        HttpURLConnection httpurlconnection = null;
        String s4;
        try {
            final URL url = new URL(urlStr);
            httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setRequestMethod("POST");
            if (headers != null) {
                for (final Object s : headers.keySet()) {
                    final String s2 = new StringBuilder().append(headers.get(s)).toString();
                    httpurlconnection.setRequestProperty((String)s, s2);
                }
            }
            httpurlconnection.setRequestProperty("Content-Type", "text/plain");
            httpurlconnection.setRequestProperty("Content-Length", new StringBuilder().append(content.length).toString());
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            final OutputStream outputstream = httpurlconnection.getOutputStream();
            outputstream.write(content);
            outputstream.flush();
            outputstream.close();
            final InputStream inputstream = httpurlconnection.getInputStream();
            final InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
            final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            final StringBuffer stringbuffer = new StringBuffer();
            String s3;
            while ((s3 = bufferedreader.readLine()) != null) {
                stringbuffer.append(s3);
                stringbuffer.append('\r');
            }
            bufferedreader.close();
            s4 = stringbuffer.toString();
        }
        finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        if (httpurlconnection != null) {
            httpurlconnection.disconnect();
        }
        return s4;
    }
    
    public static synchronized String getPlayerItemsUrl() {
        if (HttpUtils.playerItemsUrl == null) {
            try {
                final boolean flag = Config.parseBoolean(System.getProperty("player.models.local"), false);
                if (flag) {
                    final File file1 = Minecraft.getMinecraft().mcDataDir;
                    final File file2 = new File(file1, "playermodels");
                    HttpUtils.playerItemsUrl = file2.toURI().toURL().toExternalForm();
                }
            }
            catch (final Exception exception) {
                Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
            if (HttpUtils.playerItemsUrl == null) {
                HttpUtils.playerItemsUrl = "http://s.optifine.net";
            }
        }
        return HttpUtils.playerItemsUrl;
    }
}
