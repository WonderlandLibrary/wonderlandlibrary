package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "ArmorBreaker")
public class ArmorBreaker extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		final ItemStack current = mc.thePlayer.getHeldItem();
        for (int i = 0; i < 46; ++i) {
            final ItemStack toSwitch = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (current != null && toSwitch != null && toSwitch.getItem() instanceof ItemSword) {
                mc.playerController.windowClick(0, i, mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
            }
        }
	}
}
