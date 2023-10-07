package wtf.monsoon.impl.modules.player;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import org.lwjgl.input.Keyboard;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventReceivePacket;
import wtf.monsoon.api.event.impl.EventSendPacket;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>InventoryManager class.</p>
 *
 * @author Joseph Robinson
 * @version $Id: $Id
 */
public class InvManager extends Module {

	private Timer timer = new Timer();

	public BooleanSetting spoof = new BooleanSetting("Spoof Inventory", true, this);
	public NumberSetting delay = new NumberSetting("Delay", 170, 170, 500, 10, this);
	public boolean inventoryOpen;
	public List<String> junk = Arrays.asList("stick", "egg", "string", "cake", "mushroom", "flint", "dyePowder", "feather", "chest", "snowball",
			"fish", "enchant", "exp", "shears", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "piston", "snow");

	/**
	 * <p>Constructor for InventoryManager.</p>
	 */
	public InvManager() {
		super("InvManager", "Manager your Inventory", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(spoof,delay);
	}

	/**
	 * <p>getSwordStrength.</p>
	 *
	 * @param stack a {@link net.minecraft.item.ItemStack} object.
	 * @return a float.
	 */
	public static float getSwordStrength(ItemStack stack) {
		return (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F) + (!(stack.getItem() instanceof ItemSword) ? 0.0F : (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1F);
	}

	/**
	 * {@inheritDoc}
	 */

	@EventTarget
	public void onPacket(EventSendPacket e) {
		Packet packet = e.getPacket();
		if (packet instanceof C16PacketClientStatus) {
			C16PacketClientStatus open = (C16PacketClientStatus) packet;
			if (open.getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT)
				inventoryOpen = true;
		}
		if (packet instanceof C0DPacketCloseWindow) {
			inventoryOpen = false;
		}
	}
	@EventTarget
	public void onPacket(EventReceivePacket e) {
		Packet packet =  e.getPacket();
		if (packet instanceof S2DPacketOpenWindow) {
			//inventoryOpen = false;
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if ((mc.currentScreen == null || (mc.currentScreen instanceof GuiContainer && ((GuiContainer) mc.currentScreen).inventorySlots == mc.thePlayer.inventoryContainer)) && purgeUnusedArmor() && purgeUnusedTools() && purgeJunk() && manageSword()) {
			if (hotbarHasSpace())
				manageHotbar();
		}
	}

	private boolean manageSword() {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null) {
				Item item = stack.getItem();
				if (!stack.getDisplayName().toLowerCase().contains("(right click)") && item instanceof ItemSword && timer.hasTimeElapsed((long) delay.getValue(), true)) {
					moveToHotbarSlot1(i);
					return false;
				}
			}
		}
		return true;
	}

	public boolean purgeUnusedArmor() {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null) {
				Item item = stack.getItem();

				if (item instanceof ItemArmor) {
					if (!isBestArmor(stack) && timer.hasTimeElapsed((long) delay.getValue(), true)) {
						purge(i);
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean purgeUnusedTools() {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null) {
				Item item = stack.getItem();

				if (item instanceof ItemTool) {
					if (!stack.getDisplayName().toLowerCase().contains("(right click)") && !isBestTool(stack) && timer.hasTimeElapsed((long) delay.getValue(), true)) {
						purge(i);
						return false;
					}
				}
				if (item instanceof ItemSword) {
					if (!stack.getDisplayName().toLowerCase().contains("(right click)") && !isBestSword(stack) && timer.hasTimeElapsed((long) delay.getValue(), true)) {
						purge(i);
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean purgeJunk() {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null) {
				Item item = stack.getItem();

				for (String shortName : junk) {
					if (!stack.getDisplayName().toLowerCase().contains("(right click)") && item.getUnlocalizedName().contains(shortName) && timer.hasTimeElapsed((long) delay.getValue(), true)) {
						purge(i);
						return false;
					}
				}
			}
		}
		return true;
	}

	public void manageHotbar() {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null) {
				Item item = stack.getItem();

				if (!stack.getDisplayName().toLowerCase().contains("(right click)") &&
						((item instanceof ItemPickaxe && hotbarNeedsItem(ItemPickaxe.class)) || (item instanceof ItemAxe && hotbarNeedsItem(ItemAxe.class)) || (item instanceof ItemSword && hotbarNeedsItem(ItemSword.class)) ||
								(item instanceof ItemAppleGold && hotbarNeedsItem(ItemAppleGold.class)) || (item instanceof ItemEnderPearl && hotbarNeedsItem(ItemEnderPearl.class)) || (item instanceof ItemBlock && (((ItemBlock) item).getBlock().isFullCube()) &&
								!hotbarHasBlocks())) &&
						!hotbar && timer.hasTimeElapsed((long) delay.getValue(), true)) {
					moveToHotbar(i);
					return;
				}
			}
		}
		//for (int i = 9; i < 45; i++) {

		//}
	}

	public boolean hotbarHasSpace() {
		for (int i = 36; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

			if (slot.getStack() == null)
				return true;
		}
		return false;
	}

	public boolean hotbarNeedsItem(Class<?> type) {
		for (int i = 36; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

			if (type.isInstance(slot.getStack()))
				return false;
		}
		return true;
	}

	public boolean hotbarHasBlocks() {
		for (int i = 36; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

			if (slot.getStack() != null && slot.getStack().getItem() instanceof ItemBlock && ((ItemBlock) slot.getStack().getItem()).getBlock().isFullCube())
				return true;
		}
		return false;
	}

	public boolean isBestTool(ItemStack compareStack) {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null && compareStack != stack && stack.getItem() instanceof ItemTool) {
				ItemTool item = (ItemTool) stack.getItem();
				ItemTool compare = (ItemTool) compareStack.getItem();
				if (item.getClass() == compare.getClass()) {
					if (compare.getStrVsBlock(stack, preferredBlock(item.getClass())) <= item.getStrVsBlock(compareStack, preferredBlock(compare.getClass())))
						return false;
				}
			}
		}

		return true;
	}

	public boolean isBestSword(ItemStack compareStack) {
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null && compareStack != stack && stack.getItem() instanceof ItemSword) {
				ItemSword item = (ItemSword) stack.getItem();
				ItemSword compare = (ItemSword) compareStack.getItem();
				if (item.getClass() == compare.getClass()) {
					if (compare.attackDamage + getSwordStrength(compareStack) <= item.attackDamage + getSwordStrength(stack))
						return false;
				}
			}
		}

		return true;
	}

	public Block preferredBlock(Class clazz) {
		return clazz == ItemPickaxe.class ? Blocks.cobblestone : clazz == ItemAxe.class ? Blocks.log : Blocks.dirt;
	}

	public boolean isBestArmor(ItemStack compareStack) {
		for (int i = 0; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
			ItemStack stack = slot.getStack();
			boolean hotbar = i >= 36;

			if (stack != null && compareStack != stack && stack.getItem() instanceof ItemArmor) {
				ItemArmor item = (ItemArmor) stack.getItem();
				ItemArmor compare = (ItemArmor) compareStack.getItem();
				if (item.armorType == compare.armorType) {
					if (AutoArmor.checkProtection(compareStack) <= AutoArmor.checkProtection(stack))
						return false;
				}
			}
		}

		return true;
	}

	public boolean has(Item item, int count) { //WIP
		for (int i = 9; i < 45; i++) {
			Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);

			if (slot.getStack() != null && (slot.getStack().getItem().equals(item)))
				count -= slot.getStack().stackSize;
		}
		return count >= 0;
	}

	public void moveToHotbar(int slot) {
		if (spoof.isEnabled())
			openInvPacket();

		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);

		if (spoof.isEnabled())
			closeInvPacket();
	}

	public void moveToHotbarSlot1(int slot) {
		if (spoof.isEnabled())
			openInvPacket();

		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 2, mc.thePlayer);

		if (spoof.isEnabled())
			closeInvPacket();
	}

	public void purge(int slot) {
		if (spoof.isEnabled())
			openInvPacket();

		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
		// Logger.ingameInfo(mc.thePlayer.ticksExisted+"");

		if (spoof.isEnabled())
			closeInvPacket();
	}

	public void openInvPacket() {
		if(!(mc.currentScreen instanceof GuiContainer)) {
			if (!inventoryOpen)
				mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT));

			inventoryOpen = true;
		}
	}

	public void closeInvPacket() {
		if(!(mc.currentScreen instanceof GuiContainer)) {
			if (inventoryOpen)
				mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));

			inventoryOpen = false;
		}
	}

}
