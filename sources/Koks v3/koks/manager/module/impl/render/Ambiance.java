package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

/**
 * @author kroko
 * @created on 03.12.2020 : 14:26
 */
@ModuleInfo(name = "Ambiance", category = Module.Category.RENDER, description = "You can change the time")
public class Ambiance extends Module {

    public Setting time = new Setting("Time", 0, 0, 20000, true,this);

    @Override
    public void onEvent(Event event) {
        if(!this.isToggled())
            return;

        if(event instanceof EventPacket) {
            if(((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                Packet<? extends INetHandler> packet = ((EventPacket) event).getPacket();
                if(packet instanceof S03PacketTimeUpdate)
                    event.setCanceled(true);
            }
        }

        if(event instanceof EventUpdate) {
            getWorld().setWorldTime((long) time.getCurrentValue());
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
