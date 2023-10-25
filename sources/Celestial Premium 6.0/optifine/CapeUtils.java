/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import optifine.CapeImageBuffer;
import org.apache.commons.io.FilenameUtils;

public class CapeUtils {
    public static void downloadCape(AbstractClientPlayer p_downloadCape_0_) {
        String s = p_downloadCape_0_.getNameClear();
        if (s != null && !s.isEmpty()) {
            String s1 = "http://s.optifine.net/capes/" + s + ".png";
            String s2 = FilenameUtils.getBaseName(s1);
            ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s2);
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
                if (threaddownloadimagedata.imageFound != null) {
                    if (threaddownloadimagedata.imageFound.booleanValue()) {
                        p_downloadCape_0_.setLocationOfCape(resourcelocation);
                    }
                    return;
                }
            }
            CapeImageBuffer capeimagebuffer = new CapeImageBuffer(p_downloadCape_0_, resourcelocation);
            ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData(null, s1, null, capeimagebuffer);
            threaddownloadimagedata1.pipeline = true;
            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata1);
        }
    }

    public static BufferedImage parseCape(BufferedImage p_parseCape_0_) {
        int j;
        int i = 64;
        int k = p_parseCape_0_.getWidth();
        int l = p_parseCape_0_.getHeight();
        for (j = 32; i < k || j < l; i *= 2, j *= 2) {
        }
        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(p_parseCape_0_, 0, 0, null);
        graphics.dispose();
        return bufferedimage;
    }
}

