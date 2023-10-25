/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;
import org.lwjgl.input.Keyboard;

public class AutoShift
extends Feature {
    public static ListSetting mode;

    public AutoShift() {
        super("AutoShift", "\u0418\u0433\u0440\u043e\u043a \u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u0440\u0438\u0441\u0435\u0434\u0430\u0435\u0442 \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u041b\u041a\u041c", Type.Combat);
        mode = new ListSetting("ShiftTap Mode", "Client", () -> true, "Client", "Packet");
        this.addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AutoShift.mode.currentMode.equals("Client")) {
            if (!Keyboard.isKeyDown(AutoShift.mc.gameSettings.keyBindSneak.getKeyCode())) {
                AutoShift.mc.gameSettings.keyBindSneak.pressed = AutoShift.mc.gameSettings.keyBindAttack.isKeyDown();
            }
        } else if (AutoShift.mode.currentMode.equals("Packet")) {
            if (AutoShift.mc.gameSettings.keyBindAttack.isKeyDown()) {
                AutoShift.mc.player.connection.sendPacket(new CPacketEntityAction(AutoShift.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                AutoShift.mc.player.connection.sendPacket(new CPacketEntityAction(AutoShift.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    @Override
    public void onDisable() {
        AutoShift.mc.player.connection.sendPacket(new CPacketEntityAction(AutoShift.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        super.onDisable();
    }
}

