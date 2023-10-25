package fun.rich.client.utils.render.cosmetic;

import fun.rich.client.Rich;
import fun.rich.client.feature.impl.visual.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CosmeticRender implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public CosmeticRender(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!Rich.instance.featureManager.getFeature(Cosmetics.class).isEnabled())
            return;
        if (!player.hasPlayerInfo() || player.isInvisible())
            return;
        if (player == Minecraft.getMinecraft().player) {
            Cosmetic.renderAccessory(new String[]{"Dragon_wing"}, player, partialTicks);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public static void bindTexture(String url, String resource) {
        IImageBuffer iib = new IImageBuffer() {
            ImageBufferDownload ibd = new ImageBufferDownload();

            public BufferedImage parseUserSkin(BufferedImage var1) {
                return CosmeticRender.parseCape(var1);
            }

            public void skinAvailable() {
            }
        };
        ResourceLocation rl = new ResourceLocation(resource);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.getTexture(rl);
        ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
        textureManager.loadTexture(rl, textureCape);
    }

    private static BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        int srcWidth = img.getWidth(), srcHeight = img.getHeight();
        while (true) {
            if (imageWidth < srcWidth || imageHeight < srcHeight) {
                imageWidth *= 2;
                imageHeight *= 2;
                continue;
            }
            BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
            Graphics g = imgNew.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            return imgNew;
        }
    }
}
