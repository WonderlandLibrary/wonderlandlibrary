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
    private final IntegerValue colorBlueValue;
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);

    public Projectiles() {
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
    }

    public final Color interpolateHSB(Color color, Color color2, float f) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] fArray2 = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
        float f2 = (fArray[2] + fArray2[2]) / (float)2;
        float f3 = (fArray[1] + fArray2[1]) / (float)2;
        float f4 = fArray[0] > fArray2[0] ? fArray[0] : fArray2[0];
        float f5 = fArray[0] > fArray2[0] ? fArray2[0] : fArray[0];
        float f6 = (f4 - f5) * f + f5;
        return Color.getHSBColor(f6, f3, f2);
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        float f;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            return;
        }
        IWorldClient iWorldClient2 = iWorldClient;
        IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
        if (iItemStack == null) {
            return;
        }
        IItemStack iItemStack2 = iItemStack;
        IItem iItem = iItemStack2.getItem();
        IRenderManager iRenderManager = MinecraftInstance.mc.getRenderManager();
        boolean bl = false;
        float f2 = 1.5f;
        float f3 = 0.99f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        if (MinecraftInstance.classProvider.isItemBow(iItem)) {
            if (!iEntityPlayerSP2.isUsingItem()) {
                return;
            }
            bl = true;
            f4 = 0.05f;
            f5 = 0.3f;
            f = (float)iEntityPlayerSP2.getItemInUseDuration() / 20.0f;
            if ((f = (f * f + f * 2.0f) / 3.0f) < 0.1f) {
                return;
            }
            if (f > 1.0f) {
                f = 1.0f;
            }
            f2 = f * 3.0f;
        } else if (MinecraftInstance.classProvider.isItemFishingRod(iItem)) {
            f4 = 0.04f;
            f5 = 0.25f;
            f3 = 0.92f;
        } else if (MinecraftInstance.classProvider.isItemPotion(iItem) && iItemStack2.isSplash()) {
            f4 = 0.05f;
            f5 = 0.25f;
            f2 = 0.5f;
        } else {
            if (!(MinecraftInstance.classProvider.isItemSnowball(iItem) || MinecraftInstance.classProvider.isItemEnderPearl(iItem) || MinecraftInstance.classProvider.isItemEgg(iItem))) {
                return;
            }
            f4 = 0.03f;
            f5 = 0.25f;
        }
        f = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : iEntityPlayerSP2.getRotationYaw();
        float f6 = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : iEntityPlayerSP2.getRotationPitch();
        float f7 = f / 180.0f * (float)Math.PI;
        float f8 = f6 / 180.0f * (float)Math.PI;
        double d = iRenderManager.getRenderPosX();
        boolean bl2 = false;
        float f9 = (float)Math.cos(f7);
        double d2 = d - (double)(f9 * 0.16f);
        double d3 = iRenderManager.getRenderPosY() + (double)iEntityPlayerSP2.getEyeHeight() - (double)0.1f;
        d = iRenderManager.getRenderPosZ();
        boolean bl3 = false;
        f9 = (float)Math.sin(f7);
        double d4 = d - (double)(f9 * 0.16f);
        boolean bl4 = false;
        float f10 = -((float)Math.sin(f7));
        bl4 = false;
        float f11 = (float)Math.cos(f8);
        double d5 = (double)(f10 * f11) * (bl ? 1.0 : 0.4);
        float f12 = (f6 + (float)(MinecraftInstance.classProvider.isItemPotion(iItem) && iItemStack2.isSplash() ? -20 : 0)) / 180.0f * (float)Math.PI;
        boolean bl5 = false;
        double d6 = (double)(-((float)Math.sin(f12))) * (bl ? 1.0 : 0.4);
        boolean bl6 = false;
        f10 = (float)Math.cos(f7);
        bl6 = false;
        f11 = (float)Math.cos(f8);
        double d7 = (double)(f10 * f11) * (bl ? 1.0 : 0.4);
        double d8 = d5 * d5 + d6 * d6 + d7 * d7;
        boolean bl7 = false;
        double d9 = Math.sqrt(d8);
        d5 /= d9;
        d6 /= d9;
        d7 /= d9;
        d5 *= (double)f2;
        d6 *= (double)f2;
        d7 *= (double)f2;
        IMovingObjectPosition iMovingObjectPosition = null;
        boolean bl8 = false;
        bl7 = false;
        ITessellator iTessellator = MinecraftInstance.classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        GL11.glDepthMask((boolean)false);
        RenderUtils.enableGlCap(3042, 2848);
        RenderUtils.disableGlCap(2929, 3008, 3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        Object object = (String)this.colorMode.get();
        boolean bl9 = false;
        String string = object;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string.toLowerCase()) {
            case "custom": {
                RenderUtils.glColor(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 255));
                break;
            }
            case "bowpower": {
                RenderUtils.glColor(this.interpolateHSB(Color.RED, Color.GREEN, f2 / (float)30 * (float)10));
                break;
            }
            case "rainbow": {
                RenderUtils.glColor(ColorUtils.rainbow());
                break;
            }
        }
        GL11.glLineWidth((float)2.0f);
        iWorldRenderer.begin(3, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        while (!bl8 && d3 > 0.0) {
            object = new WVec3(d2, d3, d4);
            WVec3 wVec3 = new WVec3(d2 + d5, d3 + d6, d4 + d7);
            iMovingObjectPosition = iWorldClient2.rayTraceBlocks((WVec3)object, wVec3, false, true, false);
            object = new WVec3(d2, d3, d4);
            wVec3 = new WVec3(d2 + d5, d3 + d6, d4 + d7);
            if (iMovingObjectPosition != null) {
                bl8 = true;
                wVec3 = new WVec3(iMovingObjectPosition.getHitVec().getXCoord(), iMovingObjectPosition.getHitVec().getYCoord(), iMovingObjectPosition.getHitVec().getZCoord());
            }
            IAxisAlignedBB iAxisAlignedBB = MinecraftInstance.classProvider.createAxisAlignedBB(d2 - (double)f5, d3 - (double)f5, d4 - (double)f5, d2 + (double)f5, d3 + (double)f5, d4 + (double)f5).addCoord(d5, d6, d7).expand(1.0, 1.0, 1.0);
            double d10 = (iAxisAlignedBB.getMinX() - 2.0) / 16.0;
            int n = 0;
            int n2 = (int)Math.floor(d10);
            double d11 = (iAxisAlignedBB.getMaxX() + 2.0) / 16.0;
            boolean bl10 = false;
            int n3 = (int)Math.floor(d11);
            double d12 = (iAxisAlignedBB.getMinZ() - 2.0) / 16.0;
            int n4 = 0;
            int n5 = (int)Math.floor(d12);
            double d13 = (iAxisAlignedBB.getMaxZ() + 2.0) / 16.0;
            int n6 = 0;
            n = (int)Math.floor(d13);
            n4 = 0;
            List list = new ArrayList();
            n4 = n2;
            n6 = n3;
            if (n4 <= n6) {
                while (true) {
                    int n7;
                    int n8;
                    if ((n8 = n5) <= (n7 = n)) {
                        while (true) {
                            iWorldClient2.getChunkFromChunkCoords(n4, n8).getEntitiesWithinAABBForEntity(iEntityPlayerSP2, iAxisAlignedBB, list, null);
                            if (n8 == n7) break;
                            ++n8;
                        }
                    }
                    if (n4 == n6) break;
                    ++n4;
                }
            }
            for (IEntity iEntity : list) {
                IMovingObjectPosition iMovingObjectPosition2;
                if (!iEntity.canBeCollidedWith() || !(iEntity.equals(iEntityPlayerSP2) ^ true)) continue;
                IAxisAlignedBB iAxisAlignedBB2 = iEntity.getEntityBoundingBox().expand(f5, f5, f5);
                if (iAxisAlignedBB2.calculateIntercept((WVec3)object, wVec3) == null) {
                    continue;
                }
                bl7 = true;
                bl8 = true;
                iMovingObjectPosition = iMovingObjectPosition2;
            }
            IIBlockState iIBlockState = iWorldClient2.getBlockState(new WBlockPos(d2 += d5, d3 += d6, d4 += d7));
            if (iIBlockState.getBlock().getMaterial(iIBlockState).equals(MinecraftInstance.classProvider.getMaterialEnum(MaterialType.WATER))) {
                d5 *= 0.6;
                d6 *= 0.6;
                d7 *= 0.6;
            } else {
                d5 *= (double)f3;
                d6 *= (double)f3;
                d7 *= (double)f3;
            }
            d6 -= (double)f4;
            iWorldRenderer.pos(d2 - iRenderManager.getRenderPosX(), d3 - iRenderManager.getRenderPosY(), d4 - iRenderManager.getRenderPosZ()).endVertex();
        }
        iTessellator.draw();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(d2 - iRenderManager.getRenderPosX()), (double)(d3 - iRenderManager.getRenderPosY()), (double)(d4 - iRenderManager.getRenderPosZ()));
        if (iMovingObjectPosition != null) {
            IEnumFacing iEnumFacing = iMovingObjectPosition.getSideHit();
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
                    break;
                }
            }
            if (bl7) {
                RenderUtils.glColor(new Color(255, 0, 0, 150));
            }
        }
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        object = new Cylinder();
        object.setDrawStyle(100011);
        object.draw(0.2f, 0.0f, 0.0f, 60, 1);
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

