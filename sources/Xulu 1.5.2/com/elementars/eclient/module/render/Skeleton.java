// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.gui.Gui;
import java.util.function.Predicate;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.TargetPlayers;
import net.minecraft.entity.Entity;
import dev.xulu.newgui.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.friend.Friends;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.client.renderer.culling.Frustum;
import com.elementars.eclient.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import net.minecraft.client.renderer.culling.ICamera;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Skeleton extends Module
{
    private final Value<String> color;
    private final Value<Boolean> friends;
    private final Value<Float> width;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private ICamera camera;
    private static final HashMap<EntityPlayer, float[][]> entities;
    
    public Skeleton() {
        super("Skeleton", "Renders player entities skeletons", 0, Category.RENDER, true);
        this.color = this.register(new Value<String>("Color Mode", this, "White", new String[] { "White", "ClickGui", "Tracers", "Target", "Rainbow" }));
        this.friends = this.register(new Value<Boolean>("Friends", this, true));
        this.width = this.register(new Value<Float>("Width", this, 1.5f, 0.0f, 10.0f));
        this.red = this.register(new Value<Integer>("Target Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Target Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("TargetBlue", this, 0, 0, 255));
        this.camera = (ICamera)new Frustum();
    }
    
    private Vec3d getVec3(final RenderEvent event, final EntityPlayer e) {
        final float pt = event.getPartialTicks();
        final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * pt;
        final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * pt;
        final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pt;
        return new Vec3d(x, y, z);
    }
    
    private void glColor(final EntityPlayer entity) {
        if (Friends.isFriend(entity.getName()) && this.friends.getValue()) {
            GlStateManager.color(0.27f, 0.7f, 0.92f, 1.0f);
            return;
        }
        if (this.color.getValue().equalsIgnoreCase("ClickGui")) {
            GlStateManager.color(ColorUtil.getClickGUIColor().getRed() / 255.0f, ColorUtil.getClickGUIColor().getGreen() / 255.0f, ColorUtil.getClickGUIColor().getBlue() / 255.0f, 1.0f);
        }
        else if (this.color.getValue().equalsIgnoreCase("Tracers")) {
            final float distance = Skeleton.mc.player.getDistance((Entity)entity);
            if (distance <= 32.0f) {
                GlStateManager.color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f, 1.0f);
            }
            else {
                GlStateManager.color(0.0f, 0.9f, 0.0f, 1.0f);
            }
        }
        else if (this.color.getValue().equalsIgnoreCase("Target")) {
            if (TargetPlayers.targettedplayers.containsKey(entity.getName())) {
                GlStateManager.color(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, 1.0f);
            }
            else {
                GlStateManager.color(ColorUtil.getClickGUIColor().getRed() / 255.0f, ColorUtil.getClickGUIColor().getGreen() / 255.0f, ColorUtil.getClickGUIColor().getBlue() / 255.0f, 1.0f);
            }
        }
        else if (this.color.getValue().equalsIgnoreCase("Rainbow")) {
            final Color c = new Color(Xulu.rgb);
            GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);
        }
        else {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Skeleton.mc.getRenderManager() == null || Skeleton.mc.getRenderManager().options == null) {
            return;
        }
        this.startEnd(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        Skeleton.entities.keySet().removeIf(this::doesntContain);
        Skeleton.mc.world.playerEntities.forEach(e -> this.drawSkeleton(event, e));
        Gui.drawRect(0, 0, 0, 0, 0);
        this.startEnd(false);
    }
    
    private void drawSkeleton(final RenderEvent event, final EntityPlayer e) {
        final double d3 = Skeleton.mc.player.lastTickPosX + (Skeleton.mc.player.posX - Skeleton.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = Skeleton.mc.player.lastTickPosY + (Skeleton.mc.player.posY - Skeleton.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = Skeleton.mc.player.lastTickPosZ + (Skeleton.mc.player.posZ - Skeleton.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final float[][] entPos = Skeleton.entities.get(e);
        if (entPos != null && e.isEntityAlive() && this.camera.isBoundingBoxInFrustum(e.getEntityBoundingBox()) && !e.isDead && e != Skeleton.mc.player && !e.isPlayerSleeping()) {
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glLineWidth((float)this.width.getValue());
            this.glColor(e);
            final Vec3d vec = this.getVec3(event, e);
            final double x = vec.x - Skeleton.mc.getRenderManager().renderPosX;
            final double y = vec.y - Skeleton.mc.getRenderManager().renderPosY;
            final double z = vec.z - Skeleton.mc.getRenderManager().renderPosZ;
            GL11.glTranslated(x, y, z);
            final float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, e.isSneaking() ? -0.235 : 0.0);
            final float yOff = e.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(-0.125, (double)yOff, 0.0);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(0.125, (double)yOff, 0.0);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, 0.0, e.isSneaking() ? 0.25 : 0.0);
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(0.0, e.isSneaking() ? -0.05 : 0.0, e.isSneaking() ? -0.01725 : 0.0);
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(-0.375, yOff + 0.55, 0.0);
            if (entPos[1][0] != 0.0f) {
                GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[1][1] != 0.0f) {
                GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[1][2] != 0.0f) {
                GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375, yOff + 0.55, 0.0);
            if (entPos[2][0] != 0.0f) {
                GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[2][1] != 0.0f) {
                GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(xOff - e.rotationYawHead, 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(0.0, yOff + 0.55, 0.0);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(e.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, e.isSneaking() ? -0.16175 : 0.0, e.isSneaking() ? -0.48025 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double)yOff, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.125, 0.0, 0.0);
            GL11.glVertex3d(0.125, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.glColor(e);
            GL11.glTranslated(0.0, (double)yOff, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.55, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, yOff + 0.55, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375, 0.0, 0.0);
            GL11.glVertex3d(0.375, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
    
    private void startEnd(final boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
    
    public static void addEntity(final EntityPlayer e, final ModelPlayer model) {
        Skeleton.entities.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
    }
    
    private boolean doesntContain(final EntityPlayer var0) {
        return !Skeleton.mc.world.playerEntities.contains(var0);
    }
    
    static {
        entities = new HashMap<EntityPlayer, float[][]>();
    }
}
