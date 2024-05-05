/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="BlockOverlay", description="Allows you to change the design of the block overlay.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\t\u001a\u0004\u0018\u00010\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/BlockOverlay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "colorBlueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorGreenValue", "colorRainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorRedValue", "currentBlock", "Lnet/minecraft/util/BlockPos;", "getCurrentBlock", "()Lnet/minecraft/util/BlockPos;", "infoValue", "getInfoValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "KyinoClient"})
public final class BlockOverlay
extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 68, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 117, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    @NotNull
    private final BoolValue infoValue = new BoolValue("Info", false);

    @NotNull
    public final BoolValue getInfoValue() {
        return this.infoValue;
    }

    @Nullable
    public final BlockPos getCurrentBlock() {
        MovingObjectPosition movingObjectPosition = BlockOverlay.access$getMc$p$s1046033730().field_71476_x;
        if (movingObjectPosition == null || (movingObjectPosition = movingObjectPosition.func_178782_a()) == null) {
            return null;
        }
        MovingObjectPosition blockPos = movingObjectPosition;
        if (BlockUtils.canBeClicked((BlockPos)blockPos)) {
            WorldClient worldClient = BlockOverlay.access$getMc$p$s1046033730().field_71441_e;
            Intrinsics.checkExpressionValueIsNotNull(worldClient, "mc.theWorld");
            if (worldClient.func_175723_af().func_177746_a((BlockPos)blockPos)) {
                return blockPos;
            }
        }
        return null;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        BlockPos blockPos = this.getCurrentBlock();
        if (blockPos == null) {
            return;
        }
        BlockPos blockPos2 = blockPos;
        IBlockState iBlockState = BlockOverlay.access$getMc$p$s1046033730().field_71441_e.func_180495_p(blockPos2);
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(blockPos)");
        Block block = iBlockState.func_177230_c();
        if (block == null) {
            return;
        }
        Block block2 = block;
        float partialTicks = event.getPartialTicks();
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow(0.4f) : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), (int)102.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.glColor(color);
        GL11.glLineWidth((float)2.0f);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        block2.func_180654_a((IBlockAccess)BlockOverlay.access$getMc$p$s1046033730().field_71441_e, blockPos2);
        double x = BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70142_S + (BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70165_t - BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70142_S) * (double)partialTicks;
        double y = BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70137_T + (BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70163_u - BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70137_T) * (double)partialTicks;
        double z = BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70136_U + (BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70161_v - BlockOverlay.access$getMc$p$s1046033730().field_71439_g.field_70136_U) * (double)partialTicks;
        AxisAlignedBB axisAlignedBB = block2.func_180646_a((World)BlockOverlay.access$getMc$p$s1046033730().field_71441_e, blockPos2).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-x, -y, -z);
        RenderUtils.drawSelectionBoundingBox(axisAlignedBB);
        RenderUtils.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.infoValue.get()).booleanValue()) {
            BlockPos blockPos = this.getCurrentBlock();
            if (blockPos == null) {
                return;
            }
            BlockPos blockPos2 = blockPos;
            Block block = BlockUtils.getBlock(blockPos2);
            if (block == null) {
                return;
            }
            Block block2 = block;
            String info = block2.func_149732_F() + " \u00a77ID: " + Block.func_149682_b((Block)block2);
            ScaledResolution scaledResolution = new ScaledResolution(BlockOverlay.access$getMc$p$s1046033730());
            float f = (float)(scaledResolution.func_78326_a() / 2) - 2.0f;
            float f2 = (float)(scaledResolution.func_78328_b() / 2) + 5.0f;
            float f3 = (float)(scaledResolution.func_78326_a() / 2 + Fonts.font40.func_78256_a(info)) + 2.0f;
            float f4 = (float)(scaledResolution.func_78328_b() / 2) + 16.0f;
            Color color = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.BLACK");
            int n = color.getRGB();
            Color color2 = Color.BLACK;
            Intrinsics.checkExpressionValueIsNotNull(color2, "Color.BLACK");
            RenderUtils.drawBorderedRect(f, f2, f3, f4, 3.0f, n, color2.getRGB());
            GlStateManager.func_179117_G();
            int n2 = scaledResolution.func_78326_a() / 2;
            int n3 = scaledResolution.func_78328_b() / 2 + 7;
            Color color3 = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color3, "Color.WHITE");
            Fonts.font40.func_78276_b(info, n2, n3, color3.getRGB());
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

