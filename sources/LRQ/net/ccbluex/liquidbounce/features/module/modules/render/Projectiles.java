/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="Projectiles", description="Allows you to see where arrows will land.", category=ModuleCategory.RENDER)
public final class Projectiles
extends Module {
    private final ListValue colorMode = new ListValue("Color", new String[]{"Custom", "BowPower", "Rainbow"}, "Custom");
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            return;
        }
        IWorldClient theWorld = iWorldClient;
        IItemStack iItemStack = thePlayer.getHeldItem();
        if (iItemStack == null) {
            return;
        }
        IItemStack heldItem = iItemStack;
        IItem item = heldItem.getItem();
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        boolean isBow = false;
        float motionFactor = 1.5f;
        float motionSlowdown = 0.99f;
        float gravity = 0.0f;
        float size = 0.0f;
        if (MinecraftInstance.classProvider.isItemBow(item)) {
            if (!thePlayer.isUsingItem()) {
                return;
            }
            isBow = true;
            gravity = 0.05f;
            size = 0.3f;
            float power = (float)thePlayer.getItemInUseDuration() / 20.0f;
            if ((power = (power * power + power * 2.0f) / 3.0f) < 0.1f) {
                return;
            }
            if (power > 1.0f) {
                power = 1.0f;
            }
            motionFactor = power * 3.0f;
        } else if (MinecraftInstance.classProvider.isItemFishingRod(item)) {
            gravity = 0.04f;
            size = 0.25f;
            motionSlowdown = 0.92f;
        } else if (MinecraftInstance.classProvider.isItemPotion(item) && heldItem.isSplash()) {
            gravity = 0.05f;
            size = 0.25f;
            motionFactor = 0.5f;
        } else {
            if (!(MinecraftInstance.classProvider.isItemSnowball(item) || MinecraftInstance.classProvider.isItemEnderPearl(item) || MinecraftInstance.classProvider.isItemEgg(item))) {
                return;
            }
            gravity = 0.03f;
            size = 0.25f;
        }
        float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : thePlayer.getRotationYaw();
        float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : thePlayer.getRotationPitch();
        float yawRadians = yaw / 180.0f * (float)Math.PI;
        float pitchRadians = pitch / 180.0f * (float)Math.PI;
        double d = renderManager.getRenderPosX();
        boolean bl = false;
        float f = (float)Math.cos(yawRadians);
        double posX = d - (double)(f * 0.16f);
        double posY = renderManager.getRenderPosY() + (double)thePlayer.getEyeHeight() - (double)0.1f;
        d = renderManager.getRenderPosZ();
        boolean bl2 = false;
        f = (float)Math.sin(yawRadians);
        double posZ = d - (double)(f * 0.16f);
        boolean bl3 = false;
        float f2 = -((float)Math.sin(yawRadians));
        bl3 = false;
        float f3 = (float)Math.cos(pitchRadians);
        double motionX = (double)(f2 * f3) * (isBow ? 1.0 : 0.4);
        float f4 = (pitch + (float)(MinecraftInstance.classProvider.isItemPotion(item) && heldItem.isSplash() ? -20 : 0)) / 180.0f * (float)Math.PI;
        boolean bl4 = false;
        double motionY = (double)(-((float)Math.sin(f4))) * (isBow ? 1.0 : 0.4);
        boolean bl5 = false;
        f2 = (float)Math.cos(yawRadians);
        bl5 = false;
        f3 = (float)Math.cos(pitchRadians);
        double motionZ = (double)(f2 * f3) * (isBow ? 1.0 : 0.4);
        double d2 = motionX * motionX + motionY * motionY + motionZ * motionZ;
        boolean bl6 = false;
        double distance = Math.sqrt(d2);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= (double)motionFactor;
        motionY *= (double)motionFactor;
        motionZ *= (double)motionFactor;
        IMovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;
        ITessellator tessellator = MinecraftInstance.classProvider.getTessellatorInstance();
        IWorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glDepthMask((boolean)false);
        RenderUtils.enableGlCap(3042, 2848);
        RenderUtils.disableGlCap(2929, 3008, 3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        String string = (String)this.colorMode.get();
        boolean bl7 = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "custom": {
                RenderUtils.glColor(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 255));
                break;
            }
            case "bowpower": {
                RenderUtils.glColor(this.interpolateHSB(Color.RED, Color.GREEN, motionFactor / (float)30 * (float)10));
                break;
            }
            case "rainbow": {
                RenderUtils.glColor(ColorUtils.rainbow());
            }
        }
        GL11.glLineWidth((float)2.0f);
        worldRenderer.begin(3, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        while (!hasLanded && posY > 0.0) {
            WVec3 posBefore = new WVec3(posX, posY, posZ);
            WVec3 posAfter = new WVec3(posX + motionX, posY + motionY, posZ + motionZ);
            landingPosition = theWorld.rayTraceBlocks(posBefore, posAfter, false, true, false);
            posBefore = new WVec3(posX, posY, posZ);
            posAfter = new WVec3(posX + motionX, posY + motionY, posZ + motionZ);
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new WVec3(landingPosition.getHitVec().getXCoord(), landingPosition.getHitVec().getYCoord(), landingPosition.getHitVec().getZCoord());
            }
            IAxisAlignedBB arrowBox = MinecraftInstance.classProvider.createAxisAlignedBB(posX - (double)size, posY - (double)size, posZ - (double)size, posX + (double)size, posY + (double)size, posZ + (double)size).addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0);
            double d3 = (arrowBox.getMinX() - 2.0) / 16.0;
            boolean bl8 = false;
            int chunkMinX = (int)Math.floor(d3);
            double d4 = (arrowBox.getMaxX() + 2.0) / 16.0;
            boolean bl9 = false;
            int chunkMaxX = (int)Math.floor(d4);
            double d5 = (arrowBox.getMinZ() - 2.0) / 16.0;
            int n = 0;
            int chunkMinZ = (int)Math.floor(d5);
            double d6 = (arrowBox.getMaxZ() + 2.0) / 16.0;
            int n2 = 0;
            int chunkMaxZ = (int)Math.floor(d6);
            n = 0;
            List collidedEntities = new ArrayList();
            n = chunkMinX;
            n2 = chunkMaxX;
            if (n <= n2) {
                while (true) {
                    void x;
                    int n3;
                    int n4;
                    if ((n4 = chunkMinZ) <= (n3 = chunkMaxZ)) {
                        while (true) {
                            void z;
                            theWorld.getChunkFromChunkCoords((int)x, (int)z).getEntitiesWithinAABBForEntity(thePlayer, arrowBox, collidedEntities, null);
                            if (z == n3) break;
                            ++z;
                        }
                    }
                    if (x == n2) break;
                    ++x;
                }
            }
            for (IEntity possibleEntity : collidedEntities) {
                IMovingObjectPosition possibleEntityLanding;
                if (!possibleEntity.canBeCollidedWith() || !(possibleEntity.equals(thePlayer) ^ true)) continue;
                IAxisAlignedBB possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox().expand(size, size, size);
                if (possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter) == null) {
                    continue;
                }
                hitEntity = true;
                hasLanded = true;
                landingPosition = possibleEntityLanding;
            }
            IIBlockState blockState = theWorld.getBlockState(new WBlockPos(posX += motionX, posY += motionY, posZ += motionZ));
            if (blockState.getBlock().getMaterial(blockState).equals(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.WATER))) {
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else {
                motionX *= (double)motionSlowdown;
                motionY *= (double)motionSlowdown;
                motionZ *= (double)motionSlowdown;
            }
            motionY -= (double)gravity;
            worldRenderer.pos(posX - renderManager.getRenderPosX(), posY - renderManager.getRenderPosY(), posZ - renderManager.getRenderPosZ()).endVertex();
        }
        tessellator.draw();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(posX - renderManager.getRenderPosX()), (double)(posY - renderManager.getRenderPosY()), (double)(posZ - renderManager.getRenderPosZ()));
        if (landingPosition != null) {
            IEnumFacing iEnumFacing = landingPosition.getSideHit();
            if (iEnumFacing == null) {
                Intrinsics.throwNpe();
            }
            switch (iEnumFacing.getAxisOrdinal()) {
                case 0: {
                    GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    break;
                }
                case 2: {
                    GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
            }
            if (hitEntity) {
                RenderUtils.glColor(new Color(255, 0, 0, 150));
            }
        }
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(100011);
        cylinder.draw(0.2f, 0.0f, 0.0f, 60, 1);
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public final Color interpolateHSB(Color startColor, Color endColor, float process) {
        float[] startHSB = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
        float[] endHSB = Color.RGBtoHSB(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), null);
        float brightness = (startHSB[2] + endHSB[2]) / (float)2;
        float saturation = (startHSB[1] + endHSB[1]) / (float)2;
        float hueMax = startHSB[0] > endHSB[0] ? startHSB[0] : endHSB[0];
        float hueMin = startHSB[0] > endHSB[0] ? endHSB[0] : startHSB[0];
        float hue = (hueMax - hueMin) * process + hueMin;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}

