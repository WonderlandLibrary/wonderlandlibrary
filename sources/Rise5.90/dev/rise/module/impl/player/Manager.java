/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.RandomUtil;
import dev.rise.util.math.TimeUtil;
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

import java.util.ArrayList;
import java.util.Objects;

@ModuleInfo(name = "Manager", description = "Manages your items for you", category = Category.PLAYER)
public final class Manager extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting mode = new ModeSetting("Mode", this, "None", "None", "Legit", "Open Inventory", "Packet", "Packet Spam");

    private final NoteSetting bypassSettings = new NoteSetting("Bypass Settings", this);
    private final NumberSetting minDelay = new NumberSetting("Min Delay", this, 100, 0, 1000, 25);
    private final NumberSetting maxDelay = new NumberSetting("Max Delay", this, 150, 0, 1000, 25);

    private final NoteSetting throwSettings = new NoteSetting("Throw Settings", this);
    private final BooleanSetting throwUselessItems = new BooleanSetting("Throw Useless Items", this, true);
    private final BooleanSetting throwCustomItems = new BooleanSetting("Throw Custom Items", this, true);
    private final BooleanSetting throwHeads = new BooleanSetting("Throw Heads", this, true);

    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final BooleanSetting equipBestGear = new BooleanSetting("Equip Best Gear", this, true);
    private final NumberSetting swordSlot = new NumberSetting("Sword Slot", this, 1, 1, 10, 1, "10-None");
    private final NumberSetting pickaxeSlot = new NumberSetting("Pickaxe Slot", this, 2, 1, 10, 1, "10-None");
    private final NumberSetting axeSlot = new NumberSetting("Axe Slot", this, 3, 1, 10, 1, "10-None");
    private final NumberSetting shovelSlot = new NumberSetting("Shovel Slot", this, 4, 1, 10, 1, "10-None");
    private final NumberSetting blockSlot = new NumberSetting("Block Slot", this, 5, 1, 10, 1, "10-None");
    private final NumberSetting potionSlot = new NumberSetting("Potion Slot", this, 6, 1, 10, 1, "10-None");

    private final int INVENTORY_ROWS = 4, INVENTORY_COLUMNS = 9, ARMOR_SLOTS = 4;
    private final int INVENTORY_SLOTS = INVENTORY_ROWS * INVENTORY_COLUMNS + ARMOR_SLOTS;

    private EntityPlayer player;
    private PlayerControllerMP playerController;

    private boolean changingSettings;

    private final TimeUtil timer = new TimeUtil();
    private Integer delay = null;
    private boolean movedItem;
    private boolean inventoryOpen;
    private int ticksSinceChest;

    @Override
    protected void onEnable() {
        timer.reset();
    }

    @Override
    protected void onDisable() {
        closeInventoryIfNecessary();
    }

    @Override
    public void onUpdateAlwaysInGui() {
        swordSlot.hidden = pickaxeSlot.hidden = shovelSlot.hidden = axeSlot.hidden = blockSlot.hidden = potionSlot.hidden = !equipBestGear.isEnabled();

        changingSettings = false;
        final ArrayList<Integer> savedUsed = new ArrayList<>();
        for (final Setting setting : settings) {
            if (setting instanceof NumberSetting) {
                final NumberSetting numberSetting = ((NumberSetting) setting);

                final int value = (int) numberSetting.getValue();

                if (value == 10 || !numberSetting.getName().contains("Slot")) continue;

                if (savedUsed.contains(value)) {
                    if (mc.currentScreen == null) {
                        numberSetting.setValue(10);
                    }
                    changingSettings = true;
                } else {
                    savedUsed.add(value);
                }
            }
        }

        if (minDelay.getValue() > maxDelay.getValue())
            minDelay.setValue(maxDelay.getValue());
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (movedItem && mode.is("Legit")) {
            event.setJump(false);
            event.setForward(false);
            event.setSneak(false);
            event.setBackward(false);
            event.setRight(false);
            event.setLeft(false);
            movedItem = false;
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.currentScreen instanceof GuiChest) {
            ticksSinceChest = 0;
        } else {
            ticksSinceChest++;
        }

        if (ticksSinceChest <= 10) {
            return;
        }

        if (this.getModule(Scaffold.class).isEnabled() || mc.thePlayer.isSwingInProgress) {
            return;
        }

        if (delay == null) {
            delay = RandomUtil.randomRange((int) minDelay.getValue(), (int) maxDelay.getValue());
        }

        if (!timer.hasReached(delay)) {
            if (mode.is("Legit")) {
                closeInventoryIfNecessary();
            }
            return;
        }

        movedItem = false;
        timer.reset();
        delay = RandomUtil.randomRange((int) minDelay.getValue(), (int) maxDelay.getValue());

        /* Allows the mode of the manager to function */
        switch (mode.getMode()) {
            case "Open Inventory":
                if (!(mc.currentScreen instanceof GuiInventory)) return;
                break;
        }

        player = mc.thePlayer;
        playerController = mc.playerController;

        /* Looping through all inventory slots to remove unwhitelisted or blacklisted items */
        if (throwUselessItems.isEnabled()) {
            for (int i = 0; i < INVENTORY_SLOTS; ++i) {
                final ItemStack itemStack = player.inventory.getStackInSlot(i);

                if (itemStack == null || itemStack.getItem() == null) continue;

                if (!itemWhitelisted(itemStack)) {
                    throwItem(getSlotId(i));
                }
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
                final float amountOfBlocks = itemStack.getStackSize();
                if (bestBlock == null || amountOfBlocks > player.inventory.getStackInSlot(bestBlock).getStackSize()) {
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

                    if (!isPotionActive && (whitelistedPotions.contains(potionID) || (potionID == 10 || potionID == 6) && mc.thePlayer.getHealth() < ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("AutoPot", "Health"))).getValue()))
                        bestPotion = i;
                }
            }
        }

        /* Throws away armor that isn't considered the best */
        if (throwUselessItems.isEnabled()) {
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
        }

        /* Equips best armor and tools if enabled */
        if (equipBestGear.isEnabled()) {
            if (bestHelmet != null) equipArmor(getSlotId(bestHelmet));
            if (bestChestPlate != null) equipArmor(getSlotId(bestChestPlate));
            if (bestLeggings != null) equipArmor(getSlotId(bestLeggings));
            if (bestBoots != null) equipArmor(getSlotId(bestBoots));
            if (bestSword != null) {
                final int moveSwordTo = (int) swordSlot.getValue();
                if (moveSwordTo != 10) moveItem(getSlotId(bestSword), getSlotId(moveSwordTo - 37));
            }
            if (bestPickaxe != null) {
                final int movePickaxeTo = (int) pickaxeSlot.getValue();
                if (movePickaxeTo != 10) moveItem(getSlotId(bestPickaxe), getSlotId(movePickaxeTo - 37));
            }
            if (bestAxe != null) {
                final int moveAxeTo = (int) axeSlot.getValue();
                if (moveAxeTo != 10) moveItem(getSlotId(bestAxe), getSlotId(moveAxeTo - 37));
            }
            if (bestShovel != null) {
                final int moveShovelTo = (int) shovelSlot.getValue();
                if (moveShovelTo != 10) moveItem(getSlotId(bestShovel), getSlotId(moveShovelTo - 37));
            }
            if (bestBlock != null) {
                final int moveBlockTo = (int) blockSlot.getValue();
                if (moveBlockTo != 10) moveItem(getSlotId(bestBlock), getSlotId(moveBlockTo - 37));
            }
            if (bestPotion != null) {
                final int movePotionTo = (int) potionSlot.getValue();
                if (mc.thePlayer.inventory.getStackInSlot((movePotionTo - 1)) == null || !(mc.thePlayer.inventory.getStackInSlot((movePotionTo - 1)).getItem() instanceof ItemPotion)) {
                    if (movePotionTo != 10) moveItem(getSlotId(bestPotion), getSlotId(movePotionTo - 37));
                }
            }
        }

        /* Allows the mode of the manager to function */
        switch (mode.getMode()) {
            case "Packet Spam":
                closeInventoryIfNecessary();
                break;

            case "Packet":
                if (!movedItem) {
                    closeInventoryIfNecessary();
                }
                break;
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
        try {
            if (!movedItem) {
                switch (mode.getMode()) {
                    case "Packet Spam":
                    case "Packet":
                    case "Legit":
                        openInventoryIfNecessary();
                        break;
                }
                playerController.windowClick(player.inventoryContainer.windowId, slot, 1, 4, player);
                movedItem = true;
            }
        } catch (final IndexOutOfBoundsException ignored) {
        }
    }

    private void moveItem(final int slot, final int newSlot) {
        try {
            if (slot != newSlot + 36 && !changingSettings && !movedItem) {
                switch (mode.getMode()) {
                    case "Packet Spam":
                    case "Packet":
                    case "Legit":
                        openInventoryIfNecessary();
                        break;
                }
                playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, newSlot, 2, player);
                movedItem = true;
            }
        } catch (final IndexOutOfBoundsException ignored) {
        }
    }

    private void equipArmor(final int slot) {
        try {
            if (slot > 8 && !movedItem) {
                switch (mode.getMode()) {
                    case "Packet Spam":
                    case "Packet":
                    case "Legit":
                        openInventoryIfNecessary();
                        break;
                }
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
                || (item instanceof ItemSkull && !throwHeads.isEnabled())
                || (itemName.contains("\247") && !throwCustomItems.isEnabled())
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