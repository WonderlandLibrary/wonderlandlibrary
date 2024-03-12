// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import dev.tenacity.utils.server.ServerUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class Streamer extends Module
{
    public static final BooleanSetting hideIP;
    public static final BooleanSetting hideServerId;
    public static final BooleanSetting hideUsername;
    public static final StringSetting customName;
    public static boolean enabled;
    
    public Streamer() {
        super("Streamer", "Streamer", Category.RENDER, "features for content creators");
        Streamer.customName.addParent(Streamer.hideUsername, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(Streamer.hideIP, Streamer.hideServerId, Streamer.hideUsername, Streamer.customName);
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent e) {
        if (ServerUtils.isOnHypixel() && Streamer.hideServerId.isEnabled()) {
            final String message = StringUtils.stripControlCodes(e.message.getUnformattedText());
            if (message.startsWith("Sending you to")) {
                final String serverID = message.replace("Sending you to ", "").replace("!", "");
                e.message = new ChatComponentText("§aSending you to §k" + serverID + "§r§a!");
            }
        }
    }
    
    public static String filter(String text) {
        if (Streamer.enabled) {
            if (Streamer.hideUsername.isEnabled() && Streamer.mc.getSession() != null) {
                final String name = Streamer.mc.getSession().getUsername();
                if (name != null && !name.trim().isEmpty() && !name.equals("Player") && text.contains(name)) {
                    text = text.replace(name, Streamer.customName.getString().replace('&', '§'));
                    final String text2 = StringUtils.stripControlCodes(text);
                    if (text2.contains("You has ")) {
                        text = text.replace(" has", " have");
                    }
                    if (text2.contains("You was ")) {
                        text = text.replace("was ", "were ");
                    }
                    if (text2.contains("You's ")) {
                        text = text.replace("'s ", "'re ");
                    }
                }
            }
            if (Streamer.mc.theWorld != null) {
                if (Streamer.hideIP.isEnabled() && ServerUtils.isOnHypixel() && text.startsWith("§ewww.")) {
                    text = StringUtils.stripControlCodes(text).replaceAll("[^A-Za-z0-9 .]", "").replace("www.hypixel.net", "§ewww.tenacity.dev");
                }
                if (Streamer.hideServerId.isEnabled() && text.startsWith("§7") && text.contains("/") && text.contains("  §8")) {
                    text = text.replace("§8", "§8§k");
                }
            }
        }
        return text;
    }
    
    @Override
    public void onEnable() {
        Streamer.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Streamer.enabled = false;
        super.onDisable();
    }
    
    static {
        hideIP = new BooleanSetting("Hide scoreboard IP", true);
        hideServerId = new BooleanSetting("Hide server ID", true);
        hideUsername = new BooleanSetting("Hide username", true);
        customName = new StringSetting("Custom name", "You");
    }
}
