package wtf.monsoon.impl.modules.player;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends Module {

	public NumberSetting delay = new NumberSetting("Delay", 50, 0, 1000, 5, this);

	public ChestStealer() {
		super("ChestStealer", "Steals the contents of a chest for your lazy ass", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(delay);
	}
	
	Timer timer = new Timer();
	
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
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
			else if (currentChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase().contains("teleport")) return false;
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
