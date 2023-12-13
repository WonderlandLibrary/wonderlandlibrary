package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "Verifier")
public class Verifier extends Module
{   
    public static boolean click;
    
    static {
       click = true;
    }
	
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		 if (click && this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
	            final ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
	            for (int j = 0; j < chest.inventorySlots.size(); ++j) {
	                if (chest.getSlot(j) != null && chest.getSlot(j).getHasStack()) {
	                    final ItemStack item = chest.getSlot(j).getStack();
	                    if (item.getDisplayName().contains("Clique aqui")) {
	                        this.mc.playerController.windowClick(chest.windowId, j, 0, 1, this.mc.thePlayer);
	                        click = false;
	                    }
	                }
	            }
		 }
	}
}