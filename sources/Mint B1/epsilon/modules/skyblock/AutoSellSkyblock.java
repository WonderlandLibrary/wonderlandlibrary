package epsilon.modules.skyblock;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class AutoSellSkyblock extends Module {

	public ModeSetting mode = new ModeSetting("SellItem", "Clay", "Clay", "Snow");
	private ArrayList<Integer> clay = new ArrayList<Integer>();
	
	public AutoSellSkyblock() {
		super("AutoSellSkyblock", Keyboard.KEY_NONE, Category.DEV, "Auto sells items to NPC's");
		this.addSettings(mode);
		// TODO Auto-generated constructor stub
	}
	
	public void onEvent(final Event e) {
		
		if(e instanceof EventUpdate && mc.currentScreen!=null) {
			if(mc.currentScreen instanceof GuiChest) {
				clay.clear();
	            final String name = ((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
	            
				
				if(!name.contains("auction") && !name.contains("bazaar") && !name.contains("profile") && /*!name.contains("chest") &&*/ !name.contains("minion")) {

	                final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
	
			        for (int i = 0; i < 9 * 4; i++) {
			            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
			            if (itemStack == null || itemStack.getItem() == null) continue;
			            
			            if(itemStack.getDisplayName().toLowerCase().contains("clay")) {
			            	clay.add(i);
			            }
			            
			        }
			        if(!clay.isEmpty())
			        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, clay.get(0), chest.windowId, 4, mc.thePlayer);
					
				}
			}
		}
		
	}

}
