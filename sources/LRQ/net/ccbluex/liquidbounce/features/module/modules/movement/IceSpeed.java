/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="IceSpeed", description="Allows you to walk faster on ice.", category=ModuleCategory.MOVEMENT)
public final class IceSpeed
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AAC", "Spartan"}, "NCP");

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"NCP", (boolean)true)) {
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.39f);
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.39f);
        }
        super.onEnable();
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"NCP", (boolean)true)) {
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
            IMaterial it;
            boolean bl;
            boolean bl2;
            IMaterial iMaterial;
            if (StringsKt.equals((String)mode, (String)"AAC", (boolean)true)) {
                iMaterial = BlockUtils.getMaterial(thePlayer.getPosition().down());
                bl2 = false;
                bl = false;
                it = iMaterial;
                boolean bl3 = false;
                if (it.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || it.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * 1.342);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * 1.342);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.6f);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.6f);
                }
            }
            if (StringsKt.equals((String)mode, (String)"Spartan", (boolean)true)) {
                iMaterial = BlockUtils.getMaterial(thePlayer.getPosition().down());
                bl2 = false;
                bl = false;
                it = iMaterial;
                boolean bl4 = false;
                if (it.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || it.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IBlock upBlock = BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + 2.0, thePlayer.getPosZ()));
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

