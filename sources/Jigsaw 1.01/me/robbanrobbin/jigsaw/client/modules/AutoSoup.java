package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;

public class AutoSoup extends Module {

	int timer;

	public AutoSoup() {
		super("AutoSoup", Keyboard.KEY_NONE, Category.COMBAT, "Eats soup in your inventory when on low health.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer = 0;
		super.onEnable();
	}

	@Override
	public void onLateUpdate() {
		if (timer <= 6) {
			timer++;
			return;
		}
		timer = 6;
		int soupSlot = getSoupFromInventory();
		if (mc.player.getHealth() <= 10 && (soupSlot != -1)) {
			int prevSlot = this.mc.player.inventory.currentItem;
			if (soupSlot < 9) {
				this.mc.player.connection.sendPacket(new CPacketHeldItemChange(soupSlot));
				this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem());
				this.mc.player.connection.sendPacket(new CPacketHeldItemChange(prevSlot));
				this.mc.playerController.syncCurrentPlayItem();

				this.mc.player.inventory.currentItem = prevSlot;
			} else {
				swap(soupSlot,
						this.mc.player.inventory.currentItem + (this.mc.player.inventory.currentItem < 8 ? 1 : -1));

				this.mc.player.connection.sendPacket(new CPacketHeldItemChange(
						this.mc.player.inventory.currentItem + (this.mc.player.inventory.currentItem < 8 ? 1 : -1)));
				this.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem());
				this.mc.player.connection.sendPacket(new CPacketHeldItemChange(prevSlot));
			}
		}
		super.onLateUpdate();
	}

	protected void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, slot, hotbarNum,
				ClickType.SWAP, this.mc.player);
	}

	private int getSoupFromInventory() {
		for (int i = 0; i < 36; i++) {
			if (this.mc.player.inventory.mainInventory.get(i) != null) {
				ItemStack is = this.mc.player.inventory.mainInventory.get(i);
				Item item = is.getItem();
				if (Item.getIdFromItem(item) == 282) {
					return i;
				}
			}
		}
		return -1;
	}
}
