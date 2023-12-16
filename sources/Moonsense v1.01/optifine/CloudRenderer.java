// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.Minecraft;

public class CloudRenderer
{
    private Minecraft mc;
    private boolean updated;
    private boolean renderFancy;
    int cloudTickCounter;
    float partialTicks;
    private int glListClouds;
    private int cloudTickCounterUpdate;
    private double cloudPlayerX;
    private double cloudPlayerY;
    private double cloudPlayerZ;
    
    public CloudRenderer(final Minecraft mc) {
        this.updated = false;
        this.renderFancy = false;
        this.glListClouds = -1;
        this.cloudTickCounterUpdate = 0;
        this.cloudPlayerX = 0.0;
        this.cloudPlayerY = 0.0;
        this.cloudPlayerZ = 0.0;
        this.mc = mc;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }
    
    public void prepareToRender(final boolean renderFancy, final int cloudTickCounter, final float partialTicks) {
        if (this.renderFancy != renderFancy) {
            this.updated = false;
        }
        this.renderFancy = renderFancy;
        this.cloudTickCounter = cloudTickCounter;
        this.partialTicks = partialTicks;
    }
    
    public boolean shouldUpdateGlList() {
        if (!this.updated) {
            return true;
        }
        if (this.cloudTickCounter >= this.cloudTickCounterUpdate + 20) {
            return true;
        }
        final Entity rve = this.mc.func_175606_aa();
        final boolean belowCloudsPrev = this.cloudPlayerY + rve.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final boolean belowClouds = rve.prevPosY + rve.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        return belowClouds ^ belowCloudsPrev;
    }
    
    public void startUpdateGlList() {
        GL11.glNewList(this.glListClouds, 4864);
    }
    
    public void endUpdateGlList() {
        GL11.glEndList();
        this.cloudTickCounterUpdate = this.cloudTickCounter;
        this.cloudPlayerX = this.mc.func_175606_aa().prevPosX;
        this.cloudPlayerY = this.mc.func_175606_aa().prevPosY;
        this.cloudPlayerZ = this.mc.func_175606_aa().prevPosZ;
        this.updated = true;
        GlStateManager.func_179117_G();
    }
    
    public void renderGlList() {
        final Entity entityliving = this.mc.func_175606_aa();
        final double exactPlayerX = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * this.partialTicks;
        final double exactPlayerY = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * this.partialTicks;
        final double exactPlayerZ = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * this.partialTicks;
        final double dc = this.cloudTickCounter - this.cloudTickCounterUpdate + this.partialTicks;
        final float cdx = (float)(exactPlayerX - this.cloudPlayerX + dc * 0.03);
        final float cdy = (float)(exactPlayerY - this.cloudPlayerY);
        final float cdz = (float)(exactPlayerZ - this.cloudPlayerZ);
        GlStateManager.pushMatrix();
        if (this.renderFancy) {
            GlStateManager.translate(-cdx / 12.0f, -cdy, -cdz / 12.0f);
        }
        else {
            GlStateManager.translate(-cdx, -cdy, -cdz);
        }
        GlStateManager.callList(this.glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.func_179117_G();
    }
    
    public void reset() {
        this.updated = false;
    }
}
