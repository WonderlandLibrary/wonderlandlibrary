// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.utils.Utils;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.utils.server.PacketUtils;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.init.Items;
import dev.tenacity.utils.player.BlockUtils;
import net.minecraft.item.ItemBlock;
import java.util.function.Predicate;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import dev.tenacity.event.impl.network.PacketSendEvent;
import net.minecraft.inventory.Slot;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.GuiChat;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.potion.Potion;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import java.util.List;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class InvManager extends Module
{
    private BooleanSetting inventoryPackets;
    private BooleanSetting onlyWhileNotMoving;
    private BooleanSetting inventoryOnly;
    private BooleanSetting swapBlocks;
    private BooleanSetting dropArchery;
    private BooleanSetting moveArrows;
    private BooleanSetting dropFood;
    private BooleanSetting dropShears;
    private final MultipleBoolSetting options;
    private final NumberSetting delay;
    private static final NumberSetting slotWeapon;
    private static final NumberSetting slotPick;
    private static final NumberSetting slotAxe;
    private static final NumberSetting slotShovel;
    private static final NumberSetting slotBow;
    private static final NumberSetting slotBlock;
    private static final NumberSetting slotGapple;
    private final String[] blacklist;
    private final String[] serverItems;
    private final List<Integer> badPotionIDs;
    private final TimerUtil timer;
    private boolean isInvOpen;
    
    public InvManager() {
        super("InvManager", "Inv Manager", Category.PLAYER, "cleans up your inventory");
        this.options = new MultipleBoolSetting("Options", new BooleanSetting[] { this.inventoryPackets = new BooleanSetting("Send inventory packets", true), this.onlyWhileNotMoving = new BooleanSetting("Only while not moving", false), this.inventoryOnly = new BooleanSetting("Inventory only", false), this.swapBlocks = new BooleanSetting("Swap blocks", false), this.dropArchery = new BooleanSetting("Drop archery", false), this.moveArrows = new BooleanSetting("Move arrows", true), this.dropFood = new BooleanSetting("Drop food", false), this.dropShears = new BooleanSetting("Drop shears", true) });
        this.delay = new NumberSetting("Delay", 120.0, 300.0, 0.0, 10.0);
        this.blacklist = new String[] { "tnt", "stick", "egg", "string", "cake", "mushroom", "flint", "compass", "dyePowder", "feather", "bucket", "chest", "snow", "fish", "enchant", "exp", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "snowball", "piston" };
        this.serverItems = new String[] { "selector", "tracking compass", "(right click)", "tienda ", "perfil", "salir", "shop", "collectibles", "game", "profil", "lobby", "show all", "hub", "friends only", "cofre", "(click", "teleport", "play", "exit", "hide all", "jeux", "gadget", " (activ", "emote", "amis", "bountique", "choisir", "choose ", "recipe book", "click derecho", "todos", "teletransportador", "configuraci", "jugar de nuevo" };
        this.badPotionIDs = new ArrayList<Integer>(Arrays.asList(Potion.moveSlowdown.getId(), Potion.weakness.getId(), Potion.poison.getId(), Potion.harm.getId()));
        this.timer = new TimerUtil();
        this.inventoryPackets.addParent(this.inventoryOnly, ParentAttribute.BOOLEAN_CONDITION.negate());
        this.moveArrows.addParent(this.dropArchery, ParentAttribute.BOOLEAN_CONDITION.negate());
        InvManager.slotGapple.addParent(this.dropFood, ParentAttribute.BOOLEAN_CONDITION.negate());
        this.addSettings(this.options, this.delay, InvManager.slotWeapon, InvManager.slotPick, InvManager.slotAxe, InvManager.slotShovel, InvManager.slotBow, InvManager.slotBlock, InvManager.slotGapple);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPost() || this.canContinue()) {
            return;
        }
        if (!InvManager.mc.thePlayer.isUsingItem() && (InvManager.mc.currentScreen == null || InvManager.mc.currentScreen instanceof GuiChat || InvManager.mc.currentScreen instanceof GuiInventory || InvManager.mc.currentScreen instanceof GuiIngameMenu)) {
            if (this.isReady()) {
                final Slot slot = ItemType.WEAPON.getSlot();
                if (!slot.getHasStack() || !this.isBestWeapon(slot.getStack())) {
                    this.getBestWeapon();
                }
            }
            this.getBestPickaxe();
            this.getBestAxe();
            this.getBestShovel();
            this.dropItems();
            this.swapBlocks();
            this.getBestBow();
            this.moveArrows();
            this.moveFood();
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent e) {
        if (this.isInvOpen) {
            final Packet<?> packet = e.getPacket();
            if ((packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) || packet instanceof C0DPacketCloseWindow) {
                e.cancel();
            }
            else if (packet instanceof C02PacketUseEntity) {
                this.fakeClose();
            }
        }
    }
    
    private boolean isReady() {
        return this.timer.hasTimeElapsed(this.delay.getValue());
    }
    
    public static float getDamageScore(final ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return 0.0f;
        }
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemSword) {
            damage += ((ItemSword)item).getDamageVsEntity();
        }
        else if (item instanceof ItemTool) {
            damage += item.getMaxDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.1f;
        return damage;
    }
    
    public static float getProtScore(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075f;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0f;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0f;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0f;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 25.0f;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100.0f;
        }
        return prot;
    }
    
    private void dropItems() {
        if (!this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            if (this.canContinue()) {
                return;
            }
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            final ItemStack is = slot.getStack();
            if (is != null && this.isBadItem(is, i, false)) {
                InventoryUtils.drop(i);
                this.timer.reset();
                break;
            }
        }
    }
    
    private boolean isBestWeapon(final ItemStack is) {
        if (is == null) {
            return false;
        }
        final float damage = getDamageScore(is);
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is2 = slot.getStack();
                if (getDamageScore(is2) > damage && is2.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return is.getItem() instanceof ItemSword;
    }
    
    private void getBestWeapon() {
        for (int i = 9; i < 45; ++i) {
            final ItemStack is = InvManager.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() instanceof ItemSword && this.isBestWeapon(is) && getDamageScore(is) > 0.0f) {
                this.swap(i, ItemType.WEAPON.getDesiredSlot() - 36);
                break;
            }
        }
    }
    
    public boolean isBadItem(final ItemStack stack, final int slot, final boolean stealing) {
        final Item item = stack.getItem();
        final String stackName = stack.getDisplayName().toLowerCase();
        final String ulName = item.getUnlocalizedName();
        if (Arrays.stream(this.serverItems).anyMatch(stackName::contains)) {
            return stealing;
        }
        if (item instanceof ItemBlock) {
            return !BlockUtils.isValidBlock(((ItemBlock)item).getBlock(), true);
        }
        if (stealing) {
            if (this.isBestWeapon(stack) || this.isBestAxe(stack) || this.isBestPickaxe(stack) || this.isBestBow(stack) || this.isBestShovel(stack)) {
                return false;
            }
            if (item instanceof ItemArmor) {
                for (int type = 1; type < 5; ++type) {
                    final ItemStack is = InvManager.mc.thePlayer.inventoryContainer.getSlot(type + 4).getStack();
                    if (is != null) {
                        String typeStr = "";
                        switch (type) {
                            case 1: {
                                typeStr = "helmet";
                                break;
                            }
                            case 2: {
                                typeStr = "chestplate";
                                break;
                            }
                            case 3: {
                                typeStr = "leggings";
                                break;
                            }
                            case 4: {
                                typeStr = "boots";
                                break;
                            }
                        }
                        if (stack.getUnlocalizedName().contains(typeStr) && getProtScore(is) > getProtScore(stack)) {
                            continue;
                        }
                    }
                    if (this.isBestArmor(stack, type)) {
                        return false;
                    }
                }
            }
        }
        final int weaponSlot = ItemType.WEAPON.getDesiredSlot();
        final int pickaxeSlot = ItemType.PICKAXE.getDesiredSlot();
        final int axeSlot = ItemType.AXE.getDesiredSlot();
        final int shovelSlot = ItemType.SHOVEL.getDesiredSlot();
        if (stealing || ((slot != weaponSlot || !this.isBestWeapon(ItemType.WEAPON.getStackInSlot())) && (slot != pickaxeSlot || !this.isBestPickaxe(ItemType.PICKAXE.getStackInSlot())) && (slot != axeSlot || !this.isBestAxe(ItemType.AXE.getStackInSlot())) && (slot != shovelSlot || !this.isBestShovel(ItemType.SHOVEL.getStackInSlot())))) {
            if (!stealing && item instanceof ItemArmor) {
                for (int type2 = 1; type2 < 5; ++type2) {
                    final ItemStack is2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(type2 + 4).getStack();
                    if (is2 == null || !this.isBestArmor(is2, type2)) {
                        if (this.isBestArmor(stack, type2)) {
                            return false;
                        }
                    }
                }
            }
            return item == Items.wheat || item == Items.spawn_egg || (item instanceof ItemFood && this.dropFood.isEnabled() && !(item instanceof ItemAppleGold)) || (item instanceof ItemPotion && this.isBadPotion(stack)) || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemArmor || (this.dropArchery.isEnabled() && (item instanceof ItemBow || item == Items.arrow)) || (this.dropShears.isEnabled() && ulName.contains("shears")) || item instanceof ItemGlassBottle || Arrays.stream(this.blacklist).anyMatch(ulName::contains);
        }
        return false;
    }
    
    private void getBestPickaxe() {
        if (!this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.isBestPickaxe(is) && !this.isBestWeapon(is)) {
                    final int desiredSlot = ItemType.PICKAXE.getDesiredSlot();
                    if (i == desiredSlot) {
                        return;
                    }
                    final Slot slot2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !this.isBestPickaxe(slot2.getStack())) {
                        this.swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }
    
    private void getBestAxe() {
        if (!this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.isBestAxe(is) && !this.isBestWeapon(is)) {
                    final int desiredSlot = ItemType.AXE.getDesiredSlot();
                    if (i == desiredSlot) {
                        return;
                    }
                    final Slot slot2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !this.isBestAxe(slot2.getStack())) {
                        this.swap(i, desiredSlot - 36);
                        this.timer.reset();
                    }
                }
            }
        }
    }
    
    private void getBestShovel() {
        if (!this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.isBestShovel(is) && !this.isBestWeapon(is)) {
                    final int desiredSlot = ItemType.SHOVEL.getDesiredSlot();
                    if (i == desiredSlot) {
                        return;
                    }
                    final Slot slot2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !this.isBestShovel(slot2.getStack())) {
                        this.swap(i, desiredSlot - 36);
                        this.timer.reset();
                    }
                }
            }
        }
    }
    
    private void getBestBow() {
        if (!this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                final String stackName = is.getDisplayName().toLowerCase();
                if (!Arrays.stream(this.serverItems).anyMatch(stackName::contains)) {
                    if (is.getItem() instanceof ItemBow) {
                        if (this.isBestBow(is) && !this.isBestWeapon(is)) {
                            final int desiredSlot = ItemType.BOW.getDesiredSlot();
                            if (i == desiredSlot) {
                                return;
                            }
                            final Slot slot2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                            if (!slot2.getHasStack() || !this.isBestBow(slot2.getStack())) {
                                this.swap(i, desiredSlot - 36);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void moveArrows() {
        if (this.dropArchery.isEnabled() || !this.moveArrows.isEnabled() || !this.isReady()) {
            return;
        }
        for (int i = 36; i < 45; ++i) {
            final ItemStack is = InvManager.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() == Items.arrow) {
                for (int j = 0; j < 36; ++j) {
                    if (InvManager.mc.thePlayer.inventoryContainer.getSlot(j).getStack() == null) {
                        this.fakeOpen();
                        InventoryUtils.click(i, 0, true);
                        this.fakeClose();
                        this.timer.reset();
                        break;
                    }
                }
            }
        }
    }
    
    private void moveFood() {
        if (this.dropFood.isEnabled() || !this.isReady()) {
            return;
        }
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.hasMostGapples(is)) {
                    final int desiredSlot = ItemType.GAPPLE.getDesiredSlot();
                    if (i == desiredSlot) {
                        return;
                    }
                    final Slot slot2 = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !this.hasMostGapples(slot2.getStack())) {
                        this.swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }
    
    private boolean hasMostGapples(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemAppleGold)) {
            return false;
        }
        final int value = stack.stackSize;
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (is.getItem() instanceof ItemAppleGold && is.stackSize > value) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private int getMostBlocks() {
        int stack = 0;
        int biggestSlot = -1;
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            final ItemStack is = slot.getStack();
            if (is != null && is.getItem() instanceof ItemBlock && is.stackSize > stack && Arrays.stream(this.serverItems).noneMatch(is.getDisplayName().toLowerCase()::contains)) {
                stack = is.stackSize;
                biggestSlot = i;
            }
        }
        return biggestSlot;
    }
    
    private void swapBlocks() {
        if (!this.swapBlocks.isEnabled() || !this.isReady()) {
            return;
        }
        final int mostBlocksSlot = this.getMostBlocks();
        final int desiredSlot = ItemType.BLOCK.getDesiredSlot();
        if (mostBlocksSlot != -1 && mostBlocksSlot != desiredSlot) {
            final Slot dss = InvManager.mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
            final ItemStack dsis = dss.getStack();
            if (dsis == null || !(dsis.getItem() instanceof ItemBlock) || dsis.stackSize < InvManager.mc.thePlayer.inventoryContainer.getSlot(mostBlocksSlot).getStack().stackSize || !Arrays.stream(this.serverItems).noneMatch(dsis.getDisplayName().toLowerCase()::contains)) {
                this.swap(mostBlocksSlot, desiredSlot - 36);
            }
        }
    }
    
    private boolean isBestPickaxe(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        final Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        final float value = this.getToolScore(stack);
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (is.getItem() instanceof ItemPickaxe && this.getToolScore(is) > value) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestShovel(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemSpade)) {
            return false;
        }
        final float score = this.getToolScore(stack);
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (is.getItem() instanceof ItemSpade && this.getToolScore(is) > score) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestAxe(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemAxe)) {
            return false;
        }
        final float value = this.getToolScore(stack);
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.getToolScore(is) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(is)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestBow(final ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBow)) {
            return false;
        }
        final float value = this.getBowScore(stack);
        for (int i = 9; i < 45; ++i) {
            final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                final ItemStack is = slot.getStack();
                if (this.getBowScore(is) > value && is.getItem() instanceof ItemBow && !this.isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private float getBowScore(final ItemStack stack) {
        float score = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemBow) {
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) * 0.5f;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.1f;
        }
        return score;
    }
    
    private float getToolScore(final ItemStack stack) {
        float score = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            final String name = item.getUnlocalizedName().toLowerCase();
            if (item instanceof ItemPickaxe) {
                score = tool.getStrVsBlock(stack, Blocks.stone) - (name.contains("gold") ? 5 : 0);
            }
            else if (item instanceof ItemSpade) {
                score = tool.getStrVsBlock(stack, Blocks.dirt) - (name.contains("gold") ? 5 : 0);
            }
            else {
                if (!(item instanceof ItemAxe)) {
                    return 1.0f;
                }
                score = tool.getStrVsBlock(stack, Blocks.log) - (name.contains("gold") ? 5 : 0);
            }
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075f;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0f;
        }
        return score;
    }
    
    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final List<PotionEffect> effects = ((ItemPotion)stack.getItem()).getEffects(stack);
            if (effects == null) {
                return true;
            }
            for (final PotionEffect effect : effects) {
                if (this.badPotionIDs.contains(effect.getPotionID())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isBestArmor(final ItemStack stack, final int type) {
        String typeStr = "";
        switch (type) {
            case 1: {
                typeStr = "helmet";
                break;
            }
            case 2: {
                typeStr = "chestplate";
                break;
            }
            case 3: {
                typeStr = "leggings";
                break;
            }
            case 4: {
                typeStr = "boots";
                break;
            }
        }
        if (stack.getUnlocalizedName().contains(typeStr)) {
            final float prot = getProtScore(stack);
            for (int i = 5; i < 45; ++i) {
                final Slot slot = InvManager.mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    final ItemStack is = slot.getStack();
                    if (is.getUnlocalizedName().contains(typeStr) && getProtScore(is) > prot) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void fakeOpen() {
        if (!this.isInvOpen) {
            this.timer.reset();
            if (!this.inventoryOnly.isEnabled() && this.inventoryPackets.isEnabled()) {
                PacketUtils.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            this.isInvOpen = true;
        }
    }
    
    private void fakeClose() {
        if (this.isInvOpen) {
            if (!this.inventoryOnly.isEnabled() && this.inventoryPackets.isEnabled()) {
                PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow(InvManager.mc.thePlayer.inventoryContainer.windowId));
            }
            this.isInvOpen = false;
        }
    }
    
    private void swap(final int slot, final int hSlot) {
        this.fakeOpen();
        InventoryUtils.swap(slot, hSlot);
        this.fakeClose();
        this.timer.reset();
    }
    
    private boolean canContinue() {
        return (this.inventoryOnly.isEnabled() && !(InvManager.mc.currentScreen instanceof GuiInventory)) || (this.onlyWhileNotMoving.isEnabled() && MovementUtils.isMoving());
    }
    
    static {
        slotWeapon = new NumberSetting("Weapon Slot", 1.0, 9.0, 1.0, 1.0);
        slotPick = new NumberSetting("Pickaxe Slot", 2.0, 9.0, 1.0, 1.0);
        slotAxe = new NumberSetting("Axe Slot", 3.0, 9.0, 1.0, 1.0);
        slotShovel = new NumberSetting("Shovel Slot", 4.0, 9.0, 1.0, 1.0);
        slotBow = new NumberSetting("Bow Slot", 5.0, 9.0, 1.0, 1.0);
        slotBlock = new NumberSetting("Block Slot", 6.0, 9.0, 1.0, 1.0);
        slotGapple = new NumberSetting("Gapple Slot", 7.0, 9.0, 1.0, 1.0);
    }
    
    private enum ItemType
    {
        WEAPON(InvManager.slotWeapon), 
        PICKAXE(InvManager.slotPick), 
        AXE(InvManager.slotAxe), 
        SHOVEL(InvManager.slotShovel), 
        BLOCK(InvManager.slotBlock), 
        BOW(InvManager.slotBow), 
        GAPPLE(InvManager.slotGapple);
        
        private final NumberSetting setting;
        
        public int getDesiredSlot() {
            return this.setting.getValue().intValue() + 35;
        }
        
        public Slot getSlot() {
            return Utils.mc.thePlayer.inventoryContainer.getSlot(this.getDesiredSlot());
        }
        
        public ItemStack getStackInSlot() {
            return this.getSlot().getStack();
        }
        
        public NumberSetting getSetting() {
            return this.setting;
        }
        
        private ItemType(final NumberSetting setting) {
            this.setting = setting;
        }
    }
}
