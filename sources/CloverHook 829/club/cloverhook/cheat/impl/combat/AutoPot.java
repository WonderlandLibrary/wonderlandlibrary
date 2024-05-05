package club.cloverhook.cheat.impl.combat;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.Stopwatch;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

/**
 * Made by verble
 * -------------------------------
 * Donated to Niada ~ Nov 20th 2018
 **/
public class AutoPot extends Cheat {
	Stopwatch stopwatch;
    public AutoPot() {
        super ("AutoPotion", "Throws instant health potions at ur feet.", CheatCategory.COMBAT);
        stopwatch = new Stopwatch();
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
		if (updateCounter() == 0 || !(mc.thePlayer != null) || !(mc.theWorld != null)) {
			return;
		}
		if (getPlayer() != null && playerUpdateEvent.isPre()) {
			if (getPlayer().getHealth() <= 8 && stopwatch.hasPassed(Mafs.getRandomInRange(150, 300))) {
				if (doesHotbarHavePots()) {
					float pitch = playerUpdateEvent.getPitch();
					playerUpdateEvent.setPitch(88);
					for (int index = 36; index < 45; index++) {
						final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
						if (stack == null) {
							continue;
						}

						if (isStackSplashHealthPot(stack)) {
							final int oldslot = mc.thePlayer.inventory.currentItem;
							mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, playerUpdateEvent.getYaw(), 87, mc.thePlayer.onGround));
							mc.getNetHandler().addToSendQueue(
									new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, stack, 0, 0, 0));
							mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldslot));
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, playerUpdateEvent.getYaw(), playerUpdateEvent.getPitch(), mc.thePlayer.onGround));
							break;
						}
					}
					stopwatch.reset();
					playerUpdateEvent.setPitch(90);
				} else {
					getPotsFromInventory();
				}
			}
		}
    }

	private int updateCounter() {
		int counter = 0;
		for (int index = 9; index < 45; index++) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (stack == null) {
				continue;
			}
			if (isStackSplashHealthPot(stack)) {
				counter += stack.stackSize;
			}
		}
		return counter;
	}
    
	private boolean doesHotbarHavePots() {
		for (int index = 36; index < 45; index++) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (stack == null) {
				continue;
			}
			if (isStackSplashHealthPot(stack)) {
				return true;
			}
		}
		return false;
	}

	private void getPotsFromInventory() {
		if (mc.currentScreen instanceof GuiChest) {
			return;
		}
		for (int index = 9; index < 36; index++) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (stack == null) {
				continue;
			}
			if (isStackSplashHealthPot(stack)) {
				mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
				break;
			}
		}
	}

	private boolean isStackSplashHealthPot(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		if (stack.getItem() instanceof ItemPotion) {
			final ItemPotion potion = (ItemPotion) stack.getItem();
			if (ItemPotion.isSplash(stack.getItemDamage())) {
				for (final Object o : potion.getEffects(stack)) {
					final PotionEffect effect = (PotionEffect) o;
					if (effect.getPotionID() == Potion.heal.id) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
