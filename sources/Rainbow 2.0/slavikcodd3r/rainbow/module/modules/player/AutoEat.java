package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "AutoEat")
public class AutoEat extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		final int foodSlot = this.getFoodSlotInHotbar();
        if (foodSlot != -1) {
            final Minecraft mc2 = mc;
            if (mc.thePlayer.isCollidedVertically) {
                final Minecraft mc3 = mc;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(foodSlot));
                final Minecraft mc4 = mc;
                final NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;
                final Minecraft mc5 = mc;
                sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.mainInventory[foodSlot]));
                for (int i = 0; i < 32; ++i) {
                    final Minecraft mc6 = mc;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                }
                final Minecraft mc7 = mc;
                mc.thePlayer.stopUsingItem();
                final Minecraft mc8 = mc;
                final NetHandlerPlayClient sendQueue2 = mc.thePlayer.sendQueue;
                final Minecraft mc9 = mc;
                sendQueue2.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }
    }

    private int getFoodSlotInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final Minecraft mc2 = mc;
                if (mc.thePlayer.inventory.mainInventory[i].getItem() != null) {
                    final Minecraft mc3 = mc;
                    if (mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemFood) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
