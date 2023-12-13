package dev.monsoon.module.implementation.player;

import dev.monsoon.module.setting.impl.BooleanSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import net.minecraft.inventory.ContainerChest;
import dev.monsoon.module.enums.Category;

public class Cheststealer extends Module {

	public NumberSetting delay = new NumberSetting("Delay", 50, 0, 1000, 5, this);
	public BooleanSetting silent = new BooleanSetting("Silent", false, this);

	public Cheststealer() {
		super("ChestStealer", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(delay,silent);
	}
	
	Timer timer = new Timer();
	
	public void onEnable() {

	}
	
	public void onDisable() {

	}
	
	public void onEvent(Event event) {
		if(mc.thePlayer != null && (mc.thePlayer.openContainer != null) && ((mc.thePlayer.openContainer instanceof ContainerChest))) {
			ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
			for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
				if ((chest.getLowerChestInventory().getStackInSlot(i) != null) && isGoodChest()) {
					if (timer.hasTimeElapsed((long) delay.getValue(), true)) {
						mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
					}
				}
				if(i >= chest.getLowerChestInventory().getSizeInventory()) {
					mc.thePlayer.closeScreen();
				}
			}
			if(chest.getInventory().isEmpty()) {
				mc.thePlayer.closeScreen();
			}
		}
	}

	public boolean isGoodChest() {
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiChest) {
			GuiChest currentChest = (GuiChest) mc.currentScreen;
			if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("game")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("select")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("compass")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("select")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("hypixel")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("play")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("skywars")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("bedwars")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("cakewars")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("lobby")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("mode")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("shop")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("map")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("cosmetic")) return false;
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("duel")) return false;

			else return true;
		} return false;
	}
	
}
