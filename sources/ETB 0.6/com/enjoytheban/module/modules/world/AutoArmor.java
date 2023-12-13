package com.enjoytheban.module.modules.world;

import java.awt.Color;
import java.util.Random;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * AutoArmor with best armor check
 * @author purity
 *
 */

public class AutoArmor extends Module {

	private Numbers<Double> delay = new Numbers("Delay", "delay", 50.0, 0.0, 1000.0, 10.0);

	private TimerUtil timer = new TimerUtil();

	// a value that holds our armor
	private int[] boots = { 313, 309, 317, 305, 301 };
	private int[] chestplate = { 311, 307, 315, 303, 299 };
	private int[] helmet = { 310, 306, 314, 302, 298 };
	private int[] leggings = { 312, 308, 316, 304, 300 };

	// variables we'll be using later
	private int slot = 5;
	private double enchantmentValue = -1;
	private double protectionValue;
	private int item = -1;

	public AutoArmor() {
		super("AutoArmor", new String[] { "armorswap", "autoarmour" }, ModuleType.World);
		addValues(delay);
		setColor(new Color(27,104,204).getRGB());
	}

	@EventHandler
	private void onPre(EventPreUpdate e) {
		if (e.getType() == Type.PRE) {
			if ((mc.thePlayer.capabilities.isCreativeMode)
					|| (mc.thePlayer.openContainer != null) && (mc.thePlayer.openContainer.windowId != 0)) {
				return;
			}

			//if the timer has reached the DELAY + a rand int so watchdog doesnt ban you
			if (timer.hasReached(this.delay.getValue() + new Random().nextInt(4))) {
				enchantmentValue = -1;
				item = -1;
				for (int i = 9; i < 45; i++) {
					if ((mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null)) {
						if (canEquip(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) != -1
								&& (canEquip(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) == slot))
							change(slot, i);
					}
				}
				if (item != -1) {
					if (mc.thePlayer.inventoryContainer.getSlot(item).getStack() != null)
						mc.playerController.windowClick(0, slot, 0, 1, mc.thePlayer);
					mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);

				}
				if (slot == 8) {
					slot = 5;
				} else {
					slot++;
				}
				timer.reset();
			}
		}
	}

	//can the player equip the armor?
	private int canEquip(ItemStack stack) {
		for (int id : this.boots)
			if (stack.getItem().getIdFromItem(stack.getItem()) == id) {
				return 8;
			}
		for (int id : this.leggings)
			if (stack.getItem().getIdFromItem(stack.getItem()) == id) {
				return 7;
			}
		for (int id : this.chestplate)
			if (stack.getItem().getIdFromItem(stack.getItem()) == id) {
				return 6;
			}
		for (int id : this.helmet)
			if (stack.getItem().getIdFromItem(stack.getItem()) == id) {
				return 5;
			}
		return -1;
	}

	//swwp out the armor
	private void change(int numy, int i) {
		if (enchantmentValue == -1) {
			if (mc.thePlayer.inventoryContainer.getSlot(numy).getStack() != null) {
				protectionValue = getProtValue(mc.thePlayer.inventoryContainer.getSlot(numy).getStack());
			} else
				protectionValue = enchantmentValue;
		} else {
			protectionValue = enchantmentValue;
		}
		
		if (protectionValue <= getProtValue(mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
			if (protectionValue == getProtValue(mc.thePlayer.inventoryContainer.getSlot(i).getStack())) {
				int currentD = (mc.thePlayer.inventoryContainer.getSlot(numy).getStack() != null
						? mc.thePlayer.inventoryContainer.getSlot(numy).getStack().getItemDamage()
						: 999);
				int newD = (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null
						? mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItemDamage()
						: 500);
				if (newD <= currentD) {
					if (newD == currentD) {
					} else {
						item = i;
						enchantmentValue = getProtValue(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
					}
				}
			} else {
				item = i;
				enchantmentValue = getProtValue(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
			}
		}
	}

	//grab the protection value
	private double getProtValue(ItemStack stack) {
		if (stack != null)
			return ((ItemArmor) stack.getItem()).damageReduceAmount
					+ (100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4)
							* EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack) * 4
							* 0.0075D;
		else
			return 0;
	}
}