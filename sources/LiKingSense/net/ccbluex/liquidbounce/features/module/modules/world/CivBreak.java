/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="CivBreak", description="Allows you to break blocks instantly.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/CivBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airResetValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "enumFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "range", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rangeResetValue", "rotationsValue", "visualSwingValue", "onBlockClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "LiKingSense"})
public final class CivBreak
extends Module {
    public WBlockPos blockPos;
    public IEnumFacing enumFacing;
    public final FloatValue range = new FloatValue("Range", 5.0f, 1.0f, 6.0f);
    public final BoolValue rotationsValue = new BoolValue("Rotations", true);
    public final BoolValue visualSwingValue = new BoolValue("VisualSwing", true);
    public final BoolValue airResetValue = new BoolValue("Air-Reset", true);
    public final BoolValue rangeResetValue = new BoolValue("Range-Reset", true);

    @EventTarget
    public final void onBlockClick(@NotNull ClickBlockEvent event) {
        IBlock iBlock;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IClassProvider iClassProvider = MinecraftInstance.classProvider;
        WBlockPos wBlockPos = event.getClickedBlock();
        if (wBlockPos != null) {
            WBlockPos wBlockPos2 = wBlockPos;
            IClassProvider iClassProvider2 = iClassProvider;
            WBlockPos it = wBlockPos2;
            IBlock iBlock2 = BlockUtils.getBlock(it);
            iClassProvider = iClassProvider2;
            iBlock = iBlock2;
        } else {
            iBlock = null;
        }
        if (iClassProvider.isBlockBedrock(iBlock)) {
            return;
        }
        WBlockPos wBlockPos3 = event.getClickedBlock();
        if (wBlockPos3 == null) {
            return;
        }
        this.blockPos = wBlockPos3;
        IEnumFacing iEnumFacing = event.getWEnumFacing();
        if (iEnumFacing == null) {
            return;
        }
        this.enumFacing = iEnumFacing;
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
    }

    @EventTarget
    public final void onUpdate(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        WBlockPos wBlockPos = this.blockPos;
        if (wBlockPos == null) {
            return;
        }
        WBlockPos pos = wBlockPos;
        if (((Boolean)this.airResetValue.get()).booleanValue() && MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(pos)) || ((Boolean)this.rangeResetValue.get()).booleanValue() && BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue()) {
            this.blockPos = null;
            return;
        }
        if (MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(pos)) || BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue()) {
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
                    MinecraftInstance.mc.getThePlayer().swingItem();
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, this.blockPos, this.enumFacing));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
                MinecraftInstance.mc.getPlayerController().clickBlock(this.blockPos, this.enumFacing);
                break;
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        WBlockPos wBlockPos = this.blockPos;
        if (wBlockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(wBlockPos, Color.RED, true);
    }
}

