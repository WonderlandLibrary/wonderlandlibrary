/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.module.render;

import java.util.Iterator;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event3D;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    @EventTarget
    public void onRender3D(Event3D event3D) {
        boolean bl = Minecraft.gameSettings.viewBobbing;
        Iterator iterator = Minecraft.theWorld.loadedEntityList.iterator();
        while (true) {
            EntityLivingBase entityLivingBase;
            if (!iterator.hasNext()) {
                Minecraft.gameSettings.viewBobbing = bl;
                return;
            }
            Object e = iterator.next();
            if (!(e instanceof EntityLivingBase) || (entityLivingBase = (EntityLivingBase)e) == Minecraft.thePlayer || !RenderUtils.isValidForTracers(entityLivingBase)) continue;
            double d = entityLivingBase.lastTickPosX;
            double d2 = entityLivingBase.posX;
            float f = event3D.getPartialTicks();
            mc.getRenderManager();
            double d3 = this.interpolate(d, d2, f, RenderManager.renderPosX);
            d = entityLivingBase.lastTickPosY;
            d2 = entityLivingBase.posY;
            f = event3D.getPartialTicks();
            mc.getRenderManager();
            double d4 = this.interpolate(d, d2, f, RenderManager.renderPosY);
            d = entityLivingBase.lastTickPosZ;
            d2 = entityLivingBase.posZ;
            f = event3D.getPartialTicks();
            mc.getRenderManager();
            double d5 = this.interpolate(d, d2, f, RenderManager.renderPosZ);
            if (!(entityLivingBase instanceof EntityPlayer) || !(entityLivingBase instanceof EntityPlayer)) continue;
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            Minecraft.gameSettings.viewBobbing = false;
            Tracers.mc.entityRenderer.orientCamera(event3D.partialTicks);
            RenderUtils.enableGL3D(2.1f);
            float f2 = Minecraft.thePlayer.getDistanceToEntity(entityLivingBase);
            GlStateManager.color(0.0f, 0.9f, 0.0f, 0.7f);
            GL11.glBegin((int)1);
            Minecraft.getMinecraft();
            GL11.glVertex3d((double)0.0, (double)Minecraft.thePlayer.getEyeHeight(), (double)0.0);
            GL11.glVertex3d((double)d3, (double)(d4 + 1.0), (double)d5);
            GL11.glEnd();
            RenderUtils.disableGL3D();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Tracers() {
        super("Tracers", 0, Category.RENDER, "");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    private double interpolate(double d, double d2, float f, double d3) {
        return d + (d2 - d) * (double)f - d3;
    }
}

