package wtf.evolution.module.impl.Misc;

import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "ItemSwapFix", type = Category.Misc)
public class ItemSwapFix extends Module {

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof SPacketHeldItemChange) {
            e.cancel();
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }

}
