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

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        String string = (String)this.modeValue.get();
        if (StringsKt.equals((String)string, (String)"NCP", (boolean)true)) {
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
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (iEntityPlayerSP2.getOnGround() && !iEntityPlayerSP2.isOnLadder() && !iEntityPlayerSP2.isSneaking() && iEntityPlayerSP2.getSprinting() && (double)iEntityPlayerSP2.getMovementInput().getMoveForward() > 0.0) {
            boolean bl;
            IMaterial iMaterial;
            boolean bl2;
            boolean bl3;
            IMaterial iMaterial2;
            if (StringsKt.equals((String)string, (String)"AAC", (boolean)true)) {
                iMaterial2 = BlockUtils.getMaterial(iEntityPlayerSP2.getPosition().down());
                bl3 = false;
                bl2 = false;
                iMaterial = iMaterial2;
                bl = false;
                if (iMaterial.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || iMaterial.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * 1.342);
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                    iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.342);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.6f);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.6f);
                }
            }
            if (StringsKt.equals((String)string, (String)"Spartan", (boolean)true)) {
                iMaterial2 = BlockUtils.getMaterial(iEntityPlayerSP2.getPosition().down());
                bl3 = false;
                bl2 = false;
                iMaterial = iMaterial2;
                bl = false;
                if (iMaterial.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE)) || iMaterial.equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED))) {
                    IBlock iBlock = BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY() + 2.0, iEntityPlayerSP2.getPosZ()));
                    if (!MinecraftInstance.classProvider.isBlockAir(iBlock)) {
                        IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                        iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * 1.342);
                        IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP2;
                        iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * 1.342);
                    } else {
                        IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP2;
                        iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * 1.18);
                        IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP2;
                        iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * 1.18);
                    }
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.6f);
                    MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.6f);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"NCP", (boolean)true)) {
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.39f);
            MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.39f);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE).setSlipperiness(0.98f);
        MinecraftInstance.classProvider.getBlockEnum(BlockType.ICE_PACKED).setSlipperiness(0.98f);
        super.onDisable();
    }
}

