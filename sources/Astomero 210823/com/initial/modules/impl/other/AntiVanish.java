package com.initial.modules.impl.other;

import com.initial.modules.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import java.util.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import java.io.*;
import java.net.*;

public final class AntiVanish extends Module
{
    private List vanished;
    
    public AntiVanish() {
        super("AntiVanish", 0, Category.OTHER);
        this.vanished = new ArrayList();
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (this.isToggled()) {
            if (!this.isToggled()) {
                return;
            }
            this.setDisplayName("Anti Vanish");
            if (event.getPacket() instanceof S38PacketPlayerListItem) {
                final S38PacketPlayerListItem listItem = (S38PacketPlayerListItem)event.getPacket();
                if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                    for (final Object o : listItem.func_179767_a()) {
                        final S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                        final UUID uuid = data.getProfile().getId();
                        if (this.mc.getNetHandler().getPlayerInfo(uuid) == null && !this.checkList(uuid)) {
                            this.mc.thePlayer.addChatMessage(new ChatComponentText(this.getName(uuid) + " has gone invisible."));
                        }
                    }
                }
            }
        }
    }
    
    @EventTarget
    public final void onMotionUpdate(final EventMotionUpdate event) {
        if (this.isToggled() && !this.isToggled()) {
            return;
        }
        if (event.isPre()) {
            for (final UUID uuid : this.vanished) {
                if (this.mc.getNetHandler().getPlayerInfo(uuid) != null) {
                    this.mc.thePlayer.addChatMessage(new ChatComponentText(this.getName(uuid) + " is now visible."));
                    this.vanished.remove(uuid);
                }
            }
        }
    }
    
    private String getName(final UUID uuid) {
        try {
            final URL url = new URL("https://namemc.com/profile/" + uuid.toString());
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String name = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<title>")) {
                    name = line.split("§")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("\u2013 Minecraft Profile \u2013 NameMC", "").replaceAll("\u00e2\u20ac\u201c Minecraft Profile \u00e2\u20ac\u201c NameMC", "");
                }
            }
            reader.close();
            return name;
        }
        catch (Exception var7) {
            var7.printStackTrace();
            return "(Failed) " + uuid;
        }
    }
    
    private boolean checkList(final UUID uuid) {
        if (this.vanished.contains(uuid)) {
            this.vanished.remove(uuid);
            return true;
        }
        this.vanished.add(uuid);
        return false;
    }
}
