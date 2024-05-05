package sudo.module.world;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import sudo.module.Mod;

public class ElytraReplace extends Mod{
	
	 private boolean jump = false;

	public ElytraReplace() {
		super("ElytraReplace", "Automatically replaces your elytra when it brakes", Category.WORLD, 0);
	}
	
	 @Override
	    public void onTick() {
	        if ((mc.currentScreen instanceof HandledScreen && !(mc.currentScreen instanceof InventoryScreen)) && mc.currentScreen != null)
	            return;

	        int chestSlot = 38;
	        ItemStack chest = mc.player.getInventory().getStack(chestSlot);
	        if (chest.getItem() instanceof ElytraItem && chest.getDamage() == (Items.ELYTRA.getMaxDamage() - 1)) {

	            Integer elytraSlot = null;
	            for (int slot = 0; slot < 36; slot++) {
	                ItemStack stack = mc.player.getInventory().getStack(slot);
	                if (stack.isEmpty() || !(stack.getItem() instanceof ElytraItem) || stack.getDamage() == (Items.ELYTRA.getMaxDamage() - 1))
	                    continue;
	                else {
	                    elytraSlot = slot;
	                    break;
	                }
	            }

	            if (elytraSlot == null) {
	                return;
	            }

	            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);
	            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, elytraSlot < 9 ? (elytraSlot + 36) : (elytraSlot), 0, SlotActionType.PICKUP, mc.player);
	            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);

	            mc.options.jumpKey.setPressed(true);
	            jump = true;
	        } else if (jump) {
	            mc.options.jumpKey.setPressed(false);
	            jump = false;
	        }
	    }

}