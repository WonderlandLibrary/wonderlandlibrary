package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

/**
 * Cheststealer with a delay value
 * @author purity
 */

public class ChestStealer extends Module {

	private Numbers<Double> delay = new Numbers("Delay", "delay", 50.0, 0.0, 1000.0, 10.0);

	private TimerUtil timer = new TimerUtil();

	public ChestStealer() {
		super("ChestStealer", new String[] { "cheststeal", "chests", "stealer" }, ModuleType.World);
		addValues(delay);
		setColor(new Color(218,97,127).getRGB());
	}

	@EventHandler
	private void onUpdate(EventTick event) {
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			// explained in isEmpty()
			for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
				if (container.getLowerChestInventory().getStackInSlot(i) != null
						&& timer.hasReached(delay.getValue())) {
					// grab the itme
					mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
					timer.reset();
				}
			}
			// if the chest is empty close the shit
			if (isEmpty()) {
				mc.thePlayer.closeScreen();
			}
		}
	}

	// return if the chest is empty
	private boolean isEmpty() {
		// if the inv container is open and is a chest
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			// loop the amount of chest slots
			for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
				ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
				// if null return false
				if (itemStack != null && itemStack.getItem() != null) {
					return false;
				}
			}
		}
		return true;
	}
}