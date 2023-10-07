package com.ohare.client.module.modules.visuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import com.ohare.client.event.events.render.Render3DEvent;
import com.ohare.client.module.Module;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * made by oHare for oHareWare
 *
 * @since 7/23/2019
 **/
public class Trajectories extends Module {
    public Trajectories() {
        super("Trajectories", Category.VISUALS, new Color(0xF38075).getRGB());
        setDescription("Show the path of your arrow.");
    }

    @Subscribe
    public void onRender(Render3DEvent event) {

        double renderPosX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.getPartialTicks();
        double renderPosY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.getPartialTicks();
        double renderPosZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.getPartialTicks();
        if (mc.thePlayer.getHeldItem() == null || mc.gameSettings.thirdPersonView != 0) {
            return;
        }
        if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
            return;
        }
        GL11.glPushMatrix();
        Item item = mc.thePlayer.getHeldItem().getItem();
        double posX = renderPosX - MathHelper.cos(mc.thePlayer.lastReportedYaw / 180.0f * 3.1415927f) * 0.16f;
        double posY = renderPosY + mc.thePlayer.getEyeHeight() - 0.1000000014901161;
        double posZ = renderPosZ - MathHelper.sin(mc.thePlayer.lastReportedYaw / 180.0f * 3.1415927f) * 0.16f;
        double motionX = -MathHelper.sin(mc.thePlayer.lastReportedYaw / 180.0f * 3.1415927f) * MathHelper.cos(mc.thePlayer.lastReportedPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionY = -MathHelper.sin(mc.thePlayer.lastReportedPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        double motionZ = MathHelper.cos(mc.thePlayer.lastReportedYaw / 180.0f * 3.1415927f) * MathHelper.cos(mc.thePlayer.lastReportedPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
        int var6 = 72000 - mc.thePlayer.getItemInUseCount();
        float power = var6 / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((mc.thePlayer.getHeldItem().getItem() == Items.experience_bottle) ? 0.9f : 1.0f));
        motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((mc.thePlayer.getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((mc.thePlayer.getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((mc.thePlayer.getHeldItem().getItem() == Items.experience_bottle) ? 0.75f : 1.5f));
        enableGL3D(2);
        if (power > 0.6f) {
            GlStateManager.color(0.0f, 1.0f, 0.0f, 1.0f);
        } else {
            GlStateManager.color(0.8f, 0.5f, 0.0f, 1.0f);
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        float size = (float) ((item instanceof ItemBow) ? 0.3 : 0.25);
        boolean hasLanded = false;
        Entity landingOnEntity = null;
        MovingObjectPosition landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            Vec3 present = new Vec3(posX, posY, posZ);
            Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition possibleLandingStrip = mc.theWorld.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                landingPosition = possibleLandingStrip;
                hasLanded = true;
            }
            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
            List entities = this.getEntitiesWithinAABB(arrowBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
            for (Object entity : entities) {
                Entity boundingBox = (Entity) entity;
                if (boundingBox.canBeCollidedWith() && boundingBox != mc.thePlayer) {
                    float var7 = 0.3f;
                    AxisAlignedBB var8 = boundingBox.getEntityBoundingBox().expand(var7, var7, var7);
                    MovingObjectPosition possibleEntityLanding = var8.calculateIntercept(present, future);
                    if (possibleEntityLanding == null) {
                        continue;
                    }
                    hasLanded = true;
                    landingOnEntity = boundingBox;
                    landingPosition = possibleEntityLanding;
                }
            }
            if (landingOnEntity != null) {
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float motionAdjustment = 0.99f;
            motionX *= motionAdjustment;
            motionY *= motionAdjustment;
            motionZ *= motionAdjustment;
            motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
            drawLine3D(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
        }
        if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
            int side = landingPosition.sideHit.getIndex();
            if (side == 2) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else if (side == 3) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else if (side == 4) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else if (side == 5) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            Cylinder c = new Cylinder();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            c.setDrawStyle(100011);
            if (landingOnEntity != null) {
                GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0f);
                GL11.glLineWidth(2.5f);
                c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                GL11.glLineWidth(0.1f);
                GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
            }
            c.draw(0.6f, 0.3f, 0.0f, 4, 1);
        }
        disableGL3D();
        GL11.glPopMatrix();
    }
    public void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public void drawLine3D(double var1, double var2, double var3) {
        GL11.glVertex3d(var1, var2, var3);
    }
    private List getEntitiesWithinAABB(AxisAlignedBB bb) {
        ArrayList list = new ArrayList();
        int chunkMinX = MathHelper.floor_double((bb.minX - 2.0) / 16.0);
        int chunkMaxX = MathHelper.floor_double((bb.maxX + 2.0) / 16.0);
        int chunkMinZ = MathHelper.floor_double((bb.minZ - 2.0) / 16.0);
        int chunkMaxZ = MathHelper.floor_double((bb.maxZ + 2.0) / 16.0);
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (mc.theWorld.getChunkProvider().chunkExists(x, z)) {
                    mc.theWorld.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity(mc.thePlayer, bb, list, null);
                }
            }
        }
        return list;
    }
}


