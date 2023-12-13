package slavikcodd3r.rainbow.utils;

import net.minecraft.entity.player.EntityPlayer;
import slavikcodd3r.rainbow.event.EventManager;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.OldServerPinger;

public class NetworkUtils
{
    static OldServerPinger pinger;
    private Minecraft mc;
    private Timer timer;
    private static long ping;
    private long lastTime;
    private int prevDebugFPS;
    public long updatedPing;
    
    static {
        NetworkUtils.pinger = new OldServerPinger();
    }
    
    public NetworkUtils() {
        this.mc = Minecraft.getMinecraft();
        this.timer = new Timer();
        EventManager.register(this);
        final PingThread pingThread = new PingThread();
        pingThread.start();
    }
    
    public static long getPing() {
        return NetworkUtils.ping;
    }
    
    public static int getPlayerPing(final String name) {
        final EntityPlayer player = ClientUtils.mc().theWorld.getPlayerEntityByName(name);
        if (player instanceof EntityOtherPlayerMP) {
            return ((EntityOtherPlayerMP)player).field_175157_a.getResponseTime();
        }
        return 0;
    }
    
    @EventTarget
    private void on2DRender(final Render2DEvent event) {
        if (Minecraft.debugFPS != this.prevDebugFPS) {
            this.prevDebugFPS = Minecraft.debugFPS;
            NetworkUtils.ping = this.updatedPing;
        }
    }
}
