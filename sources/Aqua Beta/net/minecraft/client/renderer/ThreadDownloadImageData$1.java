package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.src.Config;
import org.apache.commons.io.FileUtils;

/*
 * Exception performing whole class analysis ignored.
 */
class ThreadDownloadImageData.1
extends Thread {
    ThreadDownloadImageData.1(String x0) {
        super(x0);
    }

    public void run() {
        HttpURLConnection httpurlconnection = null;
        ThreadDownloadImageData.access$200().debug("Downloading http texture from {} to {}", new Object[]{ThreadDownloadImageData.access$000((ThreadDownloadImageData)ThreadDownloadImageData.this), ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this)});
        if (ThreadDownloadImageData.access$300((ThreadDownloadImageData)ThreadDownloadImageData.this)) {
            ThreadDownloadImageData.access$400((ThreadDownloadImageData)ThreadDownloadImageData.this);
        } else {
            try {
                BufferedImage bufferedimage;
                httpurlconnection = (HttpURLConnection)new URL(ThreadDownloadImageData.access$000((ThreadDownloadImageData)ThreadDownloadImageData.this)).openConnection(Minecraft.getMinecraft().getProxy());
                httpurlconnection.setDoInput(true);
                httpurlconnection.setDoOutput(false);
                httpurlconnection.connect();
                if (httpurlconnection.getResponseCode() / 100 != 2) {
                    if (httpurlconnection.getErrorStream() != null) {
                        Config.readAll((InputStream)httpurlconnection.getErrorStream());
                    }
                    return;
                }
                if (ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this) != null) {
                    FileUtils.copyInputStreamToFile((InputStream)httpurlconnection.getInputStream(), (File)ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this));
                    bufferedimage = ImageIO.read((File)ThreadDownloadImageData.access$100((ThreadDownloadImageData)ThreadDownloadImageData.this));
                } else {
                    bufferedimage = TextureUtil.readBufferedImage((InputStream)httpurlconnection.getInputStream());
                }
                if (ThreadDownloadImageData.access$500((ThreadDownloadImageData)ThreadDownloadImageData.this) != null) {
                    bufferedimage = ThreadDownloadImageData.access$500((ThreadDownloadImageData)ThreadDownloadImageData.this).parseUserSkin(bufferedimage);
                }
                ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
            }
            catch (Exception exception) {
                ThreadDownloadImageData.access$200().error("Couldn't download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
                return;
            }
            finally {
                if (httpurlconnection != null) {
                    httpurlconnection.disconnect();
                }
                ThreadDownloadImageData.access$600((ThreadDownloadImageData)ThreadDownloadImageData.this);
            }
        }
    }
}
