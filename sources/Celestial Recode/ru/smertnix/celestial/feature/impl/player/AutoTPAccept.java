package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketOpenWindow;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventReceiveMessage;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.TimerHelper;

import java.util.stream.Collectors;


public class AutoTPAccept
        extends Feature {
    private final BooleanSetting friendsOnly;
    private final TimerHelper timerHelper = new TimerHelper();

    public AutoTPAccept() {
        super("Auto TP Accept", "Автоматически телепортирует человека", FeatureCategory.Player);
        friendsOnly = new BooleanSetting("Only Friends", false, () -> true);
        addSettings(this.friendsOnly);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        SPacketChat message = (SPacketChat) e.getPacket();
        if (message.getChatComponent().getFormattedText().contains("Телепорт")) {
            if (this.friendsOnly.getBoolValue()) {
                for (Friend friend : Celestial.instance.friendManager.getFriends()) {
                    if (!message.getChatComponent().getFormattedText().contains(friend.getName()) || !this.timerHelper.hasReached(300))
                        continue;
                    mc.player.sendChatMessage("/tpaccept");
                    timerHelper.reset();
                }
            } else if (this.timerHelper.hasReached(300)) {
                mc.player.sendChatMessage("/tpaccept");
                timerHelper.reset();
            }
        }
    }
}
