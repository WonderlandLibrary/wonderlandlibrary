package xyz.northclient.features.modules;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.ReceivePacketEvent;
import xyz.northclient.features.events.UpdateEvent;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;

@ModuleInfo(name = "Ambience",description = "Set world time",category = Category.RENDER)
public class Ambience extends AbstractModule {
    public ModeValue time = new ModeValue("Time",this)
            .add(new Watermark.StringMode("Night",this))
            .add(new Watermark.StringMode("Sunrise",this))
            .setDefault("Night");

    @EventLink
    public void onUpdate(UpdateEvent event) {
        if(time.get().getName().equalsIgnoreCase("Night")) {
            mc.theWorld.setWorldTime(-18000);
        }
        if(time.get().getName().equalsIgnoreCase("Sunrise")) {
            mc.theWorld.setWorldTime(22500);
        }
    }

    @EventLink
    public void onPacketReceive(ReceivePacketEvent event) {
        if(event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
}
