package org.dreamcore.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ListSetting;

public class AutoShift extends Feature {

    public static ListSetting mode;

    public AutoShift() {
        super("AutoShift", "Игрок автоматически приседает при нажатии на ЛКМ", Type.Combat);
        mode = new ListSetting("ShiftTap Mode", "Client", () -> true, "Client", "Packet");
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.currentMode.equals("Client")) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                mc.gameSettings.keyBindSneak.pressed = mc.gameSettings.keyBindAttack.isKeyDown();
            }
        } else if (mode.currentMode.equals("Packet")) {
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        super.onDisable();
    }
}
