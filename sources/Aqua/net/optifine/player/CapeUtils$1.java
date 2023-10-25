package net.optifine.player;

import java.awt.image.BufferedImage;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;
import net.optifine.player.CapeUtils;

/*
 * Exception performing whole class analysis ignored.
 */
static final class CapeUtils.1
implements IImageBuffer {
    ImageBufferDownload ibd = new ImageBufferDownload();
    final /* synthetic */ AbstractClientPlayer val$player;
    final /* synthetic */ ResourceLocation val$resourcelocation;

    CapeUtils.1(AbstractClientPlayer abstractClientPlayer, ResourceLocation resourceLocation) {
        this.val$player = abstractClientPlayer;
        this.val$resourcelocation = resourceLocation;
    }

    public BufferedImage parseUserSkin(BufferedImage image) {
        return CapeUtils.parseCape((BufferedImage)image);
    }

    public void skinAvailable() {
        this.val$player.setLocationOfCape(this.val$resourcelocation);
    }
}
