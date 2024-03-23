package xyz.northclient.features.modules;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import org.lwjgl.input.Keyboard;
import xyz.northclient.features.*;
import xyz.northclient.features.events.MotionEvent;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.util.MoveUtil;
import xyz.northclient.util.Stopwatch;

import java.util.ArrayList;

@ModuleInfo(name = "Manager", description = "", category = Category.WORLD, keyCode = Keyboard.KEY_Z)
public class Manager extends AbstractModule {
    private final int INVENTORY_ROWS = 4, INVENTORY_COLUMNS = 9, ARMOR_SLOTS = 4;
    private final int INVENTORY_SLOTS = INVENTORY_ROWS * INVENTORY_COLUMNS + ARMOR_SLOTS;

    private EntityPlayer player;
    private PlayerControllerMP playerController;

    private boolean changingSettings;

    private final Stopwatch timer = new Stopwatch();
    private boolean movedItem;
    private boolean inventoryOpen;
    private int ticksSinceChest;

    public DoubleValue delay = new DoubleValue("Delay", this)
            .setDefault(200)
            .setMin(0)
            .setMax(400)
            .enableOnlyInt();
    public BoolValue onlyInv = new BoolValue("Open inventory", this)
            .setDefault(true);
    public BoolValue dropItems = new BoolValue("Drop items", this)
            .setDefault(true);

    @EventLink
    public void onMove(MotionEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            ticksSinceChest = 0;
        } else {
            ticksSinceChest++;
        }
        if (ticksSinceChest <= 20) {
            return;
        }

        if (!timer.elapsed(delay.get().intValue())) {
            return;
        }
        timer.reset();

        if (MoveUtil.isMoving())
            return;

        if (!(mc.currentScreen instanceof GuiInventory) && onlyInv.get().booleanValue()) {
            return;
        }

        movedItem = false;

        player = mc.thePlayer;
        playerController = mc.playerController;

        /* Looping through all inventory slots to remove blacklisted items */
        for (int i = 0; i < INVENTORY_SLOTS; ++i) {
            final ItemStack itemStack = player.inventory.getStackInSlot(i);

            if (itemStack == null || itemStack.getItem() == null) continue;

            if (!itemWhitelisted(itemStack)) {
                throwItem(getSlotId(i));
            }
        }

        Integer bestHelmet = null;
        Integer bestChestPlate = null;
        Integer bestLeggings = null;
        Integer bestBoots = null;
        Integer bestSword = null;
        Integer bestPickaxe = null;
        Integer bestAxe = null;
        Integer bestShovel = null;
        Integer bestBlock = null;
        Integer bestPotion = null;

        for (int i = 0; i < INVENTORY_SLOTS; ++i) {
            final ItemStack itemStack = player.inventory.getStackInSlot(i);

            if (itemStack == null || itemStack.getItem() == null) continue;

            final Item item = itemStack.getItem();

            if (item instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) item;
                final int damageReductionItem = getArmorDamageReduction(itemStack);

                /* Helmet */
                if (armor.armorType == 0) {
                    if (bestHelmet == null || damageReductionItem > getArmorDamageReduction(player.inventory.getStackInSlot(bestHelmet))) {
                        bestHelmet = i;
                    }
                }

                /* Chestplate */
                if (armor.armorType == 1) {
                    if (bestChestPlate == null || damageReductionItem > getArmorDamageReduction(player.inventory.getStackInSlot(bestChestPlate))) {
                        bestChestPlate = i;
                    }
                }

                /* Leggings */
                if (armor.armorType == 2) {
                    if (bestLeggings == null || damageReductionItem > getArmorDamageReduction(player.inventory.getStackInSlot(bestLeggings))) {
                        bestLeggings = i;
                    }
                }

                /* Boots */
                if (armor.armorType == 3) {
                    if (bestBoots == null || damageReductionItem > getArmorDamageReduction(player.inventory.getStackInSlot(bestBoots))) {
                        bestBoots = i;
                    }
                }

            }

            /* Sword */
            if (item instanceof ItemSword) {
                final float damage = getSwordDamage(itemStack);
                if (bestSword == null || damage > getSwordDamage(player.inventory.getStackInSlot(bestSword))) {
                    bestSword = i;
                }
            }

            /* Pickaxe */
            if (item instanceof ItemPickaxe) {
                final float mineSpeed = getMineSpeed(itemStack);
                if (bestPickaxe == null || mineSpeed > getMineSpeed(player.inventory.getStackInSlot(bestPickaxe))) {
                    bestPickaxe = i;
                }
            }

            /* Axe */
            if (item instanceof ItemAxe) {
                final float mineSpeed = getMineSpeed(itemStack);
                if (bestAxe == null || mineSpeed > getMineSpeed(player.inventory.getStackInSlot(bestAxe))) {
                    bestAxe = i;
                }
            }

            /* Spade */
            if (item instanceof ItemSpade) {
                final float mineSpeed = getMineSpeed(itemStack);
                if (bestShovel == null || mineSpeed > getMineSpeed(player.inventory.getStackInSlot(bestShovel))) {
                    bestShovel = i;
                }
            }

            /* Blocks */
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().isFullCube()) {
                final float amountOfBlocks = itemStack.stackSize;
                if (bestBlock == null || amountOfBlocks > player.inventory.getStackInSlot(bestBlock).stackSize) {
                    bestBlock = i;
                }
            }

            /* Potions */
            if (item instanceof ItemPotion) {
                final ItemPotion itemPotion = (ItemPotion) item;
                if (bestPotion == null && ItemPotion.isSplash(itemStack.getMetadata()) && itemPotion.getEffects(itemStack.getMetadata()) != null) {
                    final int potionID = itemPotion.getEffects(itemStack.getMetadata()).get(0).getPotionID();
                    boolean isPotionActive = false;

                    for (final PotionEffect potion : player.getActivePotionEffects()) {
                        if (potion.getPotionID() == potionID && potion.getDuration() > 0) {
                            isPotionActive = true;
                            break;
                        }
                    }

                    final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>() {{
                        add(1);
                        add(5);
                        add(8);
                        add(14);
                        add(12);
                        add(16);
                    }};

                    if (!isPotionActive && (whitelistedPotions.contains(potionID) || (potionID == 10 || potionID == 6) && mc.thePlayer.getHealth() < 13))
                        bestPotion = i;
                }
            }
        }

        /* Throws away armor that isn't considered the best */
        for (int i = 0; i < INVENTORY_SLOTS; ++i) {
            final ItemStack itemStack = player.inventory.getStackInSlot(i);

            if (itemStack == null || itemStack.getItem() == null) continue;

            final Item item = itemStack.getItem();

            /* Throws Useless armor */
            if (item instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) item;

                if ((armor.armorType == 0 && bestHelmet != null && i != bestHelmet)
                        || (armor.armorType == 1 && bestChestPlate != null && i != bestChestPlate)
                        || (armor.armorType == 2 && bestLeggings != null && i != bestLeggings)
                        || (armor.armorType == 3 && bestBoots != null && i != bestBoots)) {
                    throwItem(getSlotId(i));
                }
            }

            /* Throws useless swords */
            if (item instanceof ItemSword) {
                if (bestSword != null && i != bestSword) {
                    throwItem(getSlotId(i));
                }
            }

            /* Throws useless pickaxes */
            if (item instanceof ItemPickaxe) {
                if (bestPickaxe != null && i != bestPickaxe) {
                    throwItem(getSlotId(i));
                }
            }

            /* Throws useless axes */
            if (item instanceof ItemAxe) {
                if (bestAxe != null && i != bestAxe) {
                    throwItem(getSlotId(i));
                }
            }

            /* Throws useless shovels */
            if (item instanceof ItemSpade) {
                if (bestShovel != null && i != bestShovel) {
                    throwItem(getSlotId(i));
                }
            }
        }

        /* Equips best armor and tools if enabled */
        if (bestHelmet != null) equipArmor(getSlotId(bestHelmet));
        if (bestChestPlate != null) equipArmor(getSlotId(bestChestPlate));
        if (bestLeggings != null) equipArmor(getSlotId(bestLeggings));
        if (bestBoots != null) equipArmor(getSlotId(bestBoots));
        if (bestSword != null) {
            final int moveSwordTo = (int) 1;
            if (moveSwordTo != 10) moveItem(getSlotId(bestSword), getSlotId(moveSwordTo - 37));
        }
        if (bestPickaxe != null) {
            final int movePickaxeTo = (int) 2;
            if (movePickaxeTo != 10) moveItem(getSlotId(bestPickaxe), getSlotId(movePickaxeTo - 37));
        }
        if (bestAxe != null) {
            final int moveAxeTo = (int) 3;
            if (moveAxeTo != 10) moveItem(getSlotId(bestAxe), getSlotId(moveAxeTo - 37));
        }
        if (bestShovel != null) {
            final int moveShovelTo = (int) 4;
            if (moveShovelTo != 10) moveItem(getSlotId(bestShovel), getSlotId(moveShovelTo - 37));
        }
        if (bestBlock != null) {
            final int moveBlockTo = (int) 5;
            if (moveBlockTo != 10) moveItem(getSlotId(bestBlock), getSlotId(moveBlockTo - 37));
        }
        if (bestPotion != null) {
            final int movePotionTo = 6;
            if (mc.thePlayer.inventory.getStackInSlot((movePotionTo - 1)) == null || !(mc.thePlayer.inventory.getStackInSlot((movePotionTo - 1)).getItem() instanceof ItemPotion)) {
                if (movePotionTo != 10) moveItem(getSlotId(bestPotion), getSlotId(movePotionTo - 37));
            }
        }
    }


    private float getSwordDamage(final ItemStack itemStack) {
        final ItemSword sword = (ItemSword) itemStack.getItem();
        final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
        return (float) (sword.getDamageVsEntity() + level * 1.25);
    }

    private int getArmorDamageReduction(final ItemStack itemStack) {
        return ((ItemArmor) itemStack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
    }

    private void openInventoryIfNecessary() {
        if (!inventoryOpen) {
            inventoryOpen = true;
            mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }
    }

    private void closeInventoryIfNecessary() {
        if (inventoryOpen) {
            inventoryOpen = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
        }
    }

    private void throwItem(final int slot) {
        if (dropItems.get()) {
            try {
                if (!movedItem) {
                    playerController.windowClick(player.inventoryContainer.windowId, slot, 1, 4, player);
                    movedItem = true;
                }
            } catch (final IndexOutOfBoundsException ignored) {
            }
        }
    }

    private void moveItem(final int slot, final int newSlot) {
        try {
            if (slot != newSlot + 36 && !changingSettings && !movedItem) {
                playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, newSlot, 2, player);
                movedItem = true;
            }
        } catch (final IndexOutOfBoundsException ignored) {
        }
    }

    private void equipArmor(final int slot) {
        try {
            if (slot > 8 && !movedItem) {
                playerController.windowClick(player.inventoryContainer.windowId, slot, 0, 1, player);
                movedItem = true;
            }
        } catch (final IndexOutOfBoundsException ignored) {
        }
    }

    public int getSlotId(final int slot) {
        if (slot >= 36) return 8 - (slot - 36);
        if (slot < 9) return slot + 36;
        return slot;
    }

    private boolean itemWhitelisted(final ItemStack itemStack) {
        final ArrayList<Item> whitelistedItems = new ArrayList<Item>() {{
            add(Items.ender_pearl);
            add(Items.iron_ingot);
            add(Items.gold_ingot);
            add(Items.redstone);
            add(Items.diamond);
            add(Items.emerald);
            add(Items.quartz);
            add(Items.bow);
            add(Items.arrow);
            add(Items.fishing_rod);
        }};

        final Item item = itemStack.getItem();
        final String itemName = itemStack.getDisplayName();

        if (itemName.contains("Right Click") || itemName.contains("Click to Use") || itemName.contains("Players Finder"))
            return true;

        final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>() {{
            add(6);
            add(1);
            add(5);
            add(8);
            add(14);
            add(12);
            add(10);
            add(16);
        }};

        if (item instanceof ItemPotion) {
            final int potionID = getPotionId(itemStack);
            return whitelistedPotions.contains(potionID);
        }

        return (item instanceof ItemBlock
                && !(((ItemBlock) item).getBlock() instanceof BlockTNT)
                && !(((ItemBlock) item).getBlock() instanceof BlockSlime)
                && !(((ItemBlock) item).getBlock() instanceof BlockFalling))
                || item instanceof ItemAnvilBlock
                || item instanceof ItemSword
                || item instanceof ItemArmor
                || item instanceof ItemTool
                || item instanceof ItemFood
                || (item instanceof ItemSkull && !true)
                || (itemName.contains("\247") && !true)
                || whitelistedItems.contains(item)
                && !item.equals(Items.spider_eye);
    }

    private int getPotionId(final ItemStack potion) {
        final Item item = potion.getItem();

        try {
            if (item instanceof ItemPotion) {
                final ItemPotion p = (ItemPotion) item;
                return p.getEffects(potion.getMetadata()).get(0).getPotionID();
            }
        } catch (final NullPointerException ignored) {
        }

        return 0;
    }

    private float getMineSpeed(final ItemStack itemStack) {
        final Item item = itemStack.getItem();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);

        //Percentage of Efficiency per Level
        switch (level) {
            case 1:
                level = 30;
                break;
            case 2:
                level = 69;
                break;
            case 3:
                level = 120;
                break;
            case 4:
                level = 186;
                break;
            case 5:
                level = 271;
                break;

            default:
                level = 0;
                break;
        }

        if (item instanceof ItemPickaxe) {
            return ((ItemPickaxe) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        } else if (item instanceof ItemSpade) {
            return ((ItemSpade) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        } else if (item instanceof ItemAxe) {
            return ((ItemAxe) item).getToolMaterial().getEfficiencyOnProperMaterial() + level;
        }

        return 0;
    }

}