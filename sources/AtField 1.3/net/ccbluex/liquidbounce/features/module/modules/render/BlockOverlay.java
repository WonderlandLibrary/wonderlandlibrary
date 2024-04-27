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
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
    private final IntegerValue colorBlueValue;
    private final BoolValue colorRainbow;
    private final IntegerValue colorRedValue = new IntegerValue("R", 68, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 117, 0, 255);
    private final BoolValue infoValue;

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        WBlockPos wBlockPos = this.getCurrentBlock();
        if (wBlockPos == null) {
            return;
        }
        WBlockPos wBlockPos2 = wBlockPos;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IBlock iBlock = iWorldClient.getBlockState(wBlockPos2).getBlock();
        float f = render3DEvent.getPartialTicks();
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
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        double d = iEntityPlayerSP2.getLastTickPosX() + (iEntityPlayerSP2.getPosX() - iEntityPlayerSP2.getLastTickPosX()) * (double)f;
        double d2 = iEntityPlayerSP2.getLastTickPosY() + (iEntityPlayerSP2.getPosY() - iEntityPlayerSP2.getLastTickPosY()) * (double)f;
        double d3 = iEntityPlayerSP2.getLastTickPosZ() + (iEntityPlayerSP2.getPosZ() - iEntityPlayerSP2.getLastTickPosZ()) * (double)f;
        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient2 == null) {
            Intrinsics.throwNpe();
        }
        IWorld iWorld = iWorldClient2;
        IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient3 == null) {
            Intrinsics.throwNpe();
        }
        IAxisAlignedBB iAxisAlignedBB = iBlock.getSelectedBoundingBox(iWorld, iWorldClient3.getBlockState(wBlockPos2), wBlockPos2).expand(0.002f, 0.002f, 0.002f).offset(-d, -d2, -d3);
        RenderUtils.drawSelectionBoundingBox(iAxisAlignedBB);
        RenderUtils.drawFilledBox(iAxisAlignedBB);
        GL11.glDepthMask((boolean)true);
        MinecraftInstance.classProvider.getGlStateManager().enableTexture2D();
        MinecraftInstance.classProvider.getGlStateManager().disableBlend();
        MinecraftInstance.classProvider.getGlStateManager().resetColor();
    }

    public final WBlockPos getCurrentBlock() {
        Object object = MinecraftInstance.mc.getObjectMouseOver();
        if (object == null || (object = object.getBlockPos()) == null) {
            return null;
        }
        Object object2 = object;
        if (BlockUtils.canBeClicked((WBlockPos)object2)) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (iWorldClient.getWorldBorder().contains((WBlockPos)object2)) {
                return object2;
            }
        }
        return null;
    }

    public final BoolValue getInfoValue() {
        return this.infoValue;
    }

    @EventTarget
    public final void onRender2D(Render2DEvent render2DEvent) {
        if (((Boolean)this.infoValue.get()).booleanValue()) {
            WBlockPos wBlockPos = this.getCurrentBlock();
            if (wBlockPos == null) {
                return;
            }
            WBlockPos wBlockPos2 = wBlockPos;
            IBlock iBlock = BlockUtils.getBlock(wBlockPos2);
            if (iBlock == null) {
                return;
            }
            IBlock iBlock2 = iBlock;
            String string = iBlock2.getLocalizedName() + " \u00a77ID: " + BlockOverlay.access$getFunctions$p$s1046033730().getIdFromBlock(iBlock2);
            IScaledResolution iScaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
            RenderUtils.drawBorderedRect((float)(iScaledResolution.getScaledWidth() / 2) - 2.0f, (float)(iScaledResolution.getScaledHeight() / 2) + 5.0f, (float)(iScaledResolution.getScaledWidth() / 2 + Fonts.roboto40.getStringWidth(string)) + 2.0f, (float)(iScaledResolution.getScaledHeight() / 2) + 16.0f, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            Fonts.roboto40.drawString(string, (float)iScaledResolution.getScaledWidth() / 2.0f, (float)iScaledResolution.getScaledHeight() / 2.0f + 7.0f, Color.WHITE.getRGB(), false);
        }
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public BlockOverlay() {
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.infoValue = new BoolValue("Info", false);
    }
}

