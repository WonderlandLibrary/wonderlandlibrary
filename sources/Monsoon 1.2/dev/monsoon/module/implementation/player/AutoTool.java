package dev.monsoon.module.implementation.player;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", Keyboard.KEY_NONE, Category.PLAYER);
	}
	@Override
	public void onEvent(Event e) {

		if (e instanceof EventPacket && e.isPre() && e.isOutgoing()) {

			Packet p = ((EventPacket)e).packet;

			if (true) {

				try {
					if (p instanceof C07PacketPlayerDigging && mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
						autotool(mc.objectMouseOver.func_178782_a());
					}
				} catch (Exception e2) {

				}

				if (p instanceof C02PacketUseEntity && ((C02PacketUseEntity)p).getAction() == C02PacketUseEntity.Action.ATTACK) {

					int bestWeapon = -1;

					for (int i = 0; i < 9; i++) {

						ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

						if (itemStack != null && itemStack.getItem() != null && isBestWeapon(itemStack)) {

							bestWeapon = i;

						}

					}

					if (bestWeapon < 0) {
						return;
					}

					if (mc.thePlayer.inventory.currentItem == bestWeapon) {
						return;
					}

					mc.thePlayer.inventory.currentItem = bestWeapon;
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestWeapon));

				}

			}

		}

	}

	private void autotool(BlockPos position) {

		Block block = mc.theWorld.getBlockState(position).getBlock();

		int item = getStrongestItem(block);
		if (item < 0) {
			return;
		}
		float strength = AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.inventory.mainInventory[item]);
		if (mc.thePlayer.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.getHeldItem()) >= strength) {
			return;
		}

		if (mc.thePlayer.inventory.currentItem == item) {
			return;
		}

		mc.thePlayer.inventory.currentItem = item;
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(item));

	}

	public boolean isBestWeapon(ItemStack stack){
		float damage = getDamage(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if(getDamage(is) > damage && is.getItem() instanceof ItemSword)
					return false;
			}
		}

		if (stack.getItem() instanceof ItemSword) {
			return true;
		}

		return false;

	}

	public static float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if(item instanceof ItemTool){
			ItemTool tool = (ItemTool)item;
			damage += tool.getDamage();
		}
		if(item instanceof ItemSword){
			ItemSword sword = (ItemSword)item;
			damage += sword.func_150931_i();
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack) * 1.25f +
				EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}

	private int getStrongestItem(Block block) {

		float strength = Float.NEGATIVE_INFINITY;
		int strongest = -1;

		for (int i = 0; i < 9; i++) {

			float itemStrength;

			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

			if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f) {

				strongest = i;
				strength = itemStrength;

			}

		}

		return strongest;

	}

	public static float getStrengthAgainstBlock(Block block, ItemStack item) {
		float strength = item.getStrVsBlock(block);
		if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) {
			return strength;
		}
		int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
		return strength + (float)(enchantLevel * enchantLevel + 1);
	}
}