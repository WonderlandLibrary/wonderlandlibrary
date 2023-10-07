// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import exhibition.event.Event;

public class EventRender3D extends Event
{
    private boolean offset;
    public float renderPartialTicks;
    private int x;
    private int y;
    private int z;
    private int ix;
    private int iy;
    private int iz;
    
    public void fire(final float renderPartialTicks, final int x, final int y, final int z) {
        this.renderPartialTicks = renderPartialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ix = x;
        this.iy = y;
        this.iz = z;
        super.fire();
    }
    
    public boolean isOffset() {
        return this.offset;
    }
    
    public void offset(final int renderOffsets) {
        final double ox = Minecraft.getMinecraft().getRenderManager().getRenderPosX();
        final double oy = Minecraft.getMinecraft().getRenderManager().getRenderPosY();
        final double oz = Minecraft.getMinecraft().getRenderManager().getRenderPosZ();
        if (renderOffsets < 0) {
            GlStateManager.translate(-ox, -oy, -oz);
            this.x -= (int)ox;
            this.y -= (int)oy;
            this.z -= (int)oz;
            this.offset = true;
        }
        else if (renderOffsets > 0) {
            GlStateManager.translate(ox, oy, oz);
            this.x += (int)ox;
            this.y += (int)oy;
            this.z += (int)oz;
            this.offset = true;
        }
    }
    
    public void reset() {
        this.x = this.ix;
        this.y = this.iy;
        this.z = this.iz;
        this.offset = false;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public void setZ(final int z) {
        this.z = z;
    }
}
