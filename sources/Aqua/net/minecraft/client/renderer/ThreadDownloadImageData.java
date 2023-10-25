package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.http.HttpPipeline;
import net.optifine.http.HttpRequest;
import net.optifine.http.HttpResponse;
import net.optifine.player.CapeImageBuffer;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData
extends SimpleTexture {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;
    public Boolean imageFound = null;
    public boolean pipeline = false;

    public ThreadDownloadImageData(File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
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
            if (Config.isShaders()) {
                ShadersTex.loadSimpleTexture((int)super.getGlTextureId(), (BufferedImage)this.bufferedImage, (boolean)false, (boolean)false, (IResourceManager)Config.getResourceManager(), (ResourceLocation)this.textureLocation, (MultiTexID)this.getMultiTexID());
            } else {
                TextureUtil.uploadTextureImage((int)super.getGlTextureId(), (BufferedImage)this.bufferedImage);
            }
        }
    }

    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }

    public void setBufferedImage(BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
        this.imageFound = this.bufferedImage != null;
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                logger.debug("Loading http texture from local cache ({})", new Object[]{this.cacheFile});
                try {
                    this.bufferedImage = ImageIO.read((File)this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                    this.loadingFinished();
                }
                catch (IOException ioexception) {
                    logger.error("Couldn't load skin " + this.cacheFile, (Throwable)ioexception);
                    this.loadTextureFromServer();
                }
            } else {
                this.loadTextureFromServer();
            }
        }
    }

    protected void loadTextureFromServer() {
        this.imageThread = new /* Unavailable Anonymous Inner Class!! */;
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }

    private boolean shouldPipeline() {
        if (!this.pipeline) {
            return false;
        }
        Proxy proxy = Minecraft.getMinecraft().getProxy();
        return proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.SOCKS ? false : this.imageUrl.startsWith("http://");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadPipelined() {
        try {
            BufferedImage bufferedimage;
            HttpRequest httprequest = HttpPipeline.makeRequest((String)this.imageUrl, (Proxy)Minecraft.getMinecraft().getProxy());
            HttpResponse httpresponse = HttpPipeline.executeRequest((HttpRequest)httprequest);
            if (httpresponse.getStatus() / 100 != 2) {
                return;
            }
            byte[] abyte = httpresponse.getBody();
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
            if (this.cacheFile != null) {
                FileUtils.copyInputStreamToFile((InputStream)bytearrayinputstream, (File)this.cacheFile);
                bufferedimage = ImageIO.read((File)this.cacheFile);
            } else {
                bufferedimage = TextureUtil.readBufferedImage((InputStream)bytearrayinputstream);
            }
            if (this.imageBuffer != null) {
                bufferedimage = this.imageBuffer.parseUserSkin(bufferedimage);
            }
            this.setBufferedImage(bufferedimage);
        }
        catch (Exception exception) {
            logger.error("Couldn't download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
            return;
        }
        finally {
            this.loadingFinished();
        }
    }

    private void loadingFinished() {
        this.imageFound = this.bufferedImage != null;
        if (this.imageBuffer instanceof CapeImageBuffer) {
            CapeImageBuffer capeimagebuffer = (CapeImageBuffer)this.imageBuffer;
            capeimagebuffer.cleanup();
        }
    }

    public IImageBuffer getImageBuffer() {
        return this.imageBuffer;
    }

    static /* synthetic */ String access$000(ThreadDownloadImageData x0) {
        return x0.imageUrl;
    }

    static /* synthetic */ File access$100(ThreadDownloadImageData x0) {
        return x0.cacheFile;
    }

    static /* synthetic */ Logger access$200() {
        return logger;
    }

    static /* synthetic */ boolean access$300(ThreadDownloadImageData x0) {
        return x0.shouldPipeline();
    }

    static /* synthetic */ void access$400(ThreadDownloadImageData x0) {
        x0.loadPipelined();
    }

    static /* synthetic */ IImageBuffer access$500(ThreadDownloadImageData x0) {
        return x0.imageBuffer;
    }

    static /* synthetic */ void access$600(ThreadDownloadImageData x0) {
        x0.loadingFinished();
    }
}
