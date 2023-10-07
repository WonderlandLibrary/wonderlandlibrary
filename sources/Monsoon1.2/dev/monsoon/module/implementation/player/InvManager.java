package dev.monsoon.module.implementation.player;

import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import dev.monsoon.module.enums.Category;


public class InvManager extends Module {

	private Timer timer = new Timer();
	
	NumberSetting throwDelay = new NumberSetting("Delay", 200, 20, 1000, 10, this);
	NumberSetting theSwordSlot = new NumberSetting("SwordSlot", 1, 1, 9, 1, this);
	NumberSetting thePickSlot = new NumberSetting("Pickaxe Slot", 6, 1, 9, 1, this);
	NumberSetting theAxeSlot = new NumberSetting("Axe Slot", 7, 1, 9, 1, this);
	BooleanSetting throwBows = new BooleanSetting("Throw Bows", true, this);
	
	BooleanSetting cleanInv = new BooleanSetting("Clean", true, this);
	
	public InvManager() {
		super("InvManager", Keyboard.KEY_NONE, Category.PLAYER);
		//this.addSettings(throwDelay,theSwordSlot,thePickSlot,theAxeSlot,cleanInv,throwBows);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		NotificationManager.show(new Notification(NotificationType.INFO, "InvManager", "Under construction!", 1));
		this.toggle();
	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			double delay = throwDelay.getValue();
			if (mc.currentScreen != null) {
				timer.reset();
				return;
			}
			for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
				ItemStack is = mc.thePlayer.inventory.mainInventory[k];
				if (is != null && !(is.getItem() instanceof ItemArmor)) {
					boolean clean = cleanInv.isEnabled();
					/*int swordSlot = (int) (theSwordSlot.getValue() - 1);
					if (getBestSword() != -1 && getBestSword() != swordSlot) {
						for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
							Slot s = (Slot) mc.thePlayer.inventoryContainer.inventorySlots.get(i);
							if (s.getHasStack() && s.getStack() == mc.thePlayer.inventory.mainInventory[getBestSword()]) {
								mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, s.slotNumber, swordSlot, 2, mc.thePlayer);
								timer.reset();
								this.drop(s.slotNumber, is);
								return;
							}
						}
					}
					int pickSlot = (int) (thePickSlot.getValue() - 1);
					if (getBestPickaxe() != -1 && getBestPickaxe() != pickSlot) {
						for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
							Slot s = (Slot) mc.thePlayer.inventoryContainer.inventorySlots.get(i);
							if (s.getHasStack() && s.getStack() == mc.thePlayer.inventory.mainInventory[getBestPickaxe()]) {
								mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, s.slotNumber, pickSlot, 2, mc.thePlayer);
								timer.reset();
								this.drop(s.slotNumber, is);
								return;
							}
						}
					}
					int axeSlot = (int) (theAxeSlot.getValue() - 1);
					if (getBestAxe() != -1 && getBestAxe() != axeSlot) {
						for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
							Slot s = (Slot) mc.thePlayer.inventoryContainer.inventorySlots.get(i);
							if (s.getHasStack() && s.getStack() == mc.thePlayer.inventory.mainInventory[getBestAxe()]) {
								mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, s.slotNumber, axeSlot, 2, mc.thePlayer);
								timer.reset();
								this.drop(s.slotNumber, is);
								return;
							}
						}
					}*/
					if (cleanInv.isEnabled() && this.isBad(is.getItem())) {
						this.drop(k, is);
						timer.reset();
						return;
					}
				}
			}
			timer.reset();
		}
	}
	
	private int getBestSword() {
		int bestSword = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemSword) {
				ItemSword itemSword = (ItemSword) is.getItem();
				float damage = itemSword.func_150931_i();
				damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, is);
				if (damage > bestDamage) {
					bestDamage = damage;
					bestSword = k;
				}
			}
		}
		return bestSword;
	}
	
	private int getBestPickaxe() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemPickaxe) {
				ItemPickaxe itemSword = (ItemPickaxe) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(4));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private int getBestAxe() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && is.getItem() instanceof ItemAxe) {
				ItemAxe itemSword = (ItemAxe) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(17));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private int getBestShovel() {
		int bestPick = -1;
		float bestDamage = 1F;
		
		for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; k++) {
			ItemStack is = mc.thePlayer.inventory.mainInventory[k];
			if (is != null && this.isShovel(is.getItem())) {
				ItemTool itemSword = (ItemTool) is.getItem();
				float damage = itemSword.getStrVsBlock(is, Block.getBlockById(3));
				if (damage > bestDamage) {
					bestDamage = damage;
					bestPick = k;
				}
			}
		}
		return bestPick;
	}
	
	private boolean isShovel(Item is) {
		return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
	}
	
	private void drop(int slot, ItemStack item) {
		boolean hotbar = false;
		for (int k = 0; k < 9; k++) {
			ItemStack itemK = mc.thePlayer.inventory.getStackInSlot(k);
			if (itemK != null && itemK == item) {
				hotbar = true;
				continue;
			}
		}
		
		if (hotbar) {
			mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
			C07PacketPlayerDigging.Action diggingAction = C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(diggingAction, BlockPos.ORIGIN, EnumFacing.DOWN));
		} else {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
		}
	}
	
	private boolean isBad(Item i) {
			if(i.getUnlocalizedName().contains("bow") ||i.getUnlocalizedName().contains("arrow")) { 
				return throwBows.isEnabled();
			}
			
			return 
			i.getUnlocalizedName().contains("stick") ||
			i.getUnlocalizedName().contains("egg") ||
			i.getUnlocalizedName().contains("string") ||
			i.getUnlocalizedName().contains("flint") ||
			i.getUnlocalizedName().contains("bucket") ||
			i.getUnlocalizedName().contains("feather") ||
			i.getUnlocalizedName().contains("snow") ||
			i.getUnlocalizedName().contains("piston") || 
			i instanceof ItemGlassBottle ||
			i.getUnlocalizedName().contains("web") ||
			i.getUnlocalizedName().contains("slime") ||
			i.getUnlocalizedName().contains("trip") ||
			i.getUnlocalizedName().contains("wire") ||
			i.getUnlocalizedName().contains("sugar") ||
			i.getUnlocalizedName().contains("note") ||
			i.getUnlocalizedName().contains("record") ||
			i.getUnlocalizedName().contains("flower") ||
			i.getUnlocalizedName().contains("wheat") ||
			i.getUnlocalizedName().contains("fishing") ||
			i.getUnlocalizedName().contains("boat") ||
			i.getUnlocalizedName().contains("leather") ||
			i.getUnlocalizedName().contains("seeds") ||
			i.getUnlocalizedName().contains("skull") ||
			i.getUnlocalizedName().contains("torch") ||
			i.getUnlocalizedName().contains("anvil") ||
			i.getUnlocalizedName().contains("enchant") ||
			i.getUnlocalizedName().contains("gunpowder") ||
			i.getUnlocalizedName().contains("shears");
				
	}
}
