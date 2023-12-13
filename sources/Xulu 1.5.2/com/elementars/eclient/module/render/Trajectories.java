// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import org.lwjgl.util.glu.Cylinder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Trajectories extends Module
{
    private Value<Float> scale;
    private Value<Boolean> rainbow;
    private Value<Integer> red;
    private Value<Integer> green;
    private Value<Integer> blue;
    private Value<Integer> redC;
    private Value<Integer> greenC;
    private Value<Integer> blueC;
    
    public Trajectories() {
        super("Trajectories", "Ingrosware trajectories", 0, Category.RENDER, true);
        this.scale = this.register(new Value<Float>("Scale", this, 1.0f, 1.0f, 2.0f));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.red = this.register(new Value<Integer>("Red", this, 0, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 255, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.redC = this.register(new Value<Integer>("Charge Red", this, 204, 0, 255));
        this.greenC = this.register(new Value<Integer>("Charge Green", this, 127, 0, 255));
        this.blueC = this.register(new Value<Integer>("Charge Blue", this, 0, 0, 255));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color rainbowColor = new Color(Xulu.rgb);
        if (Trajectories.mc.world != null && Trajectories.mc.player != null) {
            final double renderPosX = Trajectories.mc.player.lastTickPosX + (Trajectories.mc.player.posX - Trajectories.mc.player.lastTickPosX) * event.getPartialTicks();
            final double renderPosY = Trajectories.mc.player.lastTickPosY + (Trajectories.mc.player.posY - Trajectories.mc.player.lastTickPosY) * event.getPartialTicks();
            final double renderPosZ = Trajectories.mc.player.lastTickPosZ + (Trajectories.mc.player.posZ - Trajectories.mc.player.lastTickPosZ) * event.getPartialTicks();
            Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND);
            if (Trajectories.mc.gameSettings.thirdPersonView == 0 && (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle)) {
                GL11.glPushMatrix();
                final Item item = Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
                double posX = renderPosX - MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
                double posY = renderPosY + Trajectories.mc.player.getEyeHeight() - 0.1000000014901161;
                double posZ = renderPosZ - MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
                double motionX = -MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                double motionY = -MathHelper.sin(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                double motionZ = MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                final int var6 = 72000 - Trajectories.mc.player.getItemInUseCount();
                float power = var6 / 20.0f;
                power = (power * power + power * 2.0f) / 3.0f;
                if (power > 1.0f) {
                    power = 1.0f;
                }
                final float distance = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                motionX /= distance;
                motionY /= distance;
                motionZ /= distance;
                final float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.9f : 1.0f));
                motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                this.enableGL3D(2.0f);
                if (this.rainbow.getValue()) {
                    GlStateManager.color(rainbowColor.getRed() / 255.0f, rainbowColor.getGreen() / 255.0f, rainbowColor.getBlue() / 255.0f, 1.0f);
                }
                else if (power > 0.6f) {
                    GlStateManager.color(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, 1.0f);
                }
                else {
                    GlStateManager.color(this.redC.getValue() / 255.0f, this.greenC.getValue() / 255.0f, this.blueC.getValue() / 255.0f, 1.0f);
                }
                GL11.glEnable(2848);
                final float size = (float)((item instanceof ItemBow) ? 0.3 : 0.25);
                boolean hasLanded = false;
                Entity landingOnEntity = null;
                RayTraceResult landingPosition = null;
                while (!hasLanded && posY > 0.0) {
                    final Vec3d present = new Vec3d(posX, posY, posZ);
                    final Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
                    final RayTraceResult possibleLandingStrip = Trajectories.mc.world.rayTraceBlocks(present, future, false, true, false);
                    if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != RayTraceResult.Type.MISS) {
                        landingPosition = possibleLandingStrip;
                        hasLanded = true;
                    }
                    final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size);
                    final List entities = this.getEntitiesWithinAABB(arrowBox.offset(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
                    for (final Object entity : entities) {
                        final Entity boundingBox = (Entity)entity;
                        if (boundingBox.canBeCollidedWith() && boundingBox != Trajectories.mc.player) {
                            final float var8 = 0.3f;
                            final AxisAlignedBB var9 = boundingBox.getEntityBoundingBox().expand((double)var8, (double)var8, (double)var8);
                            final RayTraceResult possibleEntityLanding = var9.calculateIntercept(present, future);
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
                    final float motionAdjustment = 0.99f;
                    motionX *= motionAdjustment;
                    motionY *= motionAdjustment;
                    motionZ *= motionAdjustment;
                    motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
                    this.drawLine3D(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
                }
                if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
                    final int side = landingPosition.sideHit.getIndex();
                    if (side == 2) {
                        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    }
                    else if (side == 3) {
                        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    }
                    else if (side == 4) {
                        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                    }
                    else if (side == 5) {
                        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                    }
                    final Cylinder c = new Cylinder();
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale((float)this.scale.getValue(), (float)this.scale.getValue(), (float)this.scale.getValue());
                    c.setDrawStyle(100011);
                    if (landingOnEntity != null) {
                        GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0f);
                        GL11.glLineWidth(2.5f);
                        c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                        GL11.glLineWidth(0.1f);
                        if (this.rainbow.getValue()) {
                            GlStateManager.color(rainbowColor.getRed() / 255.0f, rainbowColor.getGreen() / 255.0f, rainbowColor.getBlue() / 255.0f, 1.0f);
                        }
                        else {
                            GlStateManager.color(this.red.getValue() / 255.0f, this.green.getValue() / 255.0f, this.blue.getValue() / 255.0f, 1.0f);
                        }
                    }
                    c.draw(0.6f, 0.3f, 0.0f, 4, 1);
                }
                this.disableGL3D();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
    
    public void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Trajectories.mc.entityRenderer.disableLightmap();
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
    
    public void drawLine3D(final double var1, final double var2, final double var3) {
        GL11.glVertex3d(var1, var2, var3);
    }
    
    private List getEntitiesWithinAABB(final AxisAlignedBB bb) {
        final ArrayList list = new ArrayList();
        final int chunkMinX = MathHelper.floor((bb.minX - 2.0) / 16.0);
        final int chunkMaxX = MathHelper.floor((bb.maxX + 2.0) / 16.0);
        final int chunkMinZ = MathHelper.floor((bb.minZ - 2.0) / 16.0);
        final int chunkMaxZ = MathHelper.floor((bb.maxZ + 2.0) / 16.0);
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.world.getChunkProvider().getLoadedChunk(x, z) != null) {
                    Trajectories.mc.world.getChunk(x, z).getEntitiesWithinAABBForEntity((Entity)Trajectories.mc.player, bb, (List)list, (Predicate)null);
                }
            }
        }
        return list;
    }
}
