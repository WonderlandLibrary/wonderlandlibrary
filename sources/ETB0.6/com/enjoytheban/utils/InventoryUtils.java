package com.enjoytheban.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.DamageSource;

public class InventoryUtils {
	public static Minecraft mc = Minecraft.getMinecraft();

	public void dropSlot(final int slot) {
		final int windowId = new GuiInventory(mc.thePlayer).inventorySlots.windowId;
		mc.playerController.windowClick(windowId, slot, 1, 4, mc.thePlayer);
	}

	public static void updateInventory() {
		for (int index = 0; index < 44; index++) {
			try {
				int offset = index < 9 ? 36 : 0;

				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(index + offset, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[index]));
			} catch (Exception localException) {
			}
		}
	}

	public static ItemStack getStackInSlot(int slot) {
		return mc.thePlayer.inventory.getStackInSlot(slot);
	}
	
	public static boolean isBestArmorOfTypeInInv(ItemStack is) {
	      try {
	         if(is == null) {
	            return false;
	         }

	         if(is.getItem() == null) {
	            return false;
	         }

	         if(is.getItem() != null && !(is.getItem() instanceof ItemArmor)) {
	            return false;
	         }

	         ItemArmor ia = (ItemArmor)is.getItem();
	         int prot = getArmorProt(is);

	         int i;
	         ItemStack stack;
	         ItemArmor otherArmor;
	         int otherProt;
	         for(i = 0; i < 4; ++i) {
	            stack = mc.thePlayer.inventory.armorInventory[i];
	            if(stack != null) {
	               otherArmor = (ItemArmor)stack.getItem();
	               if(otherArmor.armorType == ia.armorType) {
	                  otherProt = getArmorProt(stack);
	                  if(otherProt >= prot) {
	                     return false;
	                  }
	               }
	            }
	         }

	         for(i = 0; i < mc.thePlayer.inventory.getSizeInventory() - 4; ++i) {
	            stack = mc.thePlayer.inventory.getStackInSlot(i);
	            if(stack != null && stack.getItem() instanceof ItemArmor) {
	               otherArmor = (ItemArmor)stack.getItem();
	               if(otherArmor.armorType == ia.armorType && otherArmor != ia) {
	                  otherProt = getArmorProt(stack);
	                  if(otherProt >= prot) {
	                     return false;
	                  }
	               }
	            }
	         }
	      } catch (Exception var7) {
	         ;
	      }

	      return true;
	   }
	
    public static boolean hotbarHas(final Item item) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hotbarHas(final Item item, int slotID) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item && InventoryUtils.getSlotID(stack.getItem()) == slotID) {
                return true;
            }
        }
        return false;
    }
	
    public static int getSlotID(final Item item) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                return index;
            }
        }
        return -1;
    }
	
    public static ItemStack getItemBySlotID(int slotID) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && InventoryUtils.getSlotID(stack.getItem()) == slotID) {
                return stack;
            }
        }
        return null;
    }
	
	public static int getArmorProt(ItemStack i) {
	      int armorprot = -1;
	      if(i != null && i.getItem() != null && i.getItem() instanceof ItemArmor) {
	         armorprot = ((ItemArmor)i.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(i)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{i}, DamageSource.generic);
	      }

	      return armorprot;
	   }
	
    public static int getBestSwordSlotID(final ItemStack item, double damage) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack == item && getSwordDamage(stack) == getSwordDamage(item)) {
                return index;
            }
        }
        return -1;
    }
    
    private static double getSwordDamage(ItemStack itemStack) {
        double damage = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            damage = attributeModifier.get().getAmount();
        }

        damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

        return damage;
    }


	public boolean isBestChest(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[2] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[2].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[2];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 1 && ia2.armorType == 1) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestHelmet(int slot) {
		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[3] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[3].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[3];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 0 && ia2.armorType == 0) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}

				}

			}
		}

		return true;
	}

	public boolean isBestLeggings(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
			if (mc.thePlayer.inventory.armorInventory[1] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[1].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[1];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 2 && ia2.armorType == 2) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestBoots(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[0] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[0].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[0];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 3 && ia2.armorType == 3) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestSword(int slotIn) {
		return getBestWeapon() == slotIn;
	}

	public static int getItemType(ItemStack itemStack) {
		if ((itemStack.getItem() instanceof ItemArmor)) {
			ItemArmor armor = (ItemArmor) itemStack.getItem();

			return armor.armorType;
		}
		return -1;
	}

	public static float getItemDamage(ItemStack itemStack) {
		Multimap multimap = itemStack.getAttributeModifiers();
		Iterator iterator;
		if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
			Entry entry = (Entry) iterator.next();
			AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
			double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
					? attributeModifier.getAmount()
					: attributeModifier.getAmount() * 100.0D;
			return attributeModifier.getAmount() > 1.0D ? 1.0F + (float) damage : 1.0F;
		} else {
			return 1.0F;
		}
	}

	public boolean hasItemMoreTimes(int slotIn) {

		boolean has = false;
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		stacks.clear();

		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			if (!stacks.contains(getStackInSlot(i))) {
				stacks.add(getStackInSlot(i));
			} else {
				if (getStackInSlot(i) == getStackInSlot(slotIn)) {
					return true;
				}
			}

		}

		// for (ItemStack stack : stacks) {
		// if (stack == getStackInSlot(slotIn)) {
		// if (has) {
		// return true;
		// } else {
		// has = true;
		// }
		// }
		// }

		return false;
	}

	public int getBestWeaponInHotbar() {
		int originalSlot = mc.thePlayer.inventory.currentItem;
		byte weaponSlot = -1;
		float weaponDamage = 1.0F;

		for (byte slot = 0; slot < 9; ++slot) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
			if (itemStack != null) {
				float damage = getItemDamage(itemStack);
				if ((damage += EnchantmentHelper.func_152377_a(itemStack,
						EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}

		if (weaponSlot != -1) {
			return weaponSlot;
		} else {
			return originalSlot;
		}
	}

	public int getBestWeapon() {
		int originalSlot = mc.thePlayer.inventory.currentItem;
		byte weaponSlot = -1;
		float weaponDamage = 1.0F;

		for (byte slot = 0; slot < mc.thePlayer.inventory.getSizeInventory(); ++slot) {

			if (getStackInSlot(slot) == null) {
				continue;
			}
			ItemStack itemStack = getStackInSlot(slot);

			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemSword) {
				float damage = getItemDamage(itemStack);
				if ((damage += EnchantmentHelper.func_152377_a(itemStack,
						EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}

		if (weaponSlot != -1) {
			return weaponSlot;
		} else {
			return originalSlot;
		}
	}

	public int getArmorProt(int i) {
		int armorprot = -1;
		if (getStackInSlot(i) != null && getStackInSlot(i).getItem() != null
				&& getStackInSlot(i).getItem() instanceof ItemArmor) {
			armorprot = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
		}

		return armorprot;
	}

	public static int getFirstItem(Item i1) {
		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			if (getStackInSlot(i) != null && getStackInSlot(i).getItem() != null) {
				if (getStackInSlot(i).getItem() == i1) {
					return i;
				}
			}
		}
		return -1;
	}

	public static boolean isBestSword(ItemStack itemSword, int slot) {
	      if(itemSword != null && itemSword.getItem() instanceof ItemSword) {
	         for(int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); ++i) {
	            ItemStack iStack = mc.thePlayer.inventory.getStackInSlot(i);
	            if(iStack != null && iStack.getItem() instanceof ItemSword && getItemDamage(iStack) >= getItemDamage(itemSword) && slot != i) {
	               return false;
	            }
	         }
	      }

	      return true;
	   }


}
