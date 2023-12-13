/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class CloudRenderer {
    private Minecraft mc;
    private boolean updated = false;
    private boolean renderFancy = false;
    int cloudTickCounter;
    float partialTicks;
    private int glListClouds = -1;
    private int cloudTickCounterUpdate = 0;
    private double cloudPlayerX = 0.0;
    private double cloudPlayerY = 0.0;
    private double cloudPlayerZ = 0.0;

    public CloudRenderer(Minecraft mc) {
        this.mc = mc;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }

    public void prepareToRender(boolean renderFancy, int cloudTickCounter, float partialTicks) {
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
        Entity rve = this.mc.func_175606_aa();
        boolean belowCloudsPrev = this.cloudPlayerY + (double)rve.getEyeHeight() < 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f);
        boolean belowClouds = rve.prevPosY + (double)rve.getEyeHeight() < 128.0 + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0f);
        return belowClouds ^ belowCloudsPrev;
    }

    public void startUpdateGlList() {
        GL11.glNewList((int)this.glListClouds, (int)4864);
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
        Entity entityliving = this.mc.func_175606_aa();
        double exactPlayerX = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)this.partialTicks;
        double exactPlayerY = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)this.partialTicks;
        double exactPlayerZ = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)this.partialTicks;
        double dc = (float)(this.cloudTickCounter - this.cloudTickCounterUpdate) + this.partialTicks;
        float cdx = (float)(exactPlayerX - this.cloudPlayerX + dc * 0.03);
        float cdy = (float)(exactPlayerY - this.cloudPlayerY);
        float cdz = (float)(exactPlayerZ - this.cloudPlayerZ);
        GlStateManager.pushMatrix();
        if (this.renderFancy) {
            GlStateManager.translate(-cdx / 12.0f, -cdy, -cdz / 12.0f);
        } else {
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

