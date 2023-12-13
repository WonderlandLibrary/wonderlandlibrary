package com.enjoytheban.module.modules.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;
import com.google.common.collect.Multimap;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class InvCleaner extends Module {

	private static final Random RANDOM = new Random();

	public static List<Integer> blacklistedItems = new ArrayList<Integer>();

	private boolean allowSwitch = true;
	private boolean hasNoItems;

	public final TimerUtil timer = new TimerUtil();

	private Option<Boolean> openInv = new Option("Require Inventory Open?", "open inv", false);

	public InvCleaner() {
		super("InvCleaner", new String[] { "inventorycleaner", "invclean" }, ModuleType.Player);
		setColor(Color.BLUE.getRGB());
		addValues(openInv);
	}

	public void onEnable() {
		super.onEnable();
		this.hasNoItems = false;
	}

	@EventHandler
	private void onTick(EventTick event) {
		if (mc.thePlayer.isUsingItem()) {

			return;

		}

		if (mc.thePlayer.ticksExisted % 2 == 0 && RANDOM.nextInt(2) == 0) {

			if (!(openInv.getValue()) || (mc.currentScreen instanceof GuiInventory && (openInv.getValue()))) {

				if (this.timer.hasReached(59L)) {

					CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList<Integer>();

					for (int o = 0; o < 45; ++o) {

						if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {

							ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();

							if (mc.thePlayer.inventory.armorItemInSlot(0) == item
									|| mc.thePlayer.inventory.armorItemInSlot(1) == item
									|| mc.thePlayer.inventory.armorItemInSlot(2) == item
									|| mc.thePlayer.inventory.armorItemInSlot(3) == item) {

								continue;

							}

							if (item != null && item.getItem() != null && Item.getIdFromItem(item.getItem()) != 0
									&& !stackIsUseful(o)) {

								uselessItems.add(o);

							}

							hasNoItems = true;

							int i;

							ItemStack is;

						}

					}

					if (!uselessItems.isEmpty()) {

						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItems.get(0),
								1, 4, mc.thePlayer);
						uselessItems.remove(0);

						this.timer.reset();

					}

				}

			}

		}
	}

	private void bestSword() {

		int slotToSwitch = 0;
		float swordDamage = 0;

		for (int i = 9; i < 45; i++) {

			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {

				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (is.getItem() instanceof ItemSword) {

					float swordD = getItemDamage(is);

					if (swordD > swordDamage) {

						swordDamage = swordD;
						slotToSwitch = i;

					}

				}

			}

		}

		if (allowSwitch) {

			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotToSwitch,
					mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);

			allowSwitch = false;

		}

	}

	private boolean stackIsUseful(int i) {

		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

		boolean hasAlreadyOrBetter = false;

		if (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemPickaxe
				|| itemStack.getItem() instanceof ItemAxe) {

			for (int o = 0; o < 45; ++o) {

				if (o == i) {

					continue;

				}

				if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {

					ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();

					if (item != null && item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
							|| item.getItem() instanceof ItemPickaxe) {

						float damageFound = getItemDamage(itemStack);
						float damageCurrent = getItemDamage(item);

						damageFound += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
						damageCurrent += EnchantmentHelper.func_152377_a(item, EnumCreatureAttribute.UNDEFINED);

						if (damageCurrent > damageFound) {

							hasAlreadyOrBetter = true;
							break;

						}

					}

				}

			}

		} else if (itemStack.getItem() instanceof ItemArmor) {

			for (int o = 0; o < 45; ++o) {

				if (i == o) {

					continue;

				}

				if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {

					ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();

					if (item != null && item.getItem() instanceof ItemArmor) {

						List<Integer> helmet = Arrays.asList(298, 314, 302, 306, 310);
						List<Integer> chestplate = Arrays.asList(299, 315, 303, 307, 311);
						List<Integer> leggings = Arrays.asList(300, 316, 304, 308, 312);
						List<Integer> boots = Arrays.asList(301, 317, 305, 309, 313);

						if (helmet.contains(Item.getIdFromItem(item.getItem()))
								&& helmet.contains(Item.getIdFromItem(itemStack.getItem()))) {

							if (helmet.indexOf(Item.getIdFromItem(itemStack.getItem())) < helmet
									.indexOf(Item.getIdFromItem(item.getItem()))) {

								hasAlreadyOrBetter = true;

								break;

							}

						} else if (chestplate.contains(Item.getIdFromItem(item.getItem()))
								&& chestplate.contains(Item.getIdFromItem(itemStack.getItem()))) {

							if (chestplate.indexOf(Item.getIdFromItem(itemStack.getItem())) < chestplate
									.indexOf(Item.getIdFromItem(item.getItem()))) {

								hasAlreadyOrBetter = true;

								break;

							}

						} else if (leggings.contains(Item.getIdFromItem(item.getItem()))
								&& leggings.contains(Item.getIdFromItem(itemStack.getItem()))) {

							if (leggings.indexOf(Item.getIdFromItem(itemStack.getItem())) < leggings
									.indexOf(Item.getIdFromItem(item.getItem()))) {

								hasAlreadyOrBetter = true;

								break;

							}

						} else if (boots.contains(Item.getIdFromItem(item.getItem()))
								&& boots.contains(Item.getIdFromItem(itemStack.getItem()))) {

							if (boots.indexOf(Item.getIdFromItem(itemStack.getItem())) < boots
									.indexOf(Item.getIdFromItem(item.getItem()))) {

								hasAlreadyOrBetter = true;

								break;

							}

						}

					}

				}

			}

		}

		for (int o = 0; o < 45; ++o) {

			if (i == o) {

				continue;

			}

			if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {

				ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();

				if (item != null && (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
						|| item.getItem() instanceof ItemBow || item.getItem() instanceof ItemFishingRod
						|| item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemAxe
						|| item.getItem() instanceof ItemPickaxe || Item.getIdFromItem(item.getItem()) == 346)) {

					Item found = (Item) item.getItem();

					if (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())) {

						hasAlreadyOrBetter = true;

						break;

					}

				}

			}

		}

		if (Item.getIdFromItem(itemStack.getItem()) == 367)
			return false;

		if (Item.getIdFromItem(itemStack.getItem()) == 30)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 259)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 262)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 264)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 265)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 346)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 384)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 345)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 296)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 336)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 266)
			return true;

		if (Item.getIdFromItem(itemStack.getItem()) == 280)
			return true;

		if (itemStack.hasDisplayName()) {

			return true;

		}

		if (hasAlreadyOrBetter) {

			return false;

		}

		if (itemStack.getItem() instanceof ItemArmor)
			return true;
		if (itemStack.getItem() instanceof ItemAxe)
			return true;
		if (itemStack.getItem() instanceof ItemBow)
			return true;
		if (itemStack.getItem() instanceof ItemSword)
			return true;
		if (itemStack.getItem() instanceof ItemPotion)
			return true;
		if (itemStack.getItem() instanceof ItemFlintAndSteel)
			return true;
		if (itemStack.getItem() instanceof ItemEnderPearl)
			return true;
		if (itemStack.getItem() instanceof ItemBlock)
			return true;
		if (itemStack.getItem() instanceof ItemFood)
			return true;
		if (itemStack.getItem() instanceof ItemPickaxe)
			return true;

		return false;

	}

	private float getItemDamage(final ItemStack itemStack) {

		final Multimap multimap = itemStack.getAttributeModifiers();

		if (!multimap.isEmpty()) {

			final Iterator iterator = multimap.entries().iterator();

			if (iterator.hasNext()) {

				final Map.Entry entry = (Entry) iterator.next();
				final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();

				double damage;

				if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {

					damage = attributeModifier.getAmount();

				} else {

					damage = attributeModifier.getAmount() * 100.0;

				}

				if (attributeModifier.getAmount() > 1.0) {

					return 1.0f + (float) damage;

				}

				return 1.0f;
			}

		}

		return 1.0f;

	}

	public boolean isValid(Item item) {

		if (blacklistedItems.contains(Item.getIdFromItem(item))) {

			return openInv.getValue() == false || mc.currentScreen instanceof GuiInventory;

		}

		return false;

	}

	public void setSwordSlot() {

		float bestDamage = 1f;

		int bestSlot = -1;

		for (int i = 0; i < 9; i++) {

			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);

			if (item.stackSize <= 0)

				continue;

			float damage = 0;

			if (item.getItem() instanceof ItemSword)

				damage = ((ItemSword) item.getItem()).getAttackDamage();

			else if (item.getItem() instanceof ItemTool)

				damage = ((ItemTool) item.getItem()).toolMaterial.getDamageVsEntity();

			if (damage > bestDamage) {

				bestDamage = damage;
				bestSlot = i;

			}

		}

		if (bestSlot != -1 && bestSlot != mc.thePlayer.inventory.currentItem) {

			mc.thePlayer.inventory.currentItem = bestSlot;

		}

	}

}
