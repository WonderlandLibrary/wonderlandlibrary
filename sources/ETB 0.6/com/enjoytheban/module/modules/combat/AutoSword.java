package com.enjoytheban.module.modules.combat;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.InventoryUtils;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/*
 * Created by Jutting on Oct 10, 2018
 */

public class AutoSword extends Module {

	private ItemStack bestSword;
	private ItemStack prevBestSword;
	private boolean shouldSwitch = false;
	public TimerUtil timer = new TimerUtil();

	public AutoSword() {
		super("AutoSword", new String[] { "autosword" }, ModuleType.Combat);
		setColor(new Color(208, 30, 142).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (mc.thePlayer.ticksExisted % 7 == 0) {
			if ((mc.thePlayer.capabilities.isCreativeMode)
					|| (mc.thePlayer.openContainer != null) && (mc.thePlayer.openContainer.windowId != 0)) {
				return;
			}
			this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getSwordDamage));
			if (this.bestSword == null)
				return;
			boolean isInHBSlot = InventoryUtils.hotbarHas(this.bestSword.getItem(), 1 - 1);
			if (isInHBSlot) {
				if (InventoryUtils.getItemBySlotID(1 - 1) != null) {
					if (InventoryUtils.getItemBySlotID(1 - 1).getItem() instanceof ItemSword) {
						isInHBSlot = getSwordDamage(InventoryUtils.getItemBySlotID(1 - 1)) >= getSwordDamage(
								this.bestSword);
					}
				} else {
					isInHBSlot = false;
				}
			}
			if (this.prevBestSword == null || !(this.prevBestSword.equals(bestSword)) || !isInHBSlot) {
				this.shouldSwitch = true;
				this.prevBestSword = this.bestSword;
			} else
				this.shouldSwitch = false;

			if (this.shouldSwitch && timer.hasReached(1)) {
				int slotHB = InventoryUtils.getBestSwordSlotID(this.bestSword, getSwordDamage(this.bestSword));
				switch (slotHB) {
				case 0:
					slotHB = 36;
					break;
				case 1:
					slotHB = 37;
					break;
				case 2:
					slotHB = 38;
					break;
				case 3:
					slotHB = 39;
					break;
				case 4:
					slotHB = 40;
					break;
				case 5:
					slotHB = 41;
					break;
				case 6:
					slotHB = 42;
					break;
				case 7:
					slotHB = 43;
					break;
				case 8:
					slotHB = 44;
					break;
				default:
					break;
				}
				mc.playerController.windowClick(0, slotHB, 1 - 1, 2, mc.thePlayer);
				timer.reset();
			}
		}
	}

	private ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
		Optional<ItemStack> bestItem = ((List<Slot>) mc.thePlayer.inventoryContainer.inventorySlots).stream()
				.map(Slot::getStack).filter(Objects::nonNull)
				.filter(itemStack -> itemStack.getItem().getClass().equals(itemType)).max(comparator);
		return bestItem.orElse(null);
	}

	private double getSwordDamage(ItemStack itemStack) {
		double damage = 0;

		Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

		if (attributeModifier.isPresent()) {
			damage = attributeModifier.get().getAmount();
		}

		damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

		return damage;
	}

}