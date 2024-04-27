package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.client.InventoryUtils;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.item.ArmorPiece;
import me.aquavit.liquidsense.utils.item.ItemUtils;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.ghost.AutoArmor;
import me.aquavit.liquidsense.injection.implementations.IItemStack;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@ModuleInfo(name = "InvClean", description = "Automatically throws away useless items.", category = ModuleCategory.PLAYER)
public class InvClean extends Module {

	private final IntegerValue maxBlockStacksValue = new IntegerValue("MaxBlockStacks", 5, 0, 10) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			if (newValue < minimum) set(minimum);
		}
	};	private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 600, 0, 1000) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			final int minCPS = minDelayValue.get();

			if (minCPS > newValue) set(minCPS);
		}
	};
	private final IntegerValue maxEnchantedBooksValue = new IntegerValue("MaxEnchantedBooks", 5, 0, 10) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			if (newValue < getMinimum()) set(getMinimum());
		}
	};	private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 400, 0, 1000) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			final int maxDelay = maxDelayValue.get();

			if (maxDelay < newValue) set(maxDelay);
		}
	};
	private final IntegerValue maxWaterBucketsValue = new IntegerValue("MaxWaterBuckets", 3, 0, 10) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			if (newValue < getMinimum()) set(getMinimum());
		}
	};
	private final IntegerValue maxLavaBucketsValue = new IntegerValue("MaxLavaBuckets", 3, 0, 10) {
		@Override
		protected void onChanged(final Integer oldValue, final Integer newValue) {
			if (newValue < getMinimum()) set(getMinimum());
		}
	};
	private final BoolValue invOpenValue = new BoolValue("InvOpen", false);
	private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
	private final BoolValue noMoveValue = new BoolValue("NoMove", false);
	private final BoolValue ignoreVehiclesValue = new BoolValue("IgnoreVehicles", false);
	private final BoolValue hotbarValue = new BoolValue("Hotbar", true);
	private final BoolValue randomSlotValue = new BoolValue("RandomSlot", false);
	private final BoolValue cleanValue = new BoolValue("Clean", true);
	private final BoolValue sortValue = new BoolValue("Sort", true);
	private final IntegerValue itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
	private final String[] items = new String[]{"None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water", "Gapple", "Pearl", "SpeedPotion", "HealPotion"};
	private final ListValue sortSlot1Value = new ListValue("SortSlot-1", items, "Sword");
	private final ListValue sortSlot2Value = new ListValue("SortSlot-2", items, "Bow");
	private final ListValue sortSlot3Value = new ListValue("SortSlot-3", items, "Pickaxe");
	private final ListValue sortSlot4Value = new ListValue("SortSlot-4", items, "Axe");
	private final ListValue sortSlot5Value = new ListValue("SortSlot-5", items, "None");
	private final ListValue sortSlot6Value = new ListValue("SortSlot-6", items, "None");
	private final ListValue sortSlot7Value = new ListValue("SortSlot-7", items, "Food");
	private final ListValue sortSlot8Value = new ListValue("SortSlot-8", items, "Block");
	private final ListValue sortSlot9Value = new ListValue("SortSlot-9", items, "Block");
	private long delay = 0L;

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if ((mc.thePlayer.capabilities.allowFlying && mc.thePlayer.capabilities.isFlying) || !InventoryUtils.CLICK_TIMER.hasTimePassed(delay) ||
			!(mc.currentScreen instanceof GuiInventory) && invOpenValue.get() ||
			noMoveValue.get() && MovementUtils.isMoving() ||
			mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0)
			return;

		@SuppressWarnings("unchecked")
		final Map.Entry<Integer, ItemStack>[] garbageItems = items(9, hotbarValue.get() ? 45 : 36).entrySet().stream().filter(
			it -> !isUseful(it.getValue(), it.getKey())).sorted((s1, s2) -> randomSlotValue.get() ? (new Random().nextBoolean() ? 1 : -1) : 1).toArray(Map.Entry[]::new);

		if (sortValue.get()) sortHotbar();

		if (!cleanValue.get()) return;
		for (final Map.Entry<Integer, ItemStack> stackEntry : garbageItems) {
			if (!this.isUseful(stackEntry.getValue(), stackEntry.getKey())) {
				boolean openInventory = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.get();

				if (openInventory)
					mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));

				mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, stackEntry.getKey(), 4, 4, mc.thePlayer);

				if (openInventory)
					mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());

				delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
				break;
			}
		}


	}

	private int getBlockStacksAmount() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock && !itemStack.getUnlocalizedName().contains("flower"))
				amount += 1;
		}
		return amount;
	}

	private int getEnchantedBooksAmount() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getUnlocalizedName().equals("item.enchantedBook"))
				amount += 1;
		}
		return amount;
	}

	private int getWaterBucketsAmount() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getUnlocalizedName().equals("item.bucketWater"))
				amount += 1;
		}
		return amount;
	}

	private int getLavaBucketsAmount() {
		int amount = 0;
		for (int i = 9; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getUnlocalizedName().equals("item.bucketLava"))
				amount += 1;
		}
		return amount;
	}

	private String type(int targetSlot) {
		switch (targetSlot) {
			case 0:
				return sortSlot1Value.get();
			case 1:
				return sortSlot2Value.get();
			case 2:
				return sortSlot3Value.get();
			case 3:
				return sortSlot4Value.get();
			case 4:
				return sortSlot5Value.get();
			case 5:
				return sortSlot6Value.get();
			case 6:
				return sortSlot7Value.get();
			case 7:
				return sortSlot8Value.get();
			case 8:
				return sortSlot9Value.get();
			default:
				return "";
		}
	}

	private Map<Integer, ItemStack> items(final int start, final int end) {
		final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for (int i = end - 1; i >= start; --i) {
			final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (itemStack == null) continue;
			if (itemStack.getItem() == null) continue;
			if (36 <= i && 44 >= i && type(i).equalsIgnoreCase("Ignore")) continue;

			if (System.currentTimeMillis() - ((IItemStack) (Object) itemStack).getItemDelay() >= itemDelayValue.get())
				items.put(i, itemStack);
		}
		return items;
	}

	private Integer findBetterItem(int targetSlot, ItemStack slotStack) {
		String type = type(targetSlot);

		switch (type.toLowerCase()) {
			case "sword":
			case "pickaxe":
			case "axe": {
				Class<? extends Item> currentType;
				switch (type.toLowerCase()) {
					case "sword":
						currentType = ItemSword.class;
						break;
					case "pickaxe":
						currentType = ItemPickaxe.class;
						break;
					case "axe":
						currentType = ItemAxe.class;
						break;
					default:
						currentType = null;
				}

				int bestWeapon = slotStack != null && slotStack.getItem() != null && slotStack.getItem().getClass() == currentType ? targetSlot : -1;

				int index = 0;
				for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
					if (itemStack != null && itemStack.getItem() != null && itemStack.getItem().getClass() == currentType && !type(index).equalsIgnoreCase(type)) {
						if (bestWeapon == -1) {
							bestWeapon = index;
						} else {
							double currDamage = 0.0;
							double bestDamage = 0.0;

							for (final AttributeModifier attributeModifier : itemStack.getAttributeModifiers().get("generic.attackDamage")) {
								currDamage = attributeModifier.getAmount() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
							}

							final ItemStack bestStack = mc.thePlayer.inventory.getStackInSlot(bestWeapon);
							if (bestStack == null) continue;
							for (final AttributeModifier anotherAttributeModifier : bestStack.getAttributeModifiers().get("generic.attackDamage")) {
								bestDamage = anotherAttributeModifier.getAmount() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
							}

							if (bestDamage < currDamage) bestWeapon = index;
						}
					}
					index++;
				}

				return bestWeapon != -1 || bestWeapon == targetSlot ? bestWeapon : null;
			}
			case "bow": {
				int bestBow = (slotStack != null ? slotStack.getItem() : null) instanceof ItemBow ? targetSlot : -1;
				int bestPower = bestBow != -1 ? ItemUtils.getEnchantment(slotStack, Enchantment.power) : 0;

				int index = 0;
				for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
					if (((slotStack != null ? slotStack.getItem() : null) instanceof ItemBow) && !type(index).equalsIgnoreCase(type)) {
						if (bestBow == -1) {
							bestBow = index;
						} else {
							int power = ItemUtils.getEnchantment(itemStack, Enchantment.power);

							if (ItemUtils.getEnchantment(itemStack, Enchantment.power) > bestPower) {
								bestBow = index;
								bestPower = power;
							}
						}
					}
					index++;
				}
				return bestBow != -1 ? bestBow : null;
			}
			case "food": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();

						if (item instanceof ItemFood && !(item instanceof ItemAppleGold) && !type(index).equalsIgnoreCase("Food")) {
							boolean replaceCurr = slotStack == null || !(slotStack.getItem() instanceof ItemFood);

							return replaceCurr ? index : null;
						}

						index++;
					}
				}
				break;
			}
			case "block": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) item).getBlock()) &&
							!type(index).equalsIgnoreCase("Block")) {
							boolean replaceCurr = slotStack == null || !(slotStack.getItem() instanceof ItemBlock);

							return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}
			case "water": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemBucket && ((ItemBucket) item).isFull == Blocks.flowing_water &&
							!type(index).equalsIgnoreCase("Water")) {
							boolean replaceCurr = !(slotStack.getItem() instanceof ItemBucket) ||
								((ItemBucket) slotStack.getItem()).isFull != Blocks.flowing_water;

							return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}
			case "gapple": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemAppleGold && !type(index).equalsIgnoreCase("Gapple")) {
							boolean replaceCurr = slotStack == null || !(slotStack.getItem() instanceof ItemAppleGold);

							return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}
			case "pearl": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemEnderPearl && !type(index).equalsIgnoreCase("Pearl")) {
							boolean replaceCurr = slotStack == null || !(slotStack.getItem() instanceof ItemEnderPearl);

							return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}
			case "healpotion": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemPotion && !type(index).equals("HealPotion")) {
							boolean replaceCurr = slotStack == null ||!(slotStack.getItem() instanceof ItemPotion);
							for (PotionEffect potionEffect : ((ItemPotion) item).getEffects(stack))
								if (potionEffect.getPotionID() == Potion.heal.id || potionEffect.getPotionID() == Potion.regeneration.id)
									return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}
			case "speedpotion": {
				int index = 0;
				for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
					if (stack != null) {
						Item item = stack.getItem();
						if (item instanceof ItemPotion && !type(index).equalsIgnoreCase("SpeedPotion")) {
							boolean replaceCurr = slotStack == null || !(slotStack.getItem() instanceof ItemPotion);
							for (PotionEffect potionEffect : ((ItemPotion) item).getEffects(stack))
								if (potionEffect.getPotionID() == Potion.moveSpeed.id)
									return replaceCurr ? index : null;
						}
					}
					index++;
				}
				break;
			}

		}

		return null;
	}

	private void sortHotbar() {
		for (int index = 0; index < 8; index++) {
			Integer bestItem = findBetterItem(index, mc.thePlayer.inventory.getStackInSlot(index));
			if(bestItem == null)
				continue;

			if ( bestItem != index) {
				boolean openInventory = !(mc.currentScreen instanceof GuiInventory) && simulateInventory.get();

				if (openInventory)
					mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));

				mc.playerController.windowClick(0, bestItem < 9 ? bestItem + 36 : bestItem, index,
					2, mc.thePlayer);

				if (openInventory)
					mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());

				delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
				break;
			}
		}
	}

	public boolean isUseful(ItemStack itemStack, int slot) {
		try {
			Item item = itemStack.getItem();

			if (item instanceof ItemSword || item instanceof ItemTool) {
				if (slot >= 36) {
					Integer finditem = findBetterItem(slot - 36, mc.thePlayer.inventory.getStackInSlot(slot - 36));
					if (finditem != null && finditem == slot - 36) return true;
				}
				for (int i = 0; i < 9; i++) {
					if (type(i).equalsIgnoreCase("sword") && item instanceof ItemSword
						|| type(i).equalsIgnoreCase("pickaxe") && item instanceof ItemPickaxe
						|| type(i).equalsIgnoreCase("axe") && item instanceof ItemAxe) {
						if (findBetterItem(i, mc.thePlayer.inventory.getStackInSlot(i)) == null) {
							return true;
						}
					}
				}
				for (final AttributeModifier attributeModifier : itemStack.getAttributeModifiers().get("generic.attackDamage")) {
					final double damage = attributeModifier.getAmount() + 1.25 * ItemUtils.getEnchantment(itemStack, Enchantment.sharpness);
					for (final ItemStack stack : items(0, 45).values()) {
						if (!itemStack.equals(stack)) {
							if (item.getClass() != stack.getItem().getClass()) {
								continue;
							}
							for (final AttributeModifier anotherAttributeModifier : stack.getAttributeModifiers().get("generic.attackDamage")) {
								if (damage <= anotherAttributeModifier.getAmount() + 1.25 * ItemUtils.getEnchantment(stack, Enchantment.sharpness)) {
									return false;
								}
							}
						}
					}
				}
				return true;
			} else if (item instanceof ItemBow) {
				final int bowPower = ItemUtils.getEnchantment(itemStack, Enchantment.power);
				for (final ItemStack stack : this.items(0, 45).values()) {
					if (!itemStack.equals(stack)) {
						if (!(stack.getItem() instanceof ItemBow)) {
							continue;
						}
						if (ItemUtils.getEnchantment(stack, Enchantment.power) >= bowPower) {
							return false;
						}
					}
				}
				return true;
			} else if (item instanceof ItemArmor) {
				ArmorPiece currArmor = new ArmorPiece(itemStack, slot);

				for (final ItemStack stack : this.items(0, 45).values()) {
					if (stack.getItem() instanceof ItemArmor && !itemStack.equals(stack)) {
						ArmorPiece armor = new ArmorPiece(stack, slot);
						if (armor.getArmorType() != currArmor.getArmorType())
							continue;
						if (AutoArmor.ARMOR_COMPARATOR.compare(currArmor, armor) <= 0)
							return false;
					}
				}
				return true;
			} else {
				if (itemStack.getUnlocalizedName().equals("item.compass")) {
					for (final ItemStack anotherStack3 : items(0, 45).values()) {
						if (!itemStack.equals(anotherStack3) && anotherStack3.getUnlocalizedName().equals("item.compass")) {
							return false;
						}
					}
					return true;
				}
				return item instanceof ItemFood || itemStack.getUnlocalizedName().equals("item.arrow") ||
					(item instanceof ItemBlock && getBlockStacksAmount() <= maxBlockStacksValue.get() && !itemStack.getUnlocalizedName().contains("flower")) ||
					item instanceof ItemBed || itemStack.getUnlocalizedName().equals("item.diamond") || itemStack.getUnlocalizedName().equals("item.ingotIron") ||
					item instanceof ItemPotion || item instanceof ItemEnderPearl ||
					(itemStack.getUnlocalizedName().equals("item.enchantedBook") && getEnchantedBooksAmount() <= maxEnchantedBooksValue.get()) ||
					(itemStack.getUnlocalizedName().equals("item.bucketWater") && getWaterBucketsAmount() <= maxWaterBucketsValue.get()) ||
					(itemStack.getUnlocalizedName().equals("item.bucketLava") && getLavaBucketsAmount() <= maxLavaBucketsValue.get()) ||
					itemStack.getUnlocalizedName().equals("item.bucket") ||
					itemStack.getUnlocalizedName().equals("item.stick") ||
					ignoreVehiclesValue.get() && (item instanceof ItemBoat || item instanceof ItemMinecart);
			}
		} catch (Exception ex) {
			ClientUtils.getLogger().error("(InventoryCleaner) Failed to check item: " + itemStack.getUnlocalizedName() + ".", ex);
			return true;
		}
	}





}
