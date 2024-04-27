/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Sneak", description="Automatically sneaks all the time.", category=ModuleCategory.MOVEMENT)
public final class Sneak
extends Module {
    private boolean sneaked;
    @JvmField
    public final BoolValue stopMoveValue;
    @JvmField
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Legit", "Vanilla", "Switch", "MineSecure"}, "MineSecure");

    public Sneak() {
        this.stopMoveValue = new BoolValue("StopMove", false);
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (StringsKt.equals((String)"vanilla", (String)((String)this.modeValue.get()), (boolean)true)) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SNEAKING));
        }
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "legit": {
                if (MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSneak())) break;
                MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
                break;
            }
            case "minesecure": 
            case "vanilla": 
            case "switch": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SNEAKING));
                break;
            }
        }
        super.onDisable();
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (((Boolean)this.stopMoveValue.get()).booleanValue() && MovementUtils.isMoving()) {
            if (this.sneaked) {
                this.onDisable();
                this.sneaked = false;
            }
            return;
        }
        this.sneaked = true;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        block5 : switch (string2.toLowerCase()) {
            case "legit": {
                MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(true);
                break;
            }
            case "switch": {
                switch (Sneak$WhenMappings.$EnumSwitchMapping$0[motionEvent.getEventState().ordinal()]) {
                    case 1: {
                        if (!MovementUtils.isMoving()) {
                            return;
                        }
                        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SNEAKING));
                        IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP2 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SNEAKING));
                        break block5;
                    }
                    case 2: {
                        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.STOP_SNEAKING));
                        IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP3, ICPacketEntityAction.WAction.START_SNEAKING));
                        break block5;
                    }
                }
                break;
            }
            case "minesecure": {
                if (motionEvent.getEventState() == EventState.PRE) {
                    return;
                }
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SNEAKING));
                break;
            }
        }
    }
}

