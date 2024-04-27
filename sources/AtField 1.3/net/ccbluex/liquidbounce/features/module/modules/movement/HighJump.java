/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HighJump", description="Allows you to jump higher.", category=ModuleCategory.MOVEMENT)
public final class HighJump
extends Module {
    private final ListValue modeValue;
    private final BoolValue glassValue;
    private final FloatValue heightValue = new FloatValue("Height", 2.0f, 1.1f, 5.0f);

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent moveEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.glassValue.get()).booleanValue() && !MinecraftInstance.classProvider.isBlockPane(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ())))) {
            return;
        }
        if (!iEntityPlayerSP2.getOnGround()) {
            String string = (String)this.modeValue.get();
            String string2 = "mineplex";
            boolean bl = false;
            String string3 = string;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.toLowerCase();
            if (string2.equals(string4)) {
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + (iEntityPlayerSP2.getFallDistance() == 0.0f ? 0.0499 : 0.05));
            }
        }
    }

    public HighJump() {
        this.modeValue = new ListValue("Mode", new String[]{"Vanilla", "Damage", "AACv3", "DAC", "Mineplex"}, "Vanilla");
        this.glassValue = new BoolValue("OnlyGlassPane", false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.glassValue.get()).booleanValue() && !MinecraftInstance.classProvider.isBlockPane(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ())))) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case -1362669950: {
                if (!string.equals("mineplex")) return;
                break;
            }
            case 233102203: {
                if (!string.equals("vanilla")) return;
                jumpEvent.setMotion(jumpEvent.getMotion() * ((Number)this.heightValue.get()).floatValue());
                return;
            }
        }
        jumpEvent.setMotion(0.47f);
        return;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.glassValue.get()).booleanValue() && !MinecraftInstance.classProvider.isBlockPane(BlockUtils.getBlock(new WBlockPos(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ())))) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "damage": {
                if (iEntityPlayerSP2.getHurtTime() <= 0 || !iEntityPlayerSP2.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + (double)(0.42f * ((Number)this.heightValue.get()).floatValue()));
                break;
            }
            case "aacv3": {
                if (iEntityPlayerSP2.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() + 0.059);
                break;
            }
            case "dac": {
                if (iEntityPlayerSP2.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() + 0.049999);
                break;
            }
            case "mineplex": {
                if (iEntityPlayerSP2.getOnGround()) break;
                MovementUtils.strafe(0.35f);
                break;
            }
        }
    }
}

