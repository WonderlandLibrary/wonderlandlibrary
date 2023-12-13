package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.EntityHitEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;

public class AutoRod extends Module {

	private boolean changeItem = false;
	private boolean stop = false;
	private ItemStack stack = null;
	WaitTimer timer = new WaitTimer();
	private int index = -1;

	public AutoRod() {
		super("AutoRod", Keyboard.KEY_NONE, Category.COMBAT, "Throws the rod after you click or hit someone.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (stop && timer.hasTimeElapsed(250, true)) {
			sendPacket(new CPacketPlayerTryUseItem());
			sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
			stop = false;
		}
		if (changeItem && timer.hasTimeElapsed(1, true)) {
			sendPacket(new CPacketHeldItemChange(index));
			sendPacket(new CPacketPlayerTryUseItem());
			changeItem = false;
			stop = true;
		}
		super.onUpdate();
	}

	@Override
	public void onLeftClick() {
		if (!this.currentMode.equals("OnClick")) {
			return;
		}
		searchInvetory();
	}

	@Override
	public void onEntityHit(EntityHitEvent entityHitEvent) {
		if (!this.currentMode.equals("OnHit")) {
			return;
		}
		searchInvetory();
		super.onEntityHit(entityHitEvent);
	}

	public void searchInvetory() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			Item item = stack.getItem();
			if (item == null) {
				continue;
			}
			if (item instanceof ItemFishingRod) {
				changeItem = true;
				this.stack = stack;
				index = i;
				timer.reset();
				break;
			}
		}
	}

	@Override
	public String[] getModes() {
		return new String[] { "OnClick", "OnHit" };
	}

}
