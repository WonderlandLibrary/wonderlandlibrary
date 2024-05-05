/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name="Sneak", description="Automatically sneaks all the time.", category=ModuleCategory.MOVEMENT)
public class Sneak
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Legit", "Vanilla", "Switch", "MineSecure"}, "MineSecure");
    public final BoolValue stopMoveValue = new BoolValue("StopMove", false);
    private boolean sneaked;

    @Override
    public void onEnable() {
        if (Sneak.mc.field_71439_g == null) {
            return;
        }
        if ("vanilla".equalsIgnoreCase((String)this.modeValue.get())) {
            mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (((Boolean)this.stopMoveValue.get()).booleanValue() && MovementUtils.isMoving()) {
            if (this.sneaked) {
                this.onDisable();
                this.sneaked = false;
            }
            return;
        }
        this.sneaked = true;
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "legit": {
                Sneak.mc.field_71474_y.field_74311_E.field_74513_e = true;
                break;
            }
            case "switch": {
                switch (event.getEventState()) {
                    case PRE: {
                        if (!MovementUtils.isMoving()) {
                            return;
                        }
                        mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                        mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        break;
                    }
                    case POST: {
                        mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                    }
                }
                break;
            }
            case "minesecure": {
                if (event.getEventState() == EventState.PRE) break;
                mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
            }
        }
    }

    @Override
    public void onDisable() {
        if (Sneak.mc.field_71439_g == null) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "legit": {
                if (GameSettings.func_100015_a((KeyBinding)Sneak.mc.field_71474_y.field_74311_E)) break;
                Sneak.mc.field_71474_y.field_74311_E.field_74513_e = false;
                break;
            }
            case "vanilla": 
            case "switch": 
            case "minesecure": {
                mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        super.onDisable();
    }
}

