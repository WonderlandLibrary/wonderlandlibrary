package rip.helium.cheat.impl.player;

import com.google.common.collect.Multimap;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.*;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.RunTickEvent;
import rip.helium.utils.TimerHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public class InventoryCleaner extends Cheat {
    public static TimerHelper timer;
    private boolean openInv;

    public InventoryCleaner() {
        super("Inventory Cleaner", "Cleans out your inventory from shit", CheatCategory.PLAYER);
    }


    @Collect
    public void onEvent(RunTickEvent event) {


        if (Minecraft.getMinecraft().thePlayer.isUsingItem())
            return;

        if (!openInv || (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && openInv)) {

            CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList<Integer>();

            for (int o = 0; o < 45; ++o) {
                if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
                    if (Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(0) == item
                            || Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(1) == item
                            || Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2) == item
                            || Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(3) == item)
                        continue;
                    if (item != null && item.getItem() != null && Item.getIdFromItem(item.getItem()) != 0
                            && !stackIsUseful(o)) {
                        uselessItems.add(o);
                    }
                }
            }

            if (!uselessItems.isEmpty()) {
                Minecraft.getMinecraft().playerController.windowClick(
                        Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, uselessItems.get(0), 1, 4,
                        Minecraft.getMinecraft().thePlayer);
                uselessItems.remove(0);
            } else {
                return;
            }

        }

    }

    private boolean stackIsUseful(int i) {
        ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();

        boolean hasAlreadyOrBetter = false;

        if (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemPickaxe
                || itemStack.getItem() instanceof ItemAxe) {
            for (int o = 0; o < 45; ++o) {
                if (o == i)
                    continue;
                if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
                    if (item != null && item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
                            || item.getItem() instanceof ItemPickaxe) {
                        float damageFound = getItemDamage(itemStack);
                        damageFound += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

                        float damageCurrent = getItemDamage(item);
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
                if (i == o)
                    continue;
                if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
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
            if (i == o)
                continue;
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                ItemStack item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(o).getStack();
                if (item != null && (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
                        || item.getItem() instanceof ItemBow || item.getItem() instanceof ItemFishingRod
                        || item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemAxe
                        || item.getItem() instanceof ItemPickaxe || Item.getIdFromItem(item.getItem()) == 346)) {
                    Item found = item.getItem();
                    if (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())) {
                        hasAlreadyOrBetter = true;
                        break;
                    }
                }
            }
        }

        if (Item.getIdFromItem(itemStack.getItem()) == 367)
            return false; // rotten flesh
        if (Item.getIdFromItem(itemStack.getItem()) == 30)
            return true; // cobweb
        if (Item.getIdFromItem(itemStack.getItem()) == 259)
            return true; // flint & steel
        if (Item.getIdFromItem(itemStack.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(itemStack.getItem()) == 264)
            return true; // diamond
        if (Item.getIdFromItem(itemStack.getItem()) == 265)
            return true; // iron
        if (Item.getIdFromItem(itemStack.getItem()) == 346)
            return true; // fishing rod
        if (Item.getIdFromItem(itemStack.getItem()) == 384)
            return true; // bottle o' enchanting
        if (Item.getIdFromItem(itemStack.getItem()) == 345)
            return true; // compass
        if (Item.getIdFromItem(itemStack.getItem()) == 296)
            return true; // wheat
        if (Item.getIdFromItem(itemStack.getItem()) == 336)
            return true; // brick
        if (Item.getIdFromItem(itemStack.getItem()) == 266)
            return true; // gold ingot
        if (Item.getIdFromItem(itemStack.getItem()) == 280)
            return true; // stick
        if (itemStack.hasDisplayName())
            return true;

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
        return itemStack.getItem() instanceof ItemPickaxe;
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

}
