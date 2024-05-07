package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.mojang.authlib.GameProfile;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.manager.eventmanager.impl.EventTick;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vanish extends Command {

    private final List<GameProfile> vanishedPlayers = new ArrayList();
    String syntax = Client.main().getClientPrefix() + "vanish";
    ArrayList<String> invisiblePlayers = new ArrayList<>();

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            if (vanishedPlayers.isEmpty()) {
                notify.notification("Keine Treffer!", "Es sind derzeit keine Spieler unsichtbar!", NotificationType.INFO, 5);
            } else {
                for (GameProfile profile : this.vanishedPlayers) {
                    if (profile != null) {
                        printName(profile.getId());
                    }
                }
            }
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public void printName(final UUID uuid) {
        new Thread(() -> {
            try {
                URL url = new URL("https://namemc.com/profile/" + uuid.toString());
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Windows 10 20H2 x64; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String name = null;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("<title>")) {
                        name = line.split("§")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("§ Minecraft Profile § NameMC", "").replaceAll("§§§ Minecraft Profile §§§ NameMC", "" +
                                "").replaceAll("\\|", "").replaceAll("NameMC", "").replaceAll(" ", "").replaceAll("MinecraftProfile", "");
                    }
                }
                reader.close();
                invisiblePlayers.add(name);
                notify.chat(invisiblePlayers.toString());
                invisiblePlayers.clear();
            } catch (Exception e) {
                notify.notification("Timeout", "NameMC sendet keine Rückmeldung!", NotificationType.ERROR, 5);
            }
        }).start();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            if (((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                if ((((EventPacket) event).getPacket() instanceof S38PacketPlayerListItem)) {
                    S38PacketPlayerListItem packet = (S38PacketPlayerListItem) ((EventPacket) event).getPacket();
                    if (packet.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                        for (S38PacketPlayerListItem.AddPlayerData data : packet.func_179767_a()) {
                            if ((getNetHandler().getPlayerInfo(data.getProfile().getId()) == null) && (!vanishedPlayers.contains(data.getProfile()))) {
                                vanishedPlayers.add(data.getProfile());
                            }
                        }
                    }
                }
            }
        }

        if (event instanceof EventTick) {
            if (getPlayer() != null) {
                vanishedPlayers.removeIf(profile -> getNetHandler().getPlayerInfo(profile.getId()) != null);
            } else {
                vanishedPlayers.clear();
            }
        }
    }

    public Vanish() {
        super("vanish", "vanish");
    }
}
