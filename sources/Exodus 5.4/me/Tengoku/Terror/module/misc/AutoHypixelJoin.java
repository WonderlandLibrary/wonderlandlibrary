/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoHypixelJoin
extends Module {
    private TimerUtils timer = new TimerUtils();
    public Setting mode;

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("SoloInsane");
        arrayList.add("SoloNormal");
        arrayList.add("TeamsNormal");
        arrayList.add("TeamsInsane");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("AutoHypixel Mode", (Module)this, "SoloInsane", arrayList));
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("AutoHypixel Mode", this).getValString();
        if (eventPacket.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)eventPacket.getPacket();
            if (s02PacketChat.getChatComponent().getUnformattedText().contains("Want to play again?")) {
                if (string.equalsIgnoreCase("SoloInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_insane");
                }
                if (string.equalsIgnoreCase("SoloNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_normal");
                }
                if (string.equalsIgnoreCase("TeamsNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_normal");
                }
                if (string.equalsIgnoreCase("TeamsInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_insane");
                }
            }
            if (s02PacketChat.getChatComponent().getUnformattedText().contains("A player has been removed from your game.")) {
                if (string.equalsIgnoreCase("SoloInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_insane");
                }
                if (string.equalsIgnoreCase("SoloNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play solo_normal");
                }
                if (string.equalsIgnoreCase("TeamsNormal")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_normal");
                }
                if (string.equalsIgnoreCase("TeamsInsane")) {
                    Minecraft.thePlayer.sendChatMessage("/play teams_insane");
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByModule("AutoHypixel Mode", this).getValString();
        this.setDisplayName("AutoHypixel \ufffdf" + string);
    }

    public AutoHypixelJoin() {
        super("AutoHypixel", 0, Category.MISC, "Automatically joins games for you when you win.");
    }
}

