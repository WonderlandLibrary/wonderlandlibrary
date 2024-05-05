package net.optifine.player;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;

public class CapeImageBuffer extends ImageBufferDownload
{
    public ImageBufferDownload imageBufferDownload;
    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;

    public CapeImageBuffer(AbstractClientPlayer player, ResourceLocation resourceLocation) {
        playerRef = new WeakReference<>(player);
        this.resourceLocation = resourceLocation;
        imageBufferDownload = new ImageBufferDownload();
    }

    public BufferedImage parseUserSkin(BufferedImage imageRaw)
    {
        return CapeUtils.parseCape(imageRaw);
    }

    public void skinAvailable()
    {
        AbstractClientPlayer player = playerRef.get();
        if (player != null)
            player.setLocationOfCape(this.resourceLocation);
    }
}
