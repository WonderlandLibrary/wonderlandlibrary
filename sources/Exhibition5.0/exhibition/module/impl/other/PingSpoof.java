// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.util.MathUtils;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import exhibition.module.data.ModuleData;
import net.minecraft.network.Packet;
import java.util.List;
import exhibition.util.Timer;
import exhibition.module.Module;

public class PingSpoof extends Module
{
    private Timer timer;
    private List<Packet> packetList;
    private String WAIT;
    
    public PingSpoof(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        this.packetList = new CopyOnWriteArrayList<Packet>();
        this.WAIT = "WAIT";
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.WAIT, new Setting<Integer>(this.WAIT, 15, "Seconds to wait before sending packets again.", 1.0, 5.0, 30.0));
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C00PacketKeepAlive && PingSpoof.mc.thePlayer.isEntityAlive()) {
                this.packetList.add(ep.getPacket());
                event.setCancelled(true);
            }
            if (this.timer.delay(1000 * ((HashMap<K, Setting<Number>>)this.settings).get(this.WAIT).getValue().intValue())) {
                if (!this.packetList.isEmpty()) {
                    int i = 0;
                    final double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
                    for (final Packet packet : this.packetList) {
                        if (i < totalPackets) {
                            ++i;
                            PingSpoof.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
                            this.packetList.remove(packet);
                        }
                    }
                }
                PingSpoof.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C00PacketKeepAlive(10000));
                this.timer.reset();
            }
        }
    }
}
