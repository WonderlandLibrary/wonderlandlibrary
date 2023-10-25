package optifine;

import java.awt.image.BufferedImage;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;

final class CapeUtils$1 implements IImageBuffer {
    ImageBufferDownload ibd;
    // $FF: synthetic field
    final AbstractClientPlayer val$p_downloadCape_0_;
    // $FF: synthetic field
    final ResourceLocation val$resourcelocation;

    CapeUtils$1(AbstractClientPlayer var1, ResourceLocation var2) {
        this.val$p_downloadCape_0_ = var1;
        this.val$resourcelocation = var2;
        this.ibd = new ImageBufferDownload();
    }

    public BufferedImage parseUserSkin(BufferedImage image) {
        return CapeUtils.parseCape(image);
    }

    public void skinAvailable() {
        this.val$p_downloadCape_0_.setLocationOfCape(this.val$resourcelocation);
    }
}
