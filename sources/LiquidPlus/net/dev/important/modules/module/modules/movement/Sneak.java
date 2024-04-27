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
package net.dev.important.modules.module.modules.movement;

import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@Info(name="Sneak", description="Automatically sneaks all the time.", category=Category.MOVEMENT, cnName="\u6084\u6084\u884c\u8d70")
public class Sneak
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Legit", "Vanilla", "Switch", "MineSecure", "AAC3.6.4"}, "MineSecure");
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
                break;
            }
            case "aac3.6.4": {
                Sneak.mc.field_71474_y.field_74311_E.field_74513_e = true;
                if (Sneak.mc.field_71439_g.field_70122_E) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.251f);
                    break;
                }
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.03f);
            }
        }
    }

    @Override
    public void onDisable() {
        if (Sneak.mc.field_71439_g == null) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "legit": 
            case "vanilla": 
            case "switch": 
            case "aac3.6.4": {
                if (GameSettings.func_100015_a((KeyBinding)Sneak.mc.field_71474_y.field_74311_E)) break;
                Sneak.mc.field_71474_y.field_74311_E.field_74513_e = false;
                break;
            }
            case "minesecure": {
                mc.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)Sneak.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        super.onDisable();
    }
}

