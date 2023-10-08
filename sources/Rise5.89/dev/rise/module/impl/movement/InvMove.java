/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.clickgui.impl.strikeless.StrikeGUI;
import dev.rise.util.player.PacketUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

@ModuleInfo(name = "InvMove", description = "Lets you move while in Guis", category = Category.MOVEMENT)
public final class InvMove extends Module {

    private final NumberSetting slowdown = new NumberSetting("Slow Down", this, 1, 0.1, 1, 0.1);
    private final BooleanSetting packet = new BooleanSetting("Packet", this, false);

    private final KeyBinding[] affectedBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump
    };

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            for (final KeyBinding a : affectedBindings) {
                a.setKeyPressed(GameSettings.isKeyDown(a));
            }

            if (mc.currentScreen != null) {
                final double s = slowdown.getValue();

                if (!(mc.currentScreen instanceof ClickGUI || mc.currentScreen instanceof StrikeGUI)) {
                    mc.thePlayer.motionX *= s;
                    mc.thePlayer.motionZ *= s;
                }
            }
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        if (packet.isEnabled()) {
            if (p instanceof C0DPacketCloseWindow)
                event.setCancelled(true);

            if (p instanceof C0EPacketClickWindow) {
                event.setCancelled(true);

                PacketUtil.sendPacketWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                PacketUtil.sendPacketWithoutEvent(event.getPacket());
                PacketUtil.sendPacketWithoutEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            }

            if (p instanceof C16PacketClientStatus) {
                final C16PacketClientStatus packetClientStatus = (C16PacketClientStatus) event.getPacket();
                if (packetClientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    event.setCancelled(true);
            }
        }
    }
}