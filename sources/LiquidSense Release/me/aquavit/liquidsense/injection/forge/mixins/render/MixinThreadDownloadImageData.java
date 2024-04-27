package me.aquavit.liquidsense.injection.forge.mixins.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ThreadDownloadImageData.class)
@SideOnly(Side.CLIENT)
public abstract class MixinThreadDownloadImageData {

    @Shadow
    private Thread imageThread;

    @Final
    @Shadow
    private static AtomicInteger threadDownloadCounter = new AtomicInteger(0);

    @Final
    @Shadow
    private static Logger logger = LogManager.getLogger();

    @Final
    @Shadow
    private File cacheFile;

    @Final
    @Shadow
    private String imageUrl;

    @Final
    @Shadow
    private IImageBuffer imageBuffer;

    @Shadow
    public abstract void setBufferedImage(BufferedImage bufferedImageIn);

    /**
     * @author AquaVit
     * @reason 急死了
     */
    @Overwrite
    protected void loadTextureFromServer() {
        this.imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet()) {
            public void run() {
                HttpURLConnection httpurlconnection = null;
                logger.debug("Downloading http texture from {} to {}", new Object[]{imageUrl, cacheFile});

                try {
                    httpurlconnection = (HttpURLConnection)(new URL(imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();
                    if (httpurlconnection.getResponseCode() / 100 != 2) {
                        return;
                    }

                    BufferedImage bufferedimage;
                    if (cacheFile != null) {
                        FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), cacheFile);
                        bufferedimage = ImageIO.read(cacheFile);
                    } else {
                        bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                    }

                    if (imageBuffer != null) {
                        bufferedimage = imageBuffer.parseUserSkin(bufferedimage);
                    }

                    setBufferedImage(bufferedimage);
                } catch (Exception var6) {
                    logger.error("Couldn't download http texture", var6);
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                    logger.error("Fuck U SB");
                } finally {
                    if (httpurlconnection != null) {
                        httpurlconnection.disconnect();
                    }

                }

            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }

}

