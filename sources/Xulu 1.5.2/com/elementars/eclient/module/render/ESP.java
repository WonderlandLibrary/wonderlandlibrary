// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Predicate;
import com.elementars.eclient.util.EntityUtil;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ESP extends Module
{
    private final Value<Boolean> players;
    private final Value<Boolean> animals;
    private final Value<Boolean> mobs;
    
    public ESP() {
        super("ESP", "Highlights entities", 0, Category.RENDER, true);
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.animals = this.register(new Value<Boolean>("Animals", this, false));
        this.mobs = this.register(new Value<Boolean>("Mobs", this, false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Wrapper.getMinecraft().getRenderManager().options == null) {
            return;
        }
        final boolean isThirdPersonFrontal = Wrapper.getMinecraft().getRenderManager().options.thirdPersonView == 2;
        final float viewerYaw = Wrapper.getMinecraft().getRenderManager().playerViewY;
        final boolean b;
        final Vec3d pos;
        final float n;
        final boolean b2;
        ESP.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> ESP.mc.player != entity).map(entity -> entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> {
            if (!this.players.getValue() || !(entity instanceof EntityPlayer)) {
                if (!(EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue())) {
                    return b;
                }
            }
            return b;
        }).forEach(e -> {
            GlStateManager.pushMatrix();
            pos = EntityUtil.getInterpolatedPos((Entity)e, event.getPartialTicks());
            GlStateManager.translate(pos.x - ESP.mc.getRenderManager().renderPosX, pos.y - ESP.mc.getRenderManager().renderPosY, pos.z - ESP.mc.getRenderManager().renderPosZ);
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-n, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)(b2 ? -1 : 1), 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (e instanceof EntityPlayer) {
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
            }
            else if (EntityUtil.isPassive((Entity)e)) {
                GL11.glColor3f(0.11f, 0.9f, 0.11f);
            }
            else {
                GL11.glColor3f(0.9f, 0.1f, 0.1f);
            }
            GlStateManager.disableTexture2D();
            GL11.glLineWidth(2.0f);
            GL11.glEnable(2848);
            GL11.glBegin(2);
            GL11.glVertex2d((double)(-e.width / 2.0f), 0.0);
            GL11.glVertex2d((double)(-e.width / 2.0f), (double)e.height);
            GL11.glVertex2d((double)(e.width / 2.0f), (double)e.height);
            GL11.glVertex2d((double)(e.width / 2.0f), 0.0);
            GL11.glEnd();
            GlStateManager.popMatrix();
            return;
        });
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.enableCull();
        GlStateManager.glLineWidth(1.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }
}
