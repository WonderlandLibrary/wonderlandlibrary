package me.aidanmees.trivia.client.modules.Player;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.tools.timer;
import me.aidanmees.trivia.module.Module;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvCleaner extends Module {

	private int slots;
	
	private double numberIdkWillfigureout;
	private boolean someboolean;

	WaitTimer timer = new WaitTimer();
	timer timer2 = new timer();
	

	public InvCleaner() {
		super("InvCleaner", Keyboard.KEY_NONE, Category.PLAYER,
				"Automatically Cleans ur Inventory from useless shit.");
	}

	@Override
	public void onDisable() {
	
		super.onDisable();
	}

	@Override
	public void onEnable() {
		slots = 9;
		numberIdkWillfigureout = getEnchantmentOnSword(mc.thePlayer.getHeldItem());
	    

		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if ((slots >= 45) && (!someboolean)) {
			slots = 9;
			return;
		}
		if (someboolean) {
			if (timer2.delay(55)) {
				mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				mc.playerController.syncCurrentPlayItem();
				someboolean = false;
				
				;
			}
			return;
		}
		numberIdkWillfigureout = getEnchantmentOnSword(mc.thePlayer.getHeldItem());
		ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(slots).getStack();
		if ((isItemBad(stack)) && (getEnchantmentOnSword(stack) <= numberIdkWillfigureout)
				&& (stack != mc.thePlayer.getHeldItem())) {
			mc.playerController.windowClick(0, slots, 0, 0, mc.thePlayer);
			timer2.reset();
			someboolean = true;
		}
		slots += 1;
	}

	public static boolean isItemBad(ItemStack item) {
		return (item != null) && ((item.getItem().getUnlocalizedName().contains("TNT"))
				|| (item.getItem().getUnlocalizedName().contains("stick"))
				|| (item.getItem().getUnlocalizedName().contains("egg"))
				|| (item.getItem().getUnlocalizedName().contains("string"))
				|| (item.getItem().getUnlocalizedName().contains("flint"))
				|| (item.getItem().getUnlocalizedName().contains("compass"))
				|| (item.getItem().getUnlocalizedName().contains("feather"))
				|| (item.getItem().getUnlocalizedName().contains("map"))
				|| (item.getItem().getUnlocalizedName().contains("bucket"))
				|| (item.getItem().getUnlocalizedName().contains("chest"))
				|| (item.getItem().getUnlocalizedName().contains("snowball"))
				|| (item.getItem().getUnlocalizedName().contains("dye"))
				|| (item.getItem().getUnlocalizedName().contains("web"))
				|| (item.getItem().getUnlocalizedName().contains("gold_ingot"))
				|| (item.getItem().getUnlocalizedName().contains("arrow"))
				|| (item.getItem().getUnlocalizedName().contains("leather"))
				|| (item.getItem().getUnlocalizedName().contains("wheat"))
				|| (item.getItem().getUnlocalizedName().contains("fish"))
				|| (item.getItem().getUnlocalizedName().contains("enchant"))
				|| (item.getItem().getUnlocalizedName().contains("exp")) || ((item.getItem() instanceof ItemPickaxe))
				|| ((item.getItem() instanceof ItemTool)) || ((item.getItem() instanceof ItemArmor))
				|| ((item.getItem() instanceof ItemSword))
				|| ((item.getItem() instanceof ItemBow))
				|| ((item.getItem().getUnlocalizedName().contains("potion")) && (isBadPotion(item))));
	}

	public static boolean isBadPotion(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (!(itemStack.getItem() instanceof ItemPotion)) {
			return false;
		}
		ItemPotion itemPotion = (ItemPotion) itemStack.getItem();
		Iterator iterator = itemPotion.getEffects(itemStack).iterator();
		PotionEffect potionEffect;
		do {
			if (!iterator.hasNext()) {
				return false;
			}
			Object pObj = iterator.next();
			potionEffect = (PotionEffect) pObj;
			if (potionEffect.getPotionID() == Potion.poison.getId()) {
				return true;
			}
			if (potionEffect.getPotionID() == Potion.moveSlowdown.getId()) {
				return true;
			}
			if (potionEffect.getEffectName() == null) {
				return true;
			}
			
		} while (potionEffect.getPotionID() != Potion.harm.getId());
		return true;
	}

	private static double getEnchantmentOnSword(ItemStack itemStack) {
		if (itemStack == null) {
			return 0.0D;
		}
		if (!(itemStack.getItem() instanceof ItemSword)) {
			return 0.0D;
		}
		ItemSword itemSword = (ItemSword) itemStack.getItem();
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack)
				+ itemSword.getDamageVsEntity();
	}

}
