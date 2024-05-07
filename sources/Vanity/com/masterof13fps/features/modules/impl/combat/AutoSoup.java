package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.time.TimeHelper;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

import com.masterof13fps.features.modules.Category;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AutoSoup", category = Category.COMBAT, description = "You automatically eat soups when your health is low")
public class AutoSoup extends Module {

	private TimeHelper time = new TimeHelper();

	private long currentMS = 0L;
	private long lastSoup = -1L;
	private int oldslot = -1;

	private void eatSoup() {
		currentMS = (System.nanoTime() / 1000000L);
		if (!hasDelayRun(125L)) {
			return;
		}
		int oldSlot = mc.thePlayer.inventory.currentItem;
		for (int slot = 44; slot >= 9; slot--) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

			if (stack != null)
				if ((slot >= 36) && (slot <= 44)) {
					if (Item.getIdFromItem(stack.getItem()) == 282) {
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
						mc.thePlayer.sendQueue.addToSendQueue(
								new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
						lastSoup = (System.nanoTime() / 1000000L);
					}
				} else if (Item.getIdFromItem(stack.getItem()) == 282) {
					mc.playerController.windowClick(0, slot, 0, 0, mc.thePlayer);
					mc.playerController.windowClick(0, 44, 0, 0, mc.thePlayer);
					lastSoup = (System.nanoTime() / 1000000L);
					return;
				}
		}
	}

	private boolean hasDelayRun(long l) {
		return currentMS - lastSoup >= l;
	}

	@Override
	public void onToggle() {
		
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if (mc.thePlayer.getHealth() < 14.0F) {
				eatSoup();
			}
		}
	}
}