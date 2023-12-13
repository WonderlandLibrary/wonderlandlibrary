package com.ohare.client.module.modules.other;

import com.ohare.client.Client;
import com.ohare.client.event.events.game.TickEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.EnumValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;

public class AutoMatchJoin extends Module {
    private final String[] nigga = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", "Winner: ", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};
    private TimerUtil timer = new TimerUtil();
    public EnumValue<Type> type = new EnumValue<>("Type", Type.Insane);
    public BooleanValue autoDetect = new BooleanValue("Auto Detect", true);
    private boolean allowedToSend;
    public boolean teams;

    public AutoMatchJoin() {
        super("AutoMatchJoin", Category.OTHER, new Color(0xFF9FAB).getRGB());
        setDescription("Automatically joins a match when the game ends in skywars solo.");
        setRenderlabel("Auto Match Join");
        addValues(type, autoDetect);
    }

    public enum Type {
        Normal, Insane
    }

    @Override
    public void onEnable() {
        allowedToSend = false;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (allowedToSend) {
            if (timer.reach(3000)) {
                switch (type.getValue()) {
                    case Normal:
                        mc.thePlayer.sendChatMessage(teams ? "/play teams_normal" : "/play solo_normal");
                        allowedToSend = false;
                        break;
                    case Insane:
                        mc.thePlayer.sendChatMessage(teams ? "/play teams_insane" : "/play solo_insane");
                        allowedToSend = false;
                        break;
                }
            }
        }
        if (!allowedToSend) timer.reset();
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.theWorld == null) {
            allowedToSend = false;
            return;
        }
        if (!event.isSending() && !allowedToSend) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();
                for (String str : nigga) {
                    if (packet.getChatComponent().getUnformattedText().contains(str) && GuiIngame.isInSkywars) {
                        Client.INSTANCE.getNotificationManager().addNotification("Automatically joining another game.", 1500);
                        Printer.print("Automatically joining another game.");
                        allowedToSend = true;
                    }
                }
            }
        }
    }
}