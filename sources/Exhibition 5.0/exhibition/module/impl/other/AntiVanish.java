// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.net.URLConnection;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.RegisterEvent;
import java.text.DateFormat;
import java.util.Iterator;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import exhibition.event.impl.EventPacket;
import exhibition.util.misc.ChatUtil;
import java.util.Date;
import java.text.SimpleDateFormat;
import exhibition.management.notifications.Notifications;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import java.util.concurrent.CopyOnWriteArrayList;
import exhibition.module.data.ModuleData;
import java.util.UUID;
import java.util.List;
import exhibition.module.Module;

public class AntiVanish extends Module
{
    private List<UUID> vanished;
    private int delay;
    private String name;
    
    public AntiVanish(final ModuleData data) {
        super(data);
        this.vanished = new CopyOnWriteArrayList<UUID>();
        this.delay = -3200;
        this.name = "Failed Check?";
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class, EventRenderGui.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                if (!this.vanished.isEmpty()) {
                    if (this.delay > 3200) {
                        this.vanished.clear();
                        Notifications.getManager().post("Vanish Cleared", "§fVanish List has been §6Cleared.", 0L, 2500L, Notifications.Type.NOTIFY);
                        this.delay = -3200;
                    }
                    else {
                        ++this.delay;
                    }
                }
                try {
                    for (final UUID uuid : this.vanished) {
                        if (AntiVanish.mc.getNetHandler().func_175102_a(uuid) != null && this.vanished.contains(uuid)) {
                            Notifications.getManager().post("Vanish Warning", "§b" + AntiVanish.mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer §6Vanished", 0L, 2500L, Notifications.Type.NOTIFY);
                            final DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            final Date date = new Date();
                            ChatUtil.printChat("[" + dateFormat.format(date) + "] §b" + AntiVanish.mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer §6Vanished");
                        }
                        this.vanished.remove(uuid);
                    }
                }
                catch (Exception e) {
                    Notifications.getManager().post("Vanish Error", "§cSomething happened.");
                }
            }
        }
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isIncoming() && AntiVanish.mc.theWorld != null && ep.getPacket() instanceof S38PacketPlayerListItem) {
                final S38PacketPlayerListItem listItem = (S38PacketPlayerListItem)ep.getPacket();
                if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                    for (final Object o : listItem.func_179767_a()) {
                        final S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                        if (AntiVanish.mc.getNetHandler().func_175102_a(data.field_179964_d.getId()) == null && !this.checkList(data.field_179964_d.getId())) {
                            final String name = this.getName(data.field_179964_d.getId());
                            Notifications.getManager().post("Vanish Warning", "§b" + name + "is §6Vanished!", 0L, 2500L, Notifications.Type.WARNING);
                            final DateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
                            final Date date2 = new Date();
                            ChatUtil.printChat("[" + dateFormat2.format(date2) + "] §b" + name + "is no longer §6Vanished");
                            this.delay = -3200;
                        }
                    }
                }
            }
        }
    }
    
    public String getName(final UUID uuid) {
        final URL url2;
        URL url;
        URLConnection connection;
        final BufferedReader bufferedReader;
        BufferedReader reader;
        final String s;
        String line;
        final Thread thread = new Thread(() -> {
            try {
                new URL("https://namemc.com/profile/" + uuid.toString());
                url = url2;
                connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader = bufferedReader;
                Thread.sleep(500L);
                while (true) {
                    reader.readLine();
                    if ((line = s) != null) {
                        if (line.contains("<title>")) {
                            this.name = line.split("§")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("\u2013 Minecraft Profile \u2013 NameMC", "").replaceAll("\u00e2\u20ac\u201c Minecraft Profile \u00e2\u20ac\u201c NameMC", "");
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        break;
                    }
                }
                reader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                this.name = "(Failed) " + uuid;
                Notifications.getManager().post("Failed Name Check", this.name, 0L, 2500L, Notifications.Type.WARNING);
            }
            return;
        });
        thread.start();
        return this.name;
    }
    
    private boolean checkList(final UUID uuid) {
        if (this.vanished.contains(uuid)) {
            return true;
        }
        this.vanished.add(uuid);
        return false;
    }
}
