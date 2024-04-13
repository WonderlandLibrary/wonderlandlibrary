package dev.tenacity.module.impl.misc;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.setting.impl.StringSetting;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.util.misc.Multithreading;
import net.minecraft.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class AutoHypixel extends Module {

    private final BooleanSetting autoGG = new BooleanSetting("AutoGG", true);
    private final StringSetting autoGGMessage = new StringSetting("AutoGG Message", "gg");
    private final BooleanSetting autoPlay = new BooleanSetting("AutoPlay", true);
    private final NumberSetting autoPlayDelay = new NumberSetting("AutoPlay Delay", 2.5, 2, 8, 0.5);
    private final BooleanSetting autoHubOnBan = new BooleanSetting("Auto /l on ban", false);

    public AutoHypixel() {
        super("AutoHypixel", "stuff for hypixel", ModuleCategory.MISC);
        initializeSettings(autoGG, autoGGMessage, autoPlay, autoPlayDelay, autoHubOnBan);
    }

    private final IEventListener<ChatReceivedEvent> onChatReceived = e -> {
        String message = e.message.getUnformattedText(), strippedMessage = StringUtils.stripControlCodes(message);
        if (autoHubOnBan.isEnabled() && strippedMessage.equals("A player has been removed from your game.")) {
            mc.thePlayer.sendChatMessage("/lobby");
            NotificationManager.post(NotificationType.WARNING, "AutoHypixel", "A player in your lobby got banned.");
        }
        String m = e.message.toString();
        if (m.contains("ClickEvent{action=RUN_COMMAND, value='/play ")) {
            if (autoGG.isEnabled() && !strippedMessage.startsWith("You died!")) {
                mc.thePlayer.sendChatMessage("/ac " + autoGGMessage.getString());
            }
            if (autoPlay.isEnabled()) {
                sendToGame(m.split("action=RUN_COMMAND, value='")[1].split("'}")[0]);
            }
        }
    };

    private void sendToGame(String mode) {
        float delay = (float) autoPlayDelay.getCurrentValue();
        NotificationManager.post(NotificationType.INFO, "AutoPlay",
                "Sending you to a new game" + (delay > 0 ? " in " + delay + "s" : "") + "!", delay);
        Multithreading.schedule(() -> mc.thePlayer.sendChatMessage(mode), (long) (delay * 1000), TimeUnit.MILLISECONDS);
    }

}
