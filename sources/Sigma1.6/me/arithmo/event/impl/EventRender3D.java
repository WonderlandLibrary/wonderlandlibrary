/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class EventRender3D
extends Event {
    private boolean offset;
    public static float renderPartialTicks;
    private int x;
    public static float partialTicks;
    private int y;
    private int z;
    private int ix;
    private int iy;
    private int iz;

    public void fire(float renderPartialTicks, int x, int y, int z) {
        this.renderPartialTicks = renderPartialTicks;
        partialTicks = EventRender3D.partialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ix = x;
        this.iy = y;
        this.iz = z;
        super.fire();
    }
    
    public static float getPartialTicks()
    {
      return partialTicks;
  }


    public boolean isOffset() {
        return this.offset;
    }

    public void offset(int renderOffsets) {
        double ox = Minecraft.getMinecraft().getRenderManager().getRenderPosX();
        double oy = Minecraft.getMinecraft().getRenderManager().getRenderPosY();
        double oz = Minecraft.getMinecraft().getRenderManager().getRenderPosZ();
        if (renderOffsets < 0) {
            GlStateManager.translate(- ox, - oy, - oz);
            this.x = (int)((double)this.x - ox);
            this.y = (int)((double)this.y - oy);
            this.z = (int)((double)this.z - oz);
            this.offset = true;
        } else if (renderOffsets > 0) {
            GlStateManager.translate(ox, oy, oz);
            this.x = (int)((double)this.x + ox);
            this.y = (int)((double)this.y + oy);
            this.z = (int)((double)this.z + oz);
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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}

