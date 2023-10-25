/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData
extends SimpleTexture {
    private BufferedImage bufferedImage;
    private boolean textureUploaded;
    private final String imageUrl;
    private static final AtomicInteger threadDownloadCounter;
    private final File cacheFile;
    private Thread imageThread;
    private static final Logger logger;
    private final IImageBuffer imageBuffer;

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
    }

    public ThreadDownloadImageData(File file, String string, ResourceLocation resourceLocation, IImageBuffer iImageBuffer) {
        super(resourceLocation);
        this.cacheFile = file;
        this.imageUrl = string;
        this.imageBuffer = iImageBuffer;
    }

    static {
        logger = LogManager.getLogger();
        threadDownloadCounter = new AtomicInteger(0);
    }

    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
            this.textureUploaded = true;
        }
    }

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(iResourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                logger.debug("Loading http texture from local cache ({})", new Object[]{this.cacheFile});
                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                }
                catch (IOException iOException) {
                    logger.error("Couldn't load skin " + this.cacheFile, (Throwable)iOException);
                    this.loadTextureFromServer();
                }
            } else {
                this.loadTextureFromServer();
            }
        }
    }

    protected void loadTextureFromServer() {
        this.imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet()){

            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                logger.debug("Downloading http texture from {} to {}", new Object[]{ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile});
                try {
                    httpURLConnection = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() / 100 == 2) {
                        BufferedImage bufferedImage;
                        if (ThreadDownloadImageData.this.cacheFile != null) {
                            FileUtils.copyInputStreamToFile((InputStream)httpURLConnection.getInputStream(), (File)ThreadDownloadImageData.this.cacheFile);
                            bufferedImage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                        } else {
                            bufferedImage = TextureUtil.readBufferedImage(httpURLConnection.getInputStream());
                        }
                        if (ThreadDownloadImageData.this.imageBuffer != null) {
                            bufferedImage = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(bufferedImage);
                        }
                        ThreadDownloadImageData.this.setBufferedImage(bufferedImage);
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return;
                    }
                }
                catch (Exception exception) {
                    logger.error("Couldn't download http texture", (Throwable)exception);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return;
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }

    @Override
    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }
}

