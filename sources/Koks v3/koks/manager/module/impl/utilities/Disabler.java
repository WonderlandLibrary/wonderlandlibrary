package koks.manager.module.impl.utilities;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventPacket;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

/**
 * @author Kroko, Phantom, Deleteboys, Dirt
 * @created on 05.12.2020 : 21:28
 */

@ModuleInfo(name = "Disabler", description = "Disable AntiCheat Checks", category = Module.Category.UTILITIES)
public class Disabler extends Module {


    public Setting mode = new Setting("Mode", new String[]{"Hypixel"}, "Hypixel", this);

    @Override
    public void onEvent(Event event) {
        if (!this.isToggled())
            return;

        if (event instanceof EventPacket) {
            if (((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                Packet<? extends INetHandler> packet = ((EventPacket) event).getPacket();
                if (packet instanceof S32PacketConfirmTransaction) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
