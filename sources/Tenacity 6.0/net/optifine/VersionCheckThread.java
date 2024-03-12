// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.io.InputStream;
import net.minecraft.client.ClientBrandRetriever;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.src.Config;

public class VersionCheckThread extends Thread
{
    public VersionCheckThread() {
        super("VersionCheck");
    }
    
    @Override
    public void run() {
        HttpURLConnection httpurlconnection = null;
        try {
            Config.dbg("Checking for new version");
            final URL url = new URL("http://optifine.net/version/1.8.9/HD_U.txt");
            httpurlconnection = (HttpURLConnection)url.openConnection();
            if (Config.getGameSettings().snooperEnabled) {
                httpurlconnection.setRequestProperty("OF-MC-Version", "1.8.9");
                httpurlconnection.setRequestProperty("OF-MC-Brand", "" + ClientBrandRetriever.getClientModName());
                httpurlconnection.setRequestProperty("OF-Edition", "HD_U");
                httpurlconnection.setRequestProperty("OF-Release", "M5");
                httpurlconnection.setRequestProperty("OF-Java-Version", "" + System.getProperty("java.version"));
                httpurlconnection.setRequestProperty("OF-CpuCount", "" + Config.getAvailableProcessors());
                httpurlconnection.setRequestProperty("OF-OpenGL-Version", "" + Config.openGlVersion);
                httpurlconnection.setRequestProperty("OF-OpenGL-Vendor", "" + Config.openGlVendor);
            }
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();
            try {
                final InputStream inputstream = httpurlconnection.getInputStream();
                final String s = Config.readInputStream(inputstream);
                inputstream.close();
                final String[] astring = Config.tokenize(s, "\n\r");
                if (astring.length >= 1) {
                    final String s2 = astring[0].trim();
                    Config.dbg("Version found: " + s2);
                    if (Config.compareRelease(s2, "M5") <= 0) {
                        return;
                    }
                    Config.setNewRelease(s2);
                }
            }
            finally {
                if (httpurlconnection != null) {
                    httpurlconnection.disconnect();
                }
            }
        }
        catch (Exception exception) {
            Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
}
