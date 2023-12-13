// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.util.function.Consumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Predicate;
import com.elementars.eclient.util.EntityUtil;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class EyeFinder extends Module
{
    private final Value<Boolean> players;
    private final Value<Boolean> mobs;
    private final Value<Boolean> animals;
    
    public EyeFinder() {
        super("EyeFinder", "Draws line at an entity's eyes", 0, Category.RENDER, true);
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.mobs = this.register(new Value<Boolean>("Mobs", this, false));
        this.animals = this.register(new Value<Boolean>("Animals", this, false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final boolean b;
        EyeFinder.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> EyeFinder.mc.player != entity).map(entity -> entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> {
            if (!this.players.getValue() || !(entity instanceof EntityPlayer)) {
                if (!(EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue())) {
                    return b;
                }
            }
            return b;
        }).forEach(this::drawLine);
    }
    
    private void drawLine(final EntityLivingBase e) {
        final RayTraceResult result = e.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        if (result == null) {
            return;
        }
        final Vec3d eyes = e.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());
        GlStateManager.enableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        final double posx = eyes.x - EyeFinder.mc.getRenderManager().renderPosX;
        final double posy = eyes.y - EyeFinder.mc.getRenderManager().renderPosY;
        final double posz = eyes.z - EyeFinder.mc.getRenderManager().renderPosZ;
        final double posx2 = result.hitVec.x - EyeFinder.mc.getRenderManager().renderPosX;
        final double posy2 = result.hitVec.y - EyeFinder.mc.getRenderManager().renderPosY;
        final double posz2 = result.hitVec.z - EyeFinder.mc.getRenderManager().renderPosZ;
        GL11.glColor4f(0.2f, 0.1f, 0.3f, 0.8f);
        GlStateManager.glLineWidth(1.5f);
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            XuluTessellator.prepare(7);
            GL11.glEnable(2929);
            final BlockPos b = result.getBlockPos();
            final float x = b.x - 0.01f;
            final float y = b.y - 0.01f;
            final float z = b.z - 0.01f;
            XuluTessellator.drawBox(XuluTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, 51, 25, 73, 200, 63);
            XuluTessellator.release();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }
}
