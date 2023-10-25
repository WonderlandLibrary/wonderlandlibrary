package club.async.module.impl.visual;

import club.async.event.impl.Event3D;
import club.async.event.impl.EventPacket;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.NumberSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Ambiance", description = "Time changer", category = Category.VISUAL)
public class Ambiance extends Module {

    public NumberSetting time = new NumberSetting("Time", this,0,24000,0,500);

    @Handler
    public void packet(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S03PacketTimeUpdate)
            eventPacket.setCancelled(true);
    }

    @Handler
    public void on3D(Event3D event3D) {
        setExtraTag(time.getDouble());
        mc.theWorld.setWorldTime(time.getLong());
    }

}
