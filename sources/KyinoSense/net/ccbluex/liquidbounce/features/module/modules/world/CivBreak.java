/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.ClickBlockEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="CivBreak", description="Allows you to break blocks instantly.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/CivBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airResetValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blockPos", "Lnet/minecraft/util/BlockPos;", "enumFacing", "Lnet/minecraft/util/EnumFacing;", "range", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rangeResetValue", "rotationsValue", "visualSwingValue", "onBlockClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "KyinoClient"})
public final class CivBreak
extends Module {
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    private final FloatValue range = new FloatValue("Range", 5.0f, 1.0f, 6.0f);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue visualSwingValue = new BoolValue("VisualSwing", true);
    private final BoolValue airResetValue = new BoolValue("AirReset", true);
    private final BoolValue rangeResetValue = new BoolValue("RangeReset", true);

    @EventTarget
    public final void onBlockClick(@NotNull ClickBlockEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Intrinsics.areEqual(BlockUtils.getBlock(event.getClickedBlock()), Blocks.field_150357_h)) {
            return;
        }
        this.blockPos = event.getClickedBlock();
        this.enumFacing = event.getEnumFacing();
        Minecraft minecraft = CivBreak.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
        Minecraft minecraft2 = CivBreak.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        minecraft2.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
    }

    @EventTarget
    public final void onUpdate(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        BlockPos blockPos = this.blockPos;
        if (blockPos == null) {
            return;
        }
        BlockPos pos = blockPos;
        if (((Boolean)this.airResetValue.get()).booleanValue() && BlockUtils.getBlock(pos) instanceof BlockAir || ((Boolean)this.rangeResetValue.get()).booleanValue() && BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue()) {
            this.blockPos = null;
            return;
        }
        if (BlockUtils.getBlock(pos) instanceof BlockAir || BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue()) {
            return;
        }
        switch (CivBreak$WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
            case 1: {
                if (!((Boolean)this.rotationsValue.get()).booleanValue()) break;
                VecRotation vecRotation = RotationUtils.faceBlock(pos);
                if (vecRotation == null) {
                    return;
                }
                RotationUtils.setTargetRotation(vecRotation.getRotation());
                break;
            }
            case 2: {
                if (((Boolean)this.visualSwingValue.get()).booleanValue()) {
                    CivBreak.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                } else {
                    Minecraft minecraft = CivBreak.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                }
                Minecraft minecraft = CivBreak.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
                Minecraft minecraft2 = CivBreak.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
                CivBreak.access$getMc$p$s1046033730().field_71442_b.func_180511_b(this.blockPos, this.enumFacing);
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        BlockPos blockPos = this.blockPos;
        if (blockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(blockPos, Color.RED, true);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

