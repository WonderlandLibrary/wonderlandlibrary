/*
 * Decompiled with CFR 0.150.
 */
package ru.smertnix.celestial.feature.impl.misc;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.server.SPacketChat;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.ui.settings.impl.StringSetting;

public class GriefJoiner
extends Feature {
    private final StringSetting cmd;
    private NumberSetting slot = new NumberSetting("Delay", "SlotClicker", 10, 1, 20, 1, () -> true);

    public GriefJoiner() {
        super("Grief Joiner", "Автоматически заходит на выбранный Grief", FeatureCategory.Util);
        cmd = new StringSetting("", "Select your grief", "Select your grief", () -> true);
        addSettings(cmd, slot);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(this.mc.currentScreen instanceof GuiContainer)) {
        	if (mc.player.ticksExisted % slot.getNumberValue() == 0)
        	mc.player.sendChatMessage("/grief-" + cmd.getCurrentText());
        }
    }
    
    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
    	 SPacketChat message = (SPacketChat) e.getPacket();
         if (message.getChatComponent().getFormattedText().contains("������� �� �������") && isEnabled()) {
        	 toggle();
         }
    }
}

