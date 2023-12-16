// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.File;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CapeUtils
{
    public static void downloadCape(final AbstractClientPlayer player) {
        final String username = player.getNameClear();
        if (username != null && !username.isEmpty()) {
            final String ofCapeUrl = "http://s.optifine.net/capes/" + username + ".png";
            final String mptHash = FilenameUtils.getBaseName(ofCapeUrl);
            final ResourceLocation rl = new ResourceLocation("capeof/" + mptHash);
            final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            final ITextureObject tex = textureManager.getTexture(rl);
            if (tex != null && tex instanceof ThreadDownloadImageData) {
                final ThreadDownloadImageData thePlayer = (ThreadDownloadImageData)tex;
                if (thePlayer.imageFound != null) {
                    if (thePlayer.imageFound) {
                        player.setLocationOfCape(rl);
                    }
                    return;
                }
            }
            final IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();
                
                @Override
                public BufferedImage parseUserSkin(final BufferedImage var1) {
                    return CapeUtils.parseCape(var1);
                }
                
                @Override
                public void func_152634_a() {
                    player.setLocationOfCape(rl);
                }
            };
            final ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, ofCapeUrl, null, iib);
            textureCape.pipeline = true;
            textureManager.loadTexture(rl, textureCape);
        }
    }
    
    public static BufferedImage parseCape(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
}
