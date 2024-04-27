package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="BlockOverlay", description="Allows you to change the design of the block overlay.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n08F¢\b\f\rR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/BlockOverlay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "colorBlueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorGreenValue", "colorRainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorRedValue", "colora", "currentBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getCurrentBlock", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "infoValue", "getInfoValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Pride"})
public final class BlockOverlay
extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 68, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 117, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue colora = new IntegerValue("A", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    @NotNull
    private final BoolValue infoValue = new BoolValue("Info", false);

    @NotNull
    public final BoolValue getInfoValue() {
        return this.infoValue;
    }

    @Nullable
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
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow(0.4f) : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colora.get()).intValue());
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
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.infoValue.get()).booleanValue()) {
            WBlockPos wBlockPos = this.getCurrentBlock();
            if (wBlockPos == null) {
                return;
            }
            WBlockPos blockPos = wBlockPos;
            boolean $i$f$getBlock = false;
            Object object = MinecraftInstance.mc.getTheWorld();
            IBlock iBlock = object != null && (object = object.getBlockState(blockPos)) != null ? object.getBlock() : null;
            if (iBlock == null) {
                return;
            }
            IBlock block = iBlock;
            String info = block.getLocalizedName() + " §7ID: " + MinecraftInstance.functions.getIdFromBlock(block);
            IMinecraft iMinecraft = MinecraftInstance.mc;
            Intrinsics.checkExpressionValueIsNotNull(iMinecraft, "mc");
            IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(iMinecraft);
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            float f = (float)scaledResolution.getScaledWidth() / 2.0f;
            float f2 = (float)scaledResolution.getScaledHeight() / 2.0f + 7.0f;
            Color color = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
            Fonts.font40.drawString(info, f, f2, color.getRGB(), false);
        }
    }
}
