package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sneak$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Sneak", description="Automatically sneaks all the time.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\b\u000020B¢J\b\t0\nHJ0\n2\f0\rHJ0\n20HR08X¢\n\u0000R0X¢\n\u0000R0\b8X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Sneak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "sneaking", "", "stopMoveValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onDisable", "", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onWorld", "worldEvent", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class Sneak
extends Module {
    @JvmField
    @NotNull
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Legit", "Vanilla", "Switch", "MineSecure"}, "MineSecure");
    @JvmField
    @NotNull
    public final BoolValue stopMoveValue = new BoolValue("StopMove", false);
    private boolean sneaking;

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.stopMoveValue.get()).booleanValue() && MovementUtils.isMoving()) {
            if (this.sneaking) {
                this.onDisable();
            }
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        block6 : switch (string3) {
            case "legit": {
                MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(true);
                break;
            }
            case "vanilla": {
                if (this.sneaking) {
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
            case "switch": {
                switch (Sneak$WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
                    case 1: {
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
                        break block6;
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
                        break block6;
                    }
                }
                break;
            }
            case "minesecure": {
                if (event.getEventState() == EventState.PRE) {
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

    @EventTarget
    public final void onWorld(@NotNull WorldEvent worldEvent) {
        Intrinsics.checkParameterIsNotNull(worldEvent, "worldEvent");
        this.sneaking = false;
    }

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP player = iEntityPlayerSP;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "legit": {
                if (MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSneak())) break;
                MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
                break;
            }
            case "minesecure": 
            case "vanilla": 
            case "switch": {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SNEAKING));
                break;
            }
        }
        this.sneaking = false;
    }
}
