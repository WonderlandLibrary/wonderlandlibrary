// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.utils.misc.Multithreading;
import java.util.concurrent.TimeUnit;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.util.StringUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class AutoHypixel extends Module
{
    private final BooleanSetting autoGG;
    private final StringSetting autoGGMessage;
    private final BooleanSetting autoPlay;
    private final NumberSetting autoPlayDelay;
    private final BooleanSetting autoHubOnBan;
    
    public AutoHypixel() {
        super("AutoHypixel", "Auto Hypixel", Category.MISC, "stuff for hypixel");
        this.autoGG = new BooleanSetting("AutoGG", true);
        this.autoGGMessage = new StringSetting("AutoGG Message", "gg");
        this.autoPlay = new BooleanSetting("AutoPlay", true);
        this.autoPlayDelay = new NumberSetting("AutoPlay Delay", 2.5, 8.0, 1.0, 0.5);
        this.autoHubOnBan = new BooleanSetting("Auto /l on ban", false);
        this.autoGGMessage.addParent(this.autoGG, ParentAttribute.BOOLEAN_CONDITION);
        this.autoPlayDelay.addParent(this.autoPlay, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.autoGG, this.autoGGMessage, this.autoPlay, this.autoPlayDelay, this.autoHubOnBan);
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        final String message = event.message.getUnformattedText();
        final String strippedMessage = StringUtils.stripControlCodes(message);
        if (this.autoHubOnBan.isEnabled() && strippedMessage.equals("A player has been removed from your game.")) {
            ChatUtil.send("/lobby");
            NotificationManager.post(NotificationType.WARNING, "AutoHypixel", "A player in your lobby got banned.");
        }
        final String m = event.message.toString();
        if (m.contains("ClickEvent{action=RUN_COMMAND, value='/play ")) {
            if (this.autoGG.isEnabled() && !strippedMessage.startsWith("You died!")) {
                ChatUtil.send("/ac " + this.autoGGMessage.getString());
            }
            if (this.autoPlay.isEnabled()) {
                this.sendToGame(m.split("action=RUN_COMMAND, value='")[1].split("'}")[0]);
            }
        }
    }
    
    private void sendToGame(final String mode) {
        final float delay = this.autoPlayDelay.getValue().floatValue();
        NotificationManager.post(NotificationType.INFO, "AutoPlay", "Sending you to a new game" + ((delay > 0.0f) ? (" in " + delay + "s") : "") + "!", delay);
        Multithreading.schedule(() -> ChatUtil.send(mode), (long)delay, TimeUnit.SECONDS);
    }
}
