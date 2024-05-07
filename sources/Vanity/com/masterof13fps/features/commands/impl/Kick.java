package com.masterof13fps.features.commands.impl;

import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Kick extends Command {

    String syntax = getClientPrefix() + "kick";

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            sendPacket(new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public Kick() {
        super("kick", "kick");
    }
}
