package net.optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;

public class CapeImageBuffer extends ImageBufferDownload {

    public ImageBufferDownload imageBufferDownload;
    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;

    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.playerRef = new WeakReference<>(player);
        this.resourceLocation = resourceLocation;
        this.imageBufferDownload = new ImageBufferDownload();
    }

    public BufferedImage parseUserSkin(final BufferedImage image) {
        return CapeUtils.parseCape(image);
    }

    @Nullable
    private static BufferedImage parseCape(final BufferedImage image) {
        return null;
    }

    public void skinAvailable() {
        final AbstractClientPlayer player = this.playerRef.get();

        if (player != null) {
            setLocationOfCape(player, this.resourceLocation);
        }
    }

    private static void setLocationOfCape(@NonNull final AbstractClientPlayer player, final ResourceLocation capeResourceLocation) {
        player.setLocationOfCape(capeResourceLocation);
    }

    /*@Nullable private AbstractClientPlayer player;
    @Nullable private ResourceLocation resourceLocation;

    public CapeImageBuffer(@Nullable final AbstractClientPlayer player, @Nullable final ResourceLocation resourceLocation) {
        this.player = player;
        this.resourceLocation = resourceLocation;
    }

    @NonNull
    public BufferedImage parseUserSkin(@NonNull BufferedImage image) {
        return CapeUtils.parseCape(image);
    }

    public void skinAvailable() {
        if(this.player != null) {
            this.player.setLocationOfCape(this.resourceLocation);
        }
    }

    public void cleanup() {
        this.player = null;
    }*/

}
