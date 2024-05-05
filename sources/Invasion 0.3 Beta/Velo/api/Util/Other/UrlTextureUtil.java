package Velo.api.Util.Other;
import org.apache.commons.io.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import java.awt.image.*;
import java.io.*;
import net.minecraft.client.renderer.texture.*;

public class UrlTextureUtil
{
    public static void downloadAndSetTexture(final String url, final ResourceLocationCallback callback) {
        if (url != null && !url.isEmpty()) {
            final String baseName = FilenameUtils.getBaseName(url);
            final ResourceLocation resourcelocation = new ResourceLocation("clientname_temp/" + baseName);
            final TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            final ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData) {
                final ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
                if (threaddownloadimagedata.imageFound != null) {
                    if (threaddownloadimagedata.imageFound) {
                        callback.onTextureLoaded(resourcelocation);
                    }
                    return;
                }
            }
            final IImageBuffer iimagebuffer = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();
                
                @Override
                public BufferedImage parseUserSkin(final BufferedImage image) {
                    return image;
                }
                
                @Override
                public void skinAvailable() {
                    callback.onTextureLoaded(resourcelocation);
                }
            };
            final ThreadDownloadImageData threaddownloadimagedata2 = new ThreadDownloadImageData(null, url, null, iimagebuffer);
            threaddownloadimagedata2.pipeline = true;
            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata2);
        }
    }
    
    public interface ResourceLocationCallback
    {
        void onTextureLoaded(final ResourceLocation p0);
    }
}
