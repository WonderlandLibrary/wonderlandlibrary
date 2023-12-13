package me.aidanmees.trivia.client.modules.Player;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.gui.GuiDashboard;
import me.aidanmees.trivia.module.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoEat extends Module {

	int slotBefore;
	int bestSlot;
	int eating;

	public AutoEat() {
		super("AutoEat", Keyboard.KEY_NONE, Category.MISC, "Automatically eats when you can't regenerate anymore.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
mc.displayGuiScreen(new GuiDashboard(mc.currentScreen));

		slotBefore = -1;
		bestSlot = -1;
		eating = 41;

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		if (eating < 41) {
			eating++;
			if (eating <= 1) {
				mc.thePlayer.inventory.currentItem = bestSlot;
			}
			mc.gameSettings.keyBindUseItem.pressed = true;
			if (eating >= 38) {
				mc.gameSettings.keyBindUseItem.unpressKey();
				if (slotBefore != -1) {
					mc.thePlayer.inventory.currentItem = slotBefore;
				}
				slotBefore = -1;
			}
			return;
		}

		float bestRestoration = 0;
		bestSlot = -1;
		// Loop through hotbar
		int PrevSlot = mc.thePlayer.inventory.currentItem;
		for (int i = 0; i < 9; i++) {
			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
			if (item == null) {
				continue;
			}
			float restoration = 0;

			if (item.getItem() instanceof ItemFood) {
				restoration = ((ItemFood) item.getItem()).getSaturationModifier(item);
			}

			if (restoration > bestRestoration) {
				bestRestoration = restoration;
				bestSlot = i;
			}
		}
		if (bestSlot == -1) {
			return;
		}
		if ((bestSlot == -1) || !(this.mc.thePlayer.getFoodStats().getFoodLevel() < 19)) {
			return;
		}
		slotBefore = mc.thePlayer.inventory.currentItem;
		if (slotBefore == -1) {
			return;
		}
		
			
			mc.thePlayer.inventory.currentItem = PrevSlot;

			this.mc.thePlayer.stopUsingItem();
			mc.thePlayer.inventory.currentItem = PrevSlot;

			
		eating = 0;
		
		super.onUpdate();
	}

}
