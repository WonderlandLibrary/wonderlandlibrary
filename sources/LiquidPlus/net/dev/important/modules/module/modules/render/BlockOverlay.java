/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.ScaledResolution
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
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Info(name="BlockOverlay", spacedName="Block Overlay", description="Allows you to change the design of the block overlay.", category=Category.RENDER, cnName="\u6307\u9488\u65b9\u5757\u6807\u8bb0")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\t\u001a\u0004\u0018\u00010\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0016"}, d2={"Lnet/dev/important/modules/module/modules/render/BlockOverlay;", "Lnet/dev/important/modules/module/Module;", "()V", "colorBlueValue", "Lnet/dev/important/value/IntegerValue;", "colorGreenValue", "colorRainbow", "Lnet/dev/important/value/BoolValue;", "colorRedValue", "currentBlock", "Lnet/minecraft/util/BlockPos;", "getCurrentBlock", "()Lnet/minecraft/util/BlockPos;", "infoValue", "getInfoValue", "()Lnet/dev/important/value/BoolValue;", "onRender2D", "", "event", "Lnet/dev/important/event/Render2DEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "LiquidBounce"})
public final class BlockOverlay
extends Module {
    @NotNull
    private final IntegerValue colorRedValue = new IntegerValue("R", 68, 0, 255);
    @NotNull
    private final IntegerValue colorGreenValue = new IntegerValue("G", 117, 0, 255);
    @NotNull
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    @NotNull
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    @NotNull
    private final BoolValue infoValue = new BoolValue("Info", false);

    @NotNull
    public final BoolValue getInfoValue() {
        return this.infoValue;
    }

    @Nullable
    public final BlockPos getCurrentBlock() {
        MovingObjectPosition movingObjectPosition = MinecraftInstance.mc.field_71476_x;
        Object object = movingObjectPosition == null ? null : movingObjectPosition.func_178782_a();
        if (object == null) {
            return null;
        }
        BlockPos blockPos = object;
        if (BlockUtils.canBeClicked(blockPos) && MinecraftInstance.mc.field_71441_e.func_175723_af().func_177746_a(blockPos)) {
            return blockPos;
        }
        return null;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        BlockPos blockPos = this.getCurrentBlock();
        if (blockPos == null) {
            return;
        }
        BlockPos blockPos2 = blockPos;
        Block block = MinecraftInstance.mc.field_71441_e.func_180495_p(blockPos2).func_177230_c();
        if (block == null) {
            return;
        }
        Block block2 = block;
        float partialTicks = event.getPartialTicks();
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow(0.4f) : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 102);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.glColor(color);
        GL11.glLineWidth((float)2.0f);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        block2.func_180654_a((IBlockAccess)MinecraftInstance.mc.field_71441_e, blockPos2);
        double x = MinecraftInstance.mc.field_71439_g.field_70142_S + (MinecraftInstance.mc.field_71439_g.field_70165_t - MinecraftInstance.mc.field_71439_g.field_70142_S) * (double)partialTicks;
        double y = MinecraftInstance.mc.field_71439_g.field_70137_T + (MinecraftInstance.mc.field_71439_g.field_70163_u - MinecraftInstance.mc.field_71439_g.field_70137_T) * (double)partialTicks;
        double z = MinecraftInstance.mc.field_71439_g.field_70136_U + (MinecraftInstance.mc.field_71439_g.field_70161_v - MinecraftInstance.mc.field_71439_g.field_70136_U) * (double)partialTicks;
        AxisAlignedBB axisAlignedBB = block2.func_180646_a((World)MinecraftInstance.mc.field_71441_e, blockPos2).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-x, -y, -z);
        RenderUtils.drawSelectionBoundingBox(axisAlignedBB);
        RenderUtils.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
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
            ScaledResolution scaledResolution = new ScaledResolution(MinecraftInstance.mc);
            GlStateManager.func_179117_G();
            Fonts.fontSFUI35.drawCenteredString(info, (float)scaledResolution.func_78326_a() / 2.0f, (float)scaledResolution.func_78328_b() / 2.0f + 6.0f, Color.WHITE.getRGB());
        }
    }
}

