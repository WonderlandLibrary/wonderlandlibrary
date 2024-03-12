// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.util.ResourceLocation;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import dev.tenacity.utils.Utils;

public class NetworkingUtils implements Utils
{
    private static final String USER_AGENT = "KingClient";
    public static boolean bypassed;
    public static int image;
    
    public static HttpResponse httpsConnection(final String url) {
        bypassSSL();
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "KingClient");
            connection.connect();
            return new HttpResponse(FileUtils.readInputStream(connection.getInputStream()), connection.getResponseCode());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static HttpResponse httpConnection(final String url) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "KingClient");
            connection.connect();
            return new HttpResponse(FileUtils.readInputStream(connection.getErrorStream()), connection.getResponseCode());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ResourceLocation downloadImage(final String url) {
        final ThreadDownloadImageData avatarImage = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(final BufferedImage image) {
                return image;
            }
            
            @Override
            public void skinAvailable() {
            }
        });
        final ResourceLocation location = new ResourceLocation("onlineAsset/" + NetworkingUtils.image++);
        NetworkingUtils.mc.getTextureManager().loadTexture(location, avatarImage);
        return location;
    }
    
    public static void bypassSSL() {
        if (!NetworkingUtils.bypassed) {
            final TrustManager[] trustAllCerts = { new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    
                    @Override
                    public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    }
                    
                    @Override
                    public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    }
                } };
            try {
                final SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            }
            catch (Exception ex) {}
            NetworkingUtils.bypassed = true;
        }
    }
    
    static {
        NetworkingUtils.image = 0;
    }
    
    public static class HttpResponse
    {
        private final String content;
        private final int response;
        
        public String getContent() {
            return this.content;
        }
        
        public int getResponse() {
            return this.response;
        }
        
        public HttpResponse(final String content, final int response) {
            this.content = content;
            this.response = response;
        }
    }
}
