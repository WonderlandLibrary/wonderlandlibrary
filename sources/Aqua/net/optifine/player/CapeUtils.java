package net.optifine.player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.player.CapeUtils;

public class CapeUtils {
    private static final Pattern PATTERN_USERNAME = Pattern.compile((String)"[a-zA-Z0-9_]+");

    public static void downloadCape(AbstractClientPlayer player) {
        String s = player.getNameClear();
        if (s != null && !s.isEmpty()) {
            String s1 = "https://i.imgur.com/peBYZFW.png";
            String s2 = "https://i.imgur.com/peBYZFW.png";
            ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s2);
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData) {
                ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
                if (threaddownloadimagedata.imageFound != null) {
                    if (threaddownloadimagedata.imageFound.booleanValue()) {
                        player.setLocationOfCape(resourcelocation);
                    }
                    return;
                }
            }
            1 iimagebuffer = new /* Unavailable Anonymous Inner Class!! */;
            ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData((File)null, s1, (ResourceLocation)null, (IImageBuffer)iimagebuffer);
            threaddownloadimagedata1.pipeline = true;
            texturemanager.loadTexture(resourcelocation, (ITextureObject)threaddownloadimagedata1);
        }
    }

    public static BufferedImage parseCape(BufferedImage img) {
        int j;
        int i = 64;
        int k = img.getWidth();
        int l = img.getHeight();
        for (j = 32; i < k || j < l; i *= 2, j *= 2) {
        }
        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage((Image)img, 0, 0, (ImageObserver)null);
        graphics.dispose();
        return bufferedimage;
    }

    public static boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed) {
        return imageRaw.getWidth() > imageFixed.getHeight();
    }

    public static void reloadCape(AbstractClientPlayer player) {
        String s = player.getNameClear();
        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
        if (itextureobject instanceof SimpleTexture) {
            SimpleTexture simpletexture = (SimpleTexture)itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }
        player.setLocationOfCape((ResourceLocation)null);
        player.setElytraOfCape(false);
        CapeUtils.downloadCape(player);
    }
}
