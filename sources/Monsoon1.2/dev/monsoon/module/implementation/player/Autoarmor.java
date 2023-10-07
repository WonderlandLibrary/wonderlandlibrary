package dev.monsoon.module.implementation.player;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import dev.monsoon.module.enums.Category;

public class Autoarmor extends Module {
	
	private int[] chestplate, leggings, boots, helmet;
    private int delay;
    private boolean best;
	
	Timer timer = new Timer();
	public NumberSetting DelayArmor = new NumberSetting("Delay", 100, 0, 1000, 50, this);
	
	public Autoarmor() {
		super("Autoarmor", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(DelayArmor);
	}
	
	public void onEnable() {
		
	}
	
	public void onEvent(Event e) {
		if (e instanceof EventUpdate) {			
			if (!isChestInventory()) {
				for(int i = 0; i < 36; i++) {
					ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
					if (item != null && item.getItem() instanceof ItemArmor) {
						ItemArmor armour = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem();
						int equippedReduction = 0;
						int equippedDur = 0;
						int checkReduction = 0;	
						if (mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) != null) {
							ItemArmor equippedArmor = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType).getItem();
							ItemStack equippedItemStack = (ItemStack) mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType);
							equippedReduction = equippedArmor.getArmorMaterial().getDamageReductionAmount(armour.armorType);
							equippedReduction = checkProtection(mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType)) + equippedReduction;
							equippedDur = equippedItemStack.getItemDamage();
							checkReduction = armour.getArmorMaterial().getDamageReductionAmount(armour.armorType);
							checkReduction = checkProtection(mc.thePlayer.inventory.getStackInSlot(i)) + checkReduction;
						}

						if (getFreeSlot() != -1) {
							if (mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) != null) {
								if (checkReduction > equippedReduction || 
										(checkReduction == equippedReduction && item.getItemDamage() < equippedDur)) {
									
									if (i < 9) {	
										i = i+36;
									}
										mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 5 + armour.armorType, 0, 4, mc.thePlayer);
										mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
								}
							}
						}
						if (mc.thePlayer.inventory.getStackInSlot(39 - armour.armorType) == null && timer.hasTimeElapsed((long) DelayArmor.getValue(), true)) {	
							if (i < 9) {
								i = i+36;
							}
							mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
						}
					}
				}
			}
		}
	}

	public int getFreeSlot() {
		for(int i = 35; i > 0; i--) {
			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
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
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			return true;
		}
		return false;
	}
	
}
