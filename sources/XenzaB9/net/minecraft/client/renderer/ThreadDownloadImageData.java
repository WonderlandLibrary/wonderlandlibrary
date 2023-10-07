// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import optifine.HttpResponse;
import optifine.HttpRequest;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import optifine.HttpPipeline;
import java.net.Proxy;
import optifine.Config;
import org.apache.commons.io.FileUtils;
import net.minecraft.client.Minecraft;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.renderer.texture.SimpleTexture;

public class ThreadDownloadImageData extends SimpleTexture
{
    private static final Logger logger;
    private static final AtomicInteger threadDownloadCounter;
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;
    private static final String __OBFID = "CL_00001049";
    public Boolean imageFound;
    public boolean pipeline;
    
    public ThreadDownloadImageData(final File cacheFileIn, final String imageUrlIn, final ResourceLocation textureResourceLocation, final IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
        this.imageFound = null;
        this.pipeline = false;
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }
    
    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            this.textureUploaded = true;
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
        }
    }
    
    @Override
    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }
    
    public void setBufferedImage(final BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
        this.imageFound = (this.bufferedImage != null);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                    this.imageFound = (this.bufferedImage != null);
                }
                catch (final IOException var3) {
                    ThreadDownloadImageData.logger.error("Couldn't load skin " + this.cacheFile, var3);
                    this.loadTextureFromServer();
                }
            }
            else {
                this.loadTextureFromServer();
            }
        }
    }
    
    protected void loadTextureFromServer() {
        (this.imageThread = new Thread("Texture Downloader #" + ThreadDownloadImageData.threadDownloadCounter.incrementAndGet()) {
            private static final String __OBFID = "CL_00001050";
            
            @Override
            public void run() {
                HttpURLConnection httpurlconnection = null;
                if (ThreadDownloadImageData.this.shouldPipeline()) {
                    ThreadDownloadImageData.this.loadPipelined();
                }
                else {
                    try {
                        httpurlconnection = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
                        httpurlconnection.setDoInput(true);
                        httpurlconnection.setDoOutput(false);
                        httpurlconnection.connect();
                        if (httpurlconnection.getResponseCode() / 100 == 2) {
                            BufferedImage bufferedimage;
                            if (ThreadDownloadImageData.this.cacheFile != null) {
                                FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                                bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                            }
                            else {
                                bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                            }
                            if (ThreadDownloadImageData.this.imageBuffer != null) {
                                bufferedimage = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(bufferedimage);
                            }
                            ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
                            return;
                        }
                        if (httpurlconnection.getErrorStream() != null) {
                            Config.readAll(httpurlconnection.getErrorStream());
                        }
                    }
                    catch (final Exception var6) {
                        ThreadDownloadImageData.logger.error("Couldn't download http texture: " + var6.getClass().getName() + ": " + var6.getMessage());
                    }
                    finally {
                        if (httpurlconnection != null) {
                            httpurlconnection.disconnect();
                        }
                        ThreadDownloadImageData.this.imageFound = (ThreadDownloadImageData.this.bufferedImage != null);
                    }
                }
            }
        }).setDaemon(true);
        this.imageThread.start();
    }
    
    private boolean shouldPipeline() {
        if (!this.pipeline) {
            return false;
        }
        final Proxy proxy = Minecraft.getMinecraft().getProxy();
        return (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) && this.imageUrl.startsWith("http://");
    }
    
    private void loadPipelined() {
        try {
            final HttpRequest httprequest = HttpPipeline.makeRequest(this.imageUrl, Minecraft.getMinecraft().getProxy());
            final HttpResponse httpresponse = HttpPipeline.executeRequest(httprequest);
            if (httpresponse.getStatus() / 100 == 2) {
                final byte[] abyte = httpresponse.getBody();
                final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
                BufferedImage bufferedimage;
                if (this.cacheFile != null) {
                    FileUtils.copyInputStreamToFile((InputStream)bytearrayinputstream, this.cacheFile);
                    bufferedimage = ImageIO.read(this.cacheFile);
                }
                else {
                    bufferedimage = TextureUtil.readBufferedImage(bytearrayinputstream);
                }
                if (this.imageBuffer != null) {
                    bufferedimage = this.imageBuffer.parseUserSkin(bufferedimage);
                }
                this.setBufferedImage(bufferedimage);
            }
        }
        catch (final Exception var9) {
            ThreadDownloadImageData.logger.error("Couldn't download http texture: " + var9.getClass().getName() + ": " + var9.getMessage());
        }
        finally {
            this.imageFound = (this.bufferedImage != null);
        }
    }
    
    static {
        logger = LogManager.getLogger();
        threadDownloadCounter = new AtomicInteger(0);
    }
}
