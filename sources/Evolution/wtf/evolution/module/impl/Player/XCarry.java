package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "XCarry", type = Category.Player)
public class XCarry extends Module {

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof CPacketCloseWindow) {
            e.cancel();
        }
    }

}
