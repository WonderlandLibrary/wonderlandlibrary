package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

public class AutoPotion extends Module {

	private static boolean doThrow;
	int slot;
	private WaitTimer timer = new WaitTimer();

	public AutoPotion() {
		super("AutoPotion", Keyboard.KEY_NONE, Category.COMBAT, "Throws potions automatically. Note: "
				+ "Use AntiKnockback with this so that you don't get pushed away from the potion.");
		healthPotion = Potion.getPotionFromResourceLocation("instant_health");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		updatePotting(event);
		super.onUpdate();
	}
	
	public boolean updatePotting(UpdateEvent event) {
		if (!(mc.player.getHealth() < mc.player.getMaxHealth() / 2)
				|| mc.player.capabilities.isCreativeMode) {
			return false;
		}
		if (!timer.hasTimeElapsed(this.currentMode.equals("Head") ? 1000 : 500, false)) {
			return false;
		}
		slot = -1;
		boolean hasPots = false;
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			if (stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemPotion) {
				ItemPotion potion = (ItemPotion) stack.getItem();
				if (isValidPotion(stack)) {
					hasPots = true;
				}
			}
		}
		if (!hasPots) {
			for (int i = 9; i < 4 * 9; i++) {
				ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
				if (stack == null || stack.getItem() == null) {
					continue;
				}
				if (stack.getItem() instanceof ItemPotion && (isValidPotion(stack))) {
					mc.playerController.windowClick(mc.player.openContainer.windowId, i, 1, ClickType.SWAP, mc.player);
					break;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			if (stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemPotion) {
				ItemPotion potion = (ItemPotion) stack.getItem();
				if (isValidPotion(stack)) {
					slot = i;
					break;
				}
			}
		}
		if (slot == -1) {
			return false;
		}
		timer.reset();
		sendPacket(new CPacketHeldItemChange(slot));
		event.pitch = this.currentMode.equals("Head") ? -90f : 90f;
		event.autopot = true;
		doThrow = true;
		if(this.currentMode.equals("Head")) {
			mc.player.jump();
		}
		return true;
	}

	@Override
	public void onLateUpdate() {
		if (doThrow) {
			sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
			sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
			doThrow = false;
		}
		super.onLateUpdate();
	}
	
	private Potion healthPotion;

	private boolean isValidPotion(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemSplashPotion) {
			for (PotionEffect effect : PotionUtils.getEffectsFromStack(itemStack)) {
				if (effect.getPotion() == healthPotion) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isPotting() {
		return doThrow;
	}

	@Override
	public String[] getModes() {
		return new String[] { "Ground", "Head" };
	}

}
