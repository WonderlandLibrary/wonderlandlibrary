// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.server;

import java.net.UnknownHostException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import dev.tenacity.Tenacity;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import dev.tenacity.event.impl.game.TickEvent;
import java.util.Map;
import net.minecraft.client.network.OldServerPinger;
import dev.tenacity.utils.Utils;
import dev.tenacity.event.ListenerAdapter;

public class PingerUtils extends ListenerAdapter implements Utils
{
    public static long SERVER_UPDATE_TIME;
    private final OldServerPinger serverPinger;
    private final Map<String, Long> serverUpdateTime;
    private final Map<String, Boolean> serverUpdateStatus;
    private Long serverPing;
    
    @Override
    public void onTickEvent(final TickEvent event) {
        this.updateManually(Minecraft.getMinecraft().getCurrentServerData());
    }
    
    public PingerUtils() {
        this.serverPinger = new OldServerPinger();
        this.serverUpdateTime = new HashMap<String, Long>();
        this.serverUpdateStatus = new HashMap<String, Boolean>();
        this.serverPing = null;
        Tenacity.INSTANCE.getEventProtocol().register(this);
    }
    
    public static String getPing() {
        int latency = 0;
        if (!PingerUtils.mc.isSingleplayer()) {
            final NetworkPlayerInfo info = PingerUtils.mc.getNetHandler().getPlayerInfo(PingerUtils.mc.thePlayer.getUniqueID());
            if (info != null) {
                latency = info.getResponseTime();
            }
            if (ServerUtils.isOnHypixel() && latency == 1) {
                final int temp = Tenacity.INSTANCE.getPingerUtils().getServerPing().intValue();
                if (temp != -1) {
                    latency = temp;
                }
            }
        }
        return (latency == 0) ? "?" : String.valueOf(latency);
    }
    
    public void updateManually(final ServerData server) {
        if (server != null) {
            final Long updateTime = this.serverUpdateTime.get(server.serverIP);
            if ((updateTime == null || updateTime + PingerUtils.SERVER_UPDATE_TIME <= System.currentTimeMillis()) && !this.serverUpdateStatus.getOrDefault(server.serverIP, false)) {
                this.serverUpdateStatus.put(server.serverIP, true);
                new Thread(() -> {
                    try {
                        this.serverPinger.ping(server);
                    }
                    catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    this.serverUpdateStatus.put(server.serverIP, false);
                    this.serverUpdateTime.put(server.serverIP, System.currentTimeMillis());
                    return;
                }).start();
            }
            if (!ServerUtils.isOnHypixel() || server.pingToServer != 1L) {
                this.serverPing = server.pingToServer;
            }
        }
    }
    
    public Long getServerPing() {
        return this.serverPing;
    }
    
    static {
        PingerUtils.SERVER_UPDATE_TIME = 30000L;
    }
}
