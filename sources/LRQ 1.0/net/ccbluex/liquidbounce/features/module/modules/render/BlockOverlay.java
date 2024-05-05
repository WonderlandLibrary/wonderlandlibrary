/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="BlockOverlay", description="Allows you to change the design of the block overlay.", category=ModuleCategory.RENDER)
public final class BlockOverlay
extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 68, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 117, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue infoValue = new BoolValue("Info", false);

    public final BoolValue getInfoValue() {
        return this.infoValue;
    }

    public final WBlockPos getCurrentBlock() {
        Object object = MinecraftInstance.mc.getObjectMouseOver();
        if (object == null || (object = object.getBlockPos()) == null) {
            return null;
        }
        Object blockPos = object;
        if (BlockUtils.canBeClicked((WBlockPos)blockPos)) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (iWorldClient.getWorldBorder().contains((WBlockPos)blockPos)) {
                return blockPos;
            }
        }
        return null;
    }

    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        WBlockPos wBlockPos = this.getCurrentBlock();
        if (wBlockPos == null) {
            return;
        }
        WBlockPos blockPos = wBlockPos;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IBlock block = iWorldClient.getBlockState(blockPos).getBlock();
        float partialTicks = event.getPartialTicks();
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow(0.4f) : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), (int)102.0f);
        MinecraftInstance.classProvider.getGlStateManager().enableBlend();
        MinecraftInstance.classProvider.getGlStateManager().tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.glColor(color);
        GL11.glLineWidth((float)2.0f);
        MinecraftInstance.classProvider.getGlStateManager().disableTexture2D();
        GL11.glDepthMask((boolean)false);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double x = thePlayer.getLastTickPosX() + (thePlayer.getPosX() - thePlayer.getLastTickPosX()) * (double)partialTicks;
        double y = thePlayer.getLastTickPosY() + (thePlayer.getPosY() - thePlayer.getLastTickPosY()) * (double)partialTicks;
        double z = thePlayer.getLastTickPosZ() + (thePlayer.getPosZ() - thePlayer.getLastTickPosZ()) * (double)partialTicks;
        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient2 == null) {
            Intrinsics.throwNpe();
        }
        IWorld iWorld = iWorldClient2;
        IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient3 == null) {
            Intrinsics.throwNpe();
        }
        IAxisAlignedBB axisAlignedBB = block.getSelectedBoundingBox(iWorld, iWorldClient3.getBlockState(blockPos), blockPos).expand(0.002f, 0.002f, 0.002f).offset(-x, -y, -z);
        RenderUtils.drawSelectionBoundingBox(axisAlignedBB);
        RenderUtils.drawFilledBox(axisAlignedBB);
        GL11.glDepthMask((boolean)true);
        MinecraftInstance.classProvider.getGlStateManager().enableTexture2D();
        MinecraftInstance.classProvider.getGlStateManager().disableBlend();
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        if (((Boolean)this.infoValue.get()).booleanValue()) {
            WBlockPos wBlockPos = this.getCurrentBlock();
            if (wBlockPos == null) {
                return;
            }
            WBlockPos blockPos = wBlockPos;
            IBlock iBlock = BlockUtils.getBlock(blockPos);
            if (iBlock == null) {
                return;
            }
            IBlock block = iBlock;
            String info = block.getLocalizedName() + " \u00a77ID: " + MinecraftInstance.functions.getIdFromBlock(block);
            IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.getScaledWidth() / 2) - 2.0f, (float)(scaledResolution.getScaledHeight() / 2) + 5.0f, (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info)) + 2.0f, (float)(scaledResolution.getScaledHeight() / 2) + 16.0f, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            Fonts.font40.drawString(info, (float)scaledResolution.getScaledWidth() / 2.0f, (float)scaledResolution.getScaledHeight() / 2.0f + 7.0f, Color.WHITE.getRGB(), false);
        }
    }
}

