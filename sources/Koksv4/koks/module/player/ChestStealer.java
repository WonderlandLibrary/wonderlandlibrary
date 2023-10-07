package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.InventoryUtil;
import koks.api.utils.RandomUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.GuiHandleEvent;
import koks.event.UpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "ChestStealer", category = Module.Category.PLAYER, description = "You steal the items from a chest")
public class ChestStealer extends Module implements Module.Unsafe {

    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper startTimer = new TimeHelper();
    private final List<Integer> itemsToSteal = new ArrayList<>();

    @Value(minimum = 0, maximum = 500, name = "Start Delay")
    int startDelay = 150;

    @Value(minimum = 0, maximum = 500, name = "Grab Delay")
    int grabDelay = 150;

    @Value(name = "Make it Intelligent")
    boolean intelligent = true;

    @Value(name = "StackItems")
    boolean stackItems = true;

    @Value(name = "RandomPick")
    boolean randomPick = true;

    @Value(name = "Auto Close")
    boolean autoClose = true;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof GuiHandleEvent) {
            final InventoryUtil inventoryUtil = InventoryUtil.getInstance();
            if (mc.currentScreen == null) {
                timeHelper.reset();
                startTimer.reset();
                return;
            }
            
            if (mc.currentScreen instanceof GuiChest) {
                if (!startTimer.hasReached((long) (startDelay + RandomUtil.getInstance().getRandomGaussian(20))))
                    return;
                itemsToSteal.clear();


                final ContainerChest chest = (ContainerChest) getPlayer().openContainer;
                final IInventory inventory = chest.getLowerChestInventory();
                boolean isEmpty = true;

                if (intelligent) {
                    addIntelligentSlotsToSteal();
                } else {
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        final ItemStack stack = inventory.getStackInSlot(i);
                        if (stack != null) {
                            itemsToSteal.add(i);
                        }
                    }
                }

                if (randomPick)
                    Collections.shuffle(itemsToSteal);

                for (final int i : itemsToSteal) {
                    final ItemStack stack = inventory.getStackInSlot(i);
                    if (stack != null) {
                        double random = this.grabDelay == 0 ? 0 : RandomUtil.getInstance().getRandomGaussian(20);
                        if (!timeHelper.hasReached((long) (grabDelay + random)))
                            return;
                        if (stackItems && stack.stackSize != 64 && stack.getMaxStackSize() != 1 && inventoryUtil.getItemSize(stack.getItem(), inventory) != 0 && inventoryUtil.getItemSize(stack.getItem(), inventory) != 1) {
                            getPlayerController().windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                            getPlayerController().windowClick(chest.windowId, i, 0, 6, mc.thePlayer);
                            getPlayerController().windowClick(chest.windowId, i, 0, 0, mc.thePlayer);
                            getPlayerController().windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                        } else {
                            getPlayerController().windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                        }
                        timeHelper.reset();
                        isEmpty = false;
                    }
                }

                if (isEmpty && autoClose)
                    getPlayer().closeScreen();
            }
        }
    }

    private void addIntelligentSlotsToSteal() {
        float bestSwordDamage = -1, bestBowDamage = -1, bestPickAxeStrength = -1, bestAxeStrength = -1;
        float bestBootsProtection = -1, bestLeggingsProtection = -1, bestChestPlateProtection = -1, bestHelmetProtection = -1;
        //searching in inventory
        for (int i = 9; i < 45; i++) {
            final Slot slot = getPlayer().inventoryContainer.getSlot(i);
            if (slot != null && slot.getStack() != null) {
                final ItemStack stack = slot.getStack();
                if (stack != null) {
                    final Item item = stack.getItem();
                    if (item instanceof ItemSword) {
                        final float damage = 4 + ((ItemSword) stack.getItem()).getDamageVsEntity() + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f);
                        if (damage > bestSwordDamage) {
                            bestSwordDamage = damage;
                        }
                    } else if (item instanceof ItemBow) {
                        bestBowDamage = 4.5f;
                    } else if (item instanceof ItemTool) {
                        Block block = null;
                        if (item instanceof ItemPickaxe) {
                            block = Blocks.stone;
                        } else if (item instanceof ItemAxe) {
                            block = Blocks.log;
                        }
                        final float toolStrength = item.getStrVsBlock(stack, block);
                        if (item instanceof ItemPickaxe) {
                            if (toolStrength > bestPickAxeStrength) {
                                bestPickAxeStrength = toolStrength;
                            }
                        } else if (item instanceof ItemAxe) {
                            if (toolStrength > bestAxeStrength) {
                                bestAxeStrength = toolStrength;
                            }
                        }
                    } else if (item instanceof ItemArmor) {
                        final float prot = ((ItemArmor) stack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.04f;
                        switch (((ItemArmor) item).armorType) {
                            case 0:
                                if (prot > bestHelmetProtection) {
                                    bestHelmetProtection = prot;
                                }
                                break;
                            case 1:
                                if (prot > bestChestPlateProtection) {
                                    bestChestPlateProtection = prot;
                                }
                                break;
                            case 2:
                                if (prot > bestLeggingsProtection) {
                                    bestLeggingsProtection = prot;
                                }
                                break;
                            case 3:
                                if (prot > bestBootsProtection) {
                                    bestBootsProtection = prot;
                                }
                                break;
                        }
                    }
                }
            }
        }

        //searching chest and adding the items
        int bestSwordSlot = -1, bestBowSlot = -1, bestPickAxeSlot = -1, bestAxeSlot = -1, bestBootsSlot = -1, bestLeggingsSlot = -1, bestChestPlateSlot = -1, bestHelmetSlot = -1;
        for (int i = 0; i < ((ContainerChest) getPlayer().openContainer).getLowerChestInventory().getSizeInventory(); i++) {
            final ItemStack stack = getPlayer().openContainer.getSlot(i).getStack();
            if (stack != null) {
                final Item item = getPlayer().openContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemSword) {
                    final float damage = 4 + ((ItemSword) stack.getItem()).getDamageVsEntity() + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f);
                    if (damage > bestSwordDamage) {
                        bestSwordDamage = damage;
                        bestSwordSlot = i;
                    }
                } else if (item instanceof ItemBow) {
                    if (4.5f > bestBowDamage) {
                        bestBowDamage = 4.5f;
                        bestBowSlot = i;
                    }
                } else if (item instanceof ItemTool) {
                    Block block = null;
                    if (item instanceof ItemPickaxe) {
                        block = Blocks.stone;
                    } else if (item instanceof ItemAxe) {
                        block = Blocks.log;
                    }
                    final float toolStrength = item.getStrVsBlock(stack, block);
                    if (item instanceof ItemPickaxe) {
                        if (toolStrength > bestPickAxeStrength) {
                            bestPickAxeStrength = toolStrength;
                            bestPickAxeSlot = i;
                        }
                    } else if (item instanceof ItemAxe) {
                        if (toolStrength > bestAxeStrength) {
                            bestAxeStrength = toolStrength;
                            bestAxeSlot = i;
                        }
                    }
                } else if (item instanceof ItemArmor) {
                    final float prot = ((ItemArmor) stack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.04f;
                    switch (((ItemArmor) item).armorType) {
                        case 0:
                            if (prot > bestHelmetProtection) {
                                bestHelmetProtection = prot;
                                bestHelmetSlot = i;
                            }
                            break;
                        case 1:
                            if (prot > bestChestPlateProtection) {
                                bestChestPlateProtection = prot;
                                bestChestPlateSlot = i;
                            }
                            break;
                        case 2:
                            if (prot > bestLeggingsProtection) {
                                bestLeggingsProtection = prot;
                                bestLeggingsSlot = i;
                            }
                            break;
                        case 3:
                            if (prot > bestBootsProtection) {
                                bestBootsProtection = prot;
                                bestBootsSlot = i;
                            }
                            break;
                    }
                } else {
                    itemsToSteal.add(i);
                }
            }
        }
        itemsToSteal.add(bestSwordSlot);
        itemsToSteal.add(bestBowSlot);
        itemsToSteal.add(bestPickAxeSlot);
        itemsToSteal.add(bestAxeSlot);
        itemsToSteal.add(bestHelmetSlot);
        itemsToSteal.add(bestChestPlateSlot);
        itemsToSteal.add(bestLeggingsSlot);
        itemsToSteal.add(bestBootsSlot);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
