/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package me.arithmo.module.impl.other;

import com.mojang.authlib.GameProfile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.IChatComponent;

public class AntiVanish
extends Module {
    private List<UUID> vanished = new CopyOnWriteArrayList<UUID>();
    private int delay = -3200;
    private String name = "Failed Check?";

    public AntiVanish(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class, EventRenderGui.class})
    public void onEvent(Event event) {
        EventMotion em;
        EventPacket ep;
        S38PacketPlayerListItem listItem;
        if (event instanceof EventMotion && (em = (EventMotion)event).isPre()) {
            if (!this.vanished.isEmpty()) {
                if (this.delay > 3200) {
                    this.vanished.clear();
                    Notifications.getManager().post("Vanish Cleared", "\u00a7fVanish List has been \u00a76Cleared.", 2500, Notifications.Type.NOTIFY);
                    this.delay = -3200;
                } else {
                    ++this.delay;
                }
            }
            try {
                for (UUID uuid : this.vanished) {
                    if (mc.getNetHandler().func_175102_a(uuid) != null && this.vanished.contains(uuid)) {
                        Notifications.getManager().post("Vanish Warning", "\u00a7b" + mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer \u00a76Vanished", 2500, Notifications.Type.NOTIFY);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        Date date = new Date();
                        ChatUtil.printChat("[" + dateFormat.format(date) + "] \u00a7b" + mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer \u00a76Vanished");
                    }
                    this.vanished.remove(uuid);
                }
            }
            catch (Exception e) {
                Notifications.getManager().post("Vanish Error", "\u00a7cSomething happened.");
            }
        }
        if (event instanceof EventPacket && (ep = (EventPacket)event).isIncoming() && AntiVanish.mc.theWorld != null && ep.getPacket() instanceof S38PacketPlayerListItem && (listItem = (S38PacketPlayerListItem)ep.getPacket()).func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
            for (Object o : listItem.func_179767_a()) {
                S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                if (mc.getNetHandler().func_175102_a(data.field_179964_d.getId()) != null || this.checkList(data.field_179964_d.getId())) continue;
                String name = this.getName(data.field_179964_d.getId());
                Notifications.getManager().post("Vanish Warning", "\u00a7b" + name + "is \u00a76Vanished!", 2500, Notifications.Type.WARNING);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                Date date = new Date();
                ChatUtil.printChat("\u00a7b[" + dateFormat.format(date) + "] " + name + "is \u00a76Vanished");
                this.delay = -3200;
            }
        }
    }

    public String getName(UUID uuid) {
        Thread thread = new Thread(() -> {
            try {
                String line;
                URL url = new URL("https://namemc.com/profile/" + uuid.toString());
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Thread.sleep(500);
                while ((line = reader.readLine()) != null) {
                    if (!line.contains("<title>")) continue;
                    this.name = line.split("\u00a7")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("\u2013 Minecraft Profile \u2013 NameMC", "").replaceAll("\u00e2\u20ac\u201c Minecraft Profile \u00e2\u20ac\u201c NameMC", "");
                }
                reader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                this.name = "(Failed) " + uuid;
                Notifications.getManager().post("Failed Name Check", this.name, 2500, Notifications.Type.WARNING);
            }
        }
        );
        thread.start();
        return this.name;
    }

    private boolean checkList(UUID uuid) {
        if (this.vanished.contains(uuid)) {
            return true;
        }
        this.vanished.add(uuid);
        return false;
    }
}

