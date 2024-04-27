package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="IceSpeed", description="Allows you to walk faster on ice.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\b\u000020B¢J\b0HJ\b0HJ\b02\b\t0\nHR0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/IceSpeed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onDisable", "", "onEnable", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class IceSpeed
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AAC", "Spartan"}, "NCP");

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)this.modeValue.get(), "NCP", true)) {
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.39f);
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.39f);
        }
        super.onEnable();
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "NCP", true)) {
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.39f);
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.39f);
        } else {
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.98f);
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.98f);
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getOnGround() && !thePlayer.isOnLadder() && !thePlayer.isSneaking() && thePlayer.getSprinting() && (double)thePlayer.getMovementInput().getMoveForward() > 0.0) {
            Object it;
            IIBlockState state$iv;
            boolean $i$f$getState;
            boolean $i$f$getMaterial;
            WBlockPos blockPos$iv;
            if (StringsKt.equals(mode, "AAC", true)) {
                blockPos$iv = thePlayer.getPosition().down();
                $i$f$getMaterial = false;
                $i$f$getState = false;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                state$iv = iWorldClient != null ? iWorldClient.getBlockState(blockPos$iv) : null;
                Object object = state$iv;
                blockPos$iv = object != null && (object = object.getBlock()) != null ? object.getMaterial(state$iv) : null;
                $i$f$getMaterial = false;
                $i$f$getState = false;
                it = blockPos$iv;
                boolean bl = false;
                if (Intrinsics.areEqual(it, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || Intrinsics.areEqual(it, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 1.342);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 1.342);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.6f);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.6f);
                }
            }
            if (StringsKt.equals(mode, "Spartan", true)) {
                blockPos$iv = thePlayer.getPosition().down();
                $i$f$getMaterial = false;
                $i$f$getState = false;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                state$iv = iWorldClient != null ? iWorldClient.getBlockState(blockPos$iv) : null;
                Object object = state$iv;
                IMaterial iMaterial = object != null && (object = object.getBlock()) != null ? object.getMaterial(state$iv) : null;
                boolean bl = false;
                boolean bl2 = false;
                it = iMaterial;
                boolean bl3 = false;
                if (Intrinsics.areEqual(it, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || Intrinsics.areEqual(it, MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IBlock upBlock;
                    WBlockPos blockPos$iv2 = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 2.0, thePlayer.getPosZ());
                    boolean $i$f$getBlock = false;
                    Object object2 = MinecraftInstance.mc.getTheWorld();
                    IBlock iBlock = object2 != null && (object2 = object2.getBlockState(blockPos$iv2)) != null ? object2.getBlock() : (upBlock = null);
                    if (!MinecraftInstance.classProvider.isBlockAir(upBlock)) {
                        IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                        iEntityPlayerSP4.setMotionX(iEntityPlayerSP4.getMotionX() * 1.342);
                        IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                        iEntityPlayerSP5.setMotionZ(iEntityPlayerSP5.getMotionZ() * 1.342);
                    } else {
                        IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
                        iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * 1.18);
                        IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                        iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * 1.18);
                    }
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.6f);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.6f);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.98f);
        MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.98f);
        super.onDisable();
    }
}
