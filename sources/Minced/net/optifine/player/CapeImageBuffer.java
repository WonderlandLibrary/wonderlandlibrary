// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;

public class CapeImageBuffer extends ImageBufferDownload
{
    private AbstractClientPlayer player;
    private ResourceLocation resourceLocation;
    private boolean elytraOfCape;
    
    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.player = player;
        this.resourceLocation = resourceLocation;
    }
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage imageRaw) {
        final BufferedImage bufferedimage = CapeUtils.parseCape(imageRaw);
        this.elytraOfCape = CapeUtils.isElytraCape(imageRaw, bufferedimage);
        return bufferedimage;
    }
    
    @Override
    public void skinAvailable() {
        if (this.player != null) {
            this.player.setLocationOfCape(this.resourceLocation);
            this.player.setElytraOfCape(this.elytraOfCape);
        }
        this.cleanup();
    }
    
    public void cleanup() {
        this.player = null;
    }
    
    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }
}
