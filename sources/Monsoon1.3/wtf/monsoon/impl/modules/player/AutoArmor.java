package wtf.monsoon.impl.modules.player;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import wtf.monsoon.api.Wrapper;

public class AutoArmor extends Module {
	
	private int[] chestplate, leggings, boots, helmet;
    private int delay;
    private boolean best;
	
	Timer timer = new Timer();
	public NumberSetting DelayArmor = new NumberSetting("Delay", 100, 0, 1000, 50, this);
	
	public AutoArmor() {
		super("Autoarmor", "Automatically equips armor",  Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(DelayArmor);
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		
		if (!isChestInventory()) {
			for(int i = 0; i < 36; i++) {
				ItemStack item = Wrapper.mc.thePlayer.inventory.getStackInSlot(i);
				if (item != null && item.getItem() instanceof ItemArmor) {
					ItemArmor armour = (ItemArmor) Wrapper.mc.thePlayer.inventory.getStackInSlot(i).getItem();
					int equippedReduction = 0;
					int equippedDur = 0;
					int checkReduction = 0;	
					if (Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) != null) {
						ItemArmor equippedArmor = (ItemArmor) Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType).getItem();
						ItemStack equippedItemStack = (ItemStack) Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType);
						equippedReduction = equippedArmor.getArmorMaterial().getDamageReductionAmount(armour.armorType);
						equippedReduction = checkProtection(Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType)) + equippedReduction;
						equippedDur = equippedItemStack.getItemDamage();
						checkReduction = armour.getArmorMaterial().getDamageReductionAmount(armour.armorType);
						checkReduction = checkProtection(Wrapper.mc.thePlayer.inventory.getStackInSlot(i)) + checkReduction;
					}

					if (getFreeSlot() != -1) {
						if (Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) != null) {
							if (checkReduction > equippedReduction || 
									(checkReduction == equippedReduction && item.getItemDamage() < equippedDur)) {
								
								if (i < 9) {	
									i = i+36;
								}
									Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.inventoryContainer.windowId, 5 + armour.armorType, 0, 4, Wrapper.mc.thePlayer);
									Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.inventoryContainer.windowId, i, 0, 1, Wrapper.mc.thePlayer);
							}
						}
					}
					if (Wrapper.mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) == null && timer.hasTimeElapsed((long) DelayArmor.getValue(), true)) {
						if (i < 9) {
							i = i+36;
						}
						Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.inventoryContainer.windowId, i, 0, 1, Wrapper.mc.thePlayer);
					}
				}
			}
		}
	
	}
	
	public int getFreeSlot() {
		for(int i = 35; i > 0; i--) {
			ItemStack item = Wrapper.mc.thePlayer.inventory.getStackInSlot(i);
			if (item == null) {
				return i;
			}
		}
		return -1;
		
	}

	public static int checkProtection(ItemStack item) {
		return EnchantmentHelper.getEnchantmentLevel(0, item);
	}

	public boolean isChestInventory() {
		if (Wrapper.mc.thePlayer.openContainer != null && Wrapper.mc.thePlayer.openContainer instanceof ContainerChest) {
			return true;
		}
		return false;
	}
	
}
