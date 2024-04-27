package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
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
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000R0X¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/CivBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airResetValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "enumFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "range", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rangeResetValue", "rotationsValue", "visualSwingValue", "onBlockClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickBlockEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Pride"})
public final class CivBreak
extends Module {
    private WBlockPos blockPos;
    private IEnumFacing enumFacing;
    private final FloatValue range = new FloatValue("Range", 5.0f, 1.0f, 6.0f);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue visualSwingValue = new BoolValue("VisualSwing", true);
    private final BoolValue airResetValue = new BoolValue("Air-Reset", true);
    private final BoolValue rangeResetValue = new BoolValue("Range-Reset", true);

    @EventTarget
    public final void onBlockClick(@NotNull ClickBlockEvent event) {
        IBlock iBlock;
        Intrinsics.checkParameterIsNotNull(event, "event");
        IClassProvider iClassProvider = MinecraftInstance.classProvider;
        WBlockPos wBlockPos = event.getClickedBlock();
        if (wBlockPos != null) {
            WBlockPos wBlockPos2 = wBlockPos;
            IClassProvider iClassProvider2 = iClassProvider;
            boolean bl = false;
            boolean bl2 = false;
            WBlockPos it = wBlockPos2;
            boolean bl3 = false;
            boolean $i$f$getBlock = false;
            Object object = MinecraftInstance.mc.getTheWorld();
            IBlock iBlock2 = object != null && (object = object.getBlockState(it)) != null ? object.getBlock() : null;
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
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        WBlockPos wBlockPos4 = this.blockPos;
        if (wBlockPos4 == null) {
            Intrinsics.throwNpe();
        }
        IEnumFacing iEnumFacing2 = this.enumFacing;
        if (iEnumFacing2 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, wBlockPos4, iEnumFacing2));
        IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
        WBlockPos wBlockPos5 = this.blockPos;
        if (wBlockPos5 == null) {
            Intrinsics.throwNpe();
        }
        IEnumFacing iEnumFacing3 = this.enumFacing;
        if (iEnumFacing3 == null) {
            Intrinsics.throwNpe();
        }
        iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos5, iEnumFacing3));
    }

    @EventTarget
    public final void onUpdate(@NotNull MotionEvent event) {
        IBlock iBlock;
        boolean $i$f$getBlock;
        IClassProvider iClassProvider;
        WBlockPos pos;
        block21: {
            block20: {
                block19: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    WBlockPos wBlockPos = this.blockPos;
                    if (wBlockPos == null) {
                        return;
                    }
                    pos = wBlockPos;
                    if (!((Boolean)this.airResetValue.get()).booleanValue()) break block19;
                    iClassProvider = MinecraftInstance.classProvider;
                    $i$f$getBlock = false;
                    Object object = MinecraftInstance.mc.getTheWorld();
                    IBlock iBlock2 = object != null && (object = object.getBlockState(pos)) != null ? object.getBlock() : (iBlock = null);
                    if (iClassProvider.isBlockAir(iBlock)) break block20;
                }
                if (!((Boolean)this.rangeResetValue.get()).booleanValue() || !(BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue())) break block21;
            }
            this.blockPos = null;
            return;
        }
        iClassProvider = MinecraftInstance.classProvider;
        $i$f$getBlock = false;
        Object object = MinecraftInstance.mc.getTheWorld();
        IBlock iBlock3 = object != null && (object = object.getBlockState(pos)) != null ? object.getBlock() : (iBlock = null);
        if (iClassProvider.isBlockAir(iBlock) || BlockUtils.getCenterDistance(pos) > ((Number)this.range.get()).doubleValue()) {
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
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.swingItem();
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                WBlockPos wBlockPos = this.blockPos;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing = this.enumFacing;
                if (iEnumFacing == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, wBlockPos, iEnumFacing));
                IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                WBlockPos wBlockPos2 = this.blockPos;
                if (wBlockPos2 == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing2 = this.enumFacing;
                if (iEnumFacing2 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos2, iEnumFacing2));
                IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                WBlockPos wBlockPos3 = this.blockPos;
                if (wBlockPos3 == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing3 = this.enumFacing;
                if (iEnumFacing3 == null) {
                    Intrinsics.throwNpe();
                }
                iPlayerControllerMP.clickBlock(wBlockPos3, iEnumFacing3);
                break;
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        WBlockPos wBlockPos = this.blockPos;
        if (wBlockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(wBlockPos, Color.RED, true);
    }
}
