
package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import info.sigmaclient.sigma.utils.player.ScaffoldUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;

import java.util.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class InvManager extends Module {
    public ModeValue type = new ModeValue("Type", "OpenInv", new String[]{"OpenInv", "SpoofInv"});
    private BooleanValue onlyWhileNotMoving = new BooleanValue("Only while not moving", false),
            inventoryOnly = new BooleanValue("Inventory only", false){
                @Override
                public boolean isHidden() {
                    return true;
                }
            },
            swapBlocks = new BooleanValue("Swap blocks", false),
            dropFood = new BooleanValue("No food", false);

    BooleanValue noCompass = new BooleanValue("No Compass", false);
    private final NumberValue delay = new NumberValue("Delay", 0.1, 1, 0, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return false;
        }
    };

    private final String[] blacklist = {"tnt", "stick", "egg", "string", "cake", "mushroom", "flint", "compass", "dyePowder", "feather", "bucket", "chest", "snow", "fish", "enchant", "exp", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "snowball", "piston"};
    private final String[] serverItems = {"selector", "tracking compass", "(right click)", "tienda ", "perfil", "salir", "shop", "collectibles", "game", "profil", "lobby", "show all", "hub", "friends only", "cofre", "(click", "teleport", "play", "exit", "hide all", "jeux", "gadget", " (activ", "emote", "amis", "bountique", "choisir", "choose ", "recipe book", "click derecho", "todos", "teletransportador", "configuraci", "jugar de nuevo"};
    private final List<Integer> badPotionIDs = new ArrayList<>(Arrays.asList(2, 18, 19, 7));

    private final TimerUtil timer = new TimerUtil();
    private boolean isInvOpen;

    public InvManager() {
        super("InvManager", Category.Item, "auto cleans your inventory");
     registerValue(type);
     registerValue(onlyWhileNotMoving);
     registerValue(swapBlocks);
     registerValue(dropFood);
     registerValue(noCompass);
     registerValue(delay);
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if (e.isPost() || canContinue() || AutoArmor.isWearing) return;
        if (!mc.player.isHandActive() && (mc.currentScreen == null || mc.currentScreen instanceof InventoryScreen)) {
            if (isReady()) {
                Slot slot = ItemType.WEAPON.getSlot();
                if (!slot.getHasStack() || !isBestWeapon(slot.getStack())) {
                    getBestWeapon();
                }
            }
            getBestPickaxe();
            getBestAxe();
            dropItems();
            swapBlocks();
            getBestBow();
            moveArrows();
            moveFood();
        }
    }

    private boolean isReady() {
        return timer.hasTimeElapsed(delay.getValue().floatValue() * 20 * 50);
    }

    public static float getDamageScore(ItemStack stack) {
        if (stack == null || stack.getItem() == Items.AIR) return 0;

        float damage = 0;
        Item item = stack.getItem();

        if (item instanceof SwordItem) {
            damage += ((SwordItem) item).getAttackDamage();
        } else if (item instanceof ToolItem) {
            damage += item.getMaxDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) * 1.25F +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), stack) * 0.1F;
        return damage;
    }

    public static float getProtScore(ItemStack stack) {
        float prot = 0;
        if (stack.getItem() instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) stack.getItem();
            int amount = 0;
            if(armor.getTranslationKey().contains("nether")){
                amount = 6;
            }else
            if(armor.getTranslationKey().contains("diamond")){
                amount = 5;
            }else
            if(armor.getTranslationKey().contains("iron")){
                amount = 4;
            }else
            if(armor.getTranslationKey().contains("chain")){
                amount = 3;
            }else{
                amount = 2;
            }
            prot += amount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack) * 3F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 25.F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(2), stack) / 100F;
        }
        return prot;
    }

    public static void drop(int slot) {
        mc.playerController.windowClickFixed(0, slot, 1, ClickType.THROW, mc.player, false);
    }
    private void dropItems() {
        if (!isReady()) return;
        for (int i = 9; i < 45; i++) {
            if (canContinue()) return;
            Slot slot = mc.player.container.getSlot(i);
            ItemStack is = slot.getStack();
            if (is != ItemStack.EMPTY && isBadItem(is, i, false)) {
                drop(i);
                timer.reset();
                break;
            }
        }
    }

    private boolean isBestWeapon(ItemStack is) {
        if (is == null) return false;
        float damage = getDamageScore(is);
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is2 = slot.getStack();
                if (getDamageScore(is2) > damage && is2.getItem() instanceof SwordItem) {
                    return false;
                }
            }
        }
        return is.getItem() instanceof SwordItem;
    }

    private void getBestWeapon() {
        for (int i = 9; i < 45; i++) {
            ItemStack is = mc.player.container.getSlot(i).getStack();
            if (is != ItemStack.EMPTY && is.getItem() instanceof SwordItem && isBestWeapon(is) && getDamageScore(is) > 0) {
                swap(i, ItemType.WEAPON.getDesiredSlot() - 36);
                break;
            }
        }
    }

    // stealing is true when called from the ChestStealer module because returning true = ignore, but in invmanager returning true = drop
    public boolean isBadItem(ItemStack stack, int slot, boolean stealing) {
        Item item = stack.getItem();
        String stackName = stack.getDisplayName().getString().toLowerCase(), ulName = item.getTranslationKey();
        if(!noCompass.isEnable() && stack.getItem() instanceof CompassItem){
            return false;
        }
        if (Arrays.stream(serverItems).anyMatch(stackName::contains)) return stealing;

        if (item instanceof BlockItem) {
            return !ScaffoldUtils.isOkBlock(((BlockItem) item));
        }

        if (stealing) {
            if (isBestWeapon(stack) || isBestAxe(stack) || isBestPickaxe(stack) || isBestBow(stack) || isBestShovel(stack)) {
                return false;
            }
            if (item instanceof ArmorItem) {
                for (int type = 1; type < 5; type++) {
                    ItemStack is = mc.player.container.getSlot(type + 4).getStack();
                    if (is != ItemStack.EMPTY) {
                        String typeStr = "";
                        switch (type) {
                            case 1:
                                typeStr = "helmet";
                                break;
                            case 2:
                                typeStr = "chestplate";
                                break;
                            case 3:
                                typeStr = "leggings";
                                break;
                            case 4:
                                typeStr = "boots";
                                break;
                        }
                        if (stack.getTranslationKey().contains(typeStr) && getProtScore(is) > getProtScore(stack)) {
                            continue;
                        }
                    }
                    if (isBestArmor(stack, type)) {
                        return false;
                    }
                }
            }
        }

        int weaponSlot = ItemType.WEAPON.getDesiredSlot(), pickaxeSlot = ItemType.PICKAXE.getDesiredSlot(),
                axeSlot = ItemType.AXE.getDesiredSlot();

        if (stealing || (slot != weaponSlot || !isBestWeapon(ItemType.WEAPON.getStackInSlot()))
                && (slot != pickaxeSlot || !isBestPickaxe(ItemType.PICKAXE.getStackInSlot()))
                && (slot != axeSlot || !isBestAxe(ItemType.AXE.getStackInSlot()))) {
            if (!stealing && item instanceof ArmorItem) {
                for (int type = 1; type < 5; type++) {
                    ItemStack is = mc.player.container.getSlot(type + 4).getStack();
                    if (is != ItemStack.EMPTY && isBestArmor(is, type)) {
                        continue;
                    }
                    if (isBestArmor(stack, type)) {
                        return false;
                    }
                }
            }

            if ((item == Items.WHEAT) || item instanceof SpawnEggItem
                    || (item.isFood() && dropFood.isEnable() && !(item.getTranslationKey().contains("golden_apple")))
                    || (item instanceof PotionItem && isBadPotion(stack))) {
                return true;
            } else if (!(item instanceof SwordItem) && !(item instanceof ToolItem) && !(item instanceof HoeItem) && !(item instanceof ArmorItem)) {
//                if ((item instanceof BowItem || item == Items.ARROW)) {
//                    return true;
//                } else {
                    return (item instanceof GlassBottleItem || Arrays.stream(blacklist).anyMatch(ulName::contains));
//                }
            }
            return true;
        }

        return false;
    }

    private void getBestPickaxe() {
        if (!isReady()) return;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestPickaxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = ItemType.PICKAXE.getDesiredSlot();
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.player.container.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestPickaxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    private void getBestAxe() {
        if (!isReady()) return;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestAxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = ItemType.AXE.getDesiredSlot();
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.player.container.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestAxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                        timer.reset();
                    }
                }
            }
        }
    }

    private void getBestBow() {
        if (!isReady()) return;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                String stackName = is.getDisplayName().getString().toLowerCase();
                if (Arrays.stream(serverItems).anyMatch(stackName::contains) || !(is.getItem() instanceof BowItem))
                    continue;
                if (isBestBow(is) && !isBestWeapon(is)) {
                    int desiredSlot = ItemType.BOW.getDesiredSlot();
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.player.container.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestBow(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    public static void click(int slot, int mouseButton, boolean shiftClick) {
        mc.playerController.windowClickFixed(mc.player.container.windowId, slot, mouseButton, shiftClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, mc.player, false);
    }
    private void moveArrows() {
//        dropArchery.isEnable();
        return;
    }

    private void moveFood() {
        if (dropFood.isEnable() || !isReady()) return;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (hasMostGapples(is)) {
                    int desiredSlot = ItemType.GAPPLE.getDesiredSlot();
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.player.container.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !hasMostGapples(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    private boolean hasMostGapples(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item.getTranslationKey().contains("golden_apple"))) {
            return false;
        } else {
            int value = stack.getStackSize();
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem().getTranslationKey().contains("golden_apple") && is.getStackSize() > value) {
                        return false;
                    }
                }
            }
            return true;
        }

    }

    private int getMostBlocks() {
        int stack = 0;
        int biggestSlot = -1;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.container.getSlot(i);
            ItemStack is = slot.getStack();
            if (is != ItemStack.EMPTY && is.getItem() instanceof BlockItem && is.getStackSize() > stack && Arrays.stream(serverItems).noneMatch(is.getDisplayName().getString().toLowerCase()::contains)) {
                stack = is.getStackSize();
                biggestSlot = i;
            }
        }
        return biggestSlot;
    }

    private void swapBlocks() {
        if (!swapBlocks.isEnable() || !isReady()) return;
        int mostBlocksSlot = getMostBlocks();
        int desiredSlot = ItemType.BLOCK.getDesiredSlot();
        if (mostBlocksSlot != -1 && mostBlocksSlot != desiredSlot) {
            // only switch if the hotbar slot doesn't already have blocks of the same quantity to prevent an inf loop
            Slot dss = mc.player.container.getSlot(desiredSlot);
            ItemStack dsis = dss.getStack();
            if (!(dsis != ItemStack.EMPTY && dsis.getItem() instanceof BlockItem && dsis.getStackSize() >= mc.player.container.getSlot(mostBlocksSlot).getStack().getStackSize() && Arrays.stream(serverItems).noneMatch(dsis.getDisplayName().getString().toLowerCase()::contains))) {
                swap(mostBlocksSlot, desiredSlot - 36);
            }
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (!(item instanceof PickaxeItem)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof PickaxeItem && getToolScore(is) > value) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestShovel(ItemStack stack) {
        if (stack == null) return false;
        if (!(stack.getItem() instanceof ShovelItem)) {
            return false;
        } else {
            float score = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof ShovelItem && getToolScore(is) > score) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestAxe(ItemStack stack) {
        if (stack == null) return false;
        if (!(stack.getItem() instanceof AxeItem)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getToolScore(is) > value && is.getItem() instanceof AxeItem && !isBestWeapon(is)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestBow(ItemStack stack) {
        if (!(stack.getItem() instanceof BowItem)) {
            return false;
        } else {
            float value = getBowScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getBowScore(is) > value && is.getItem() instanceof BowItem && !isBestWeapon(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private float getBowScore(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof BowItem) {
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(48), stack);
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(50), stack) * 0.5F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) * 0.1F;
        }
        return score;
    }

    private float getToolScore(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            String name = item.getTranslationKey().toLowerCase();
            if (item instanceof PickaxeItem) {
                score = tool.getDestroySpeed(stack, Blocks.STONE.getDefaultState()) - (name.contains("gold") ? 5 : 0);
            } else if (item instanceof ShovelItem) {
                score = tool.getDestroySpeed(stack, Blocks.DIRT.getDefaultState()) - (name.contains("gold") ? 5 : 0);
            } else {
                if (!(item instanceof AxeItem)) return 1;
                score = tool.getDestroySpeed(stack, Blocks.OAK_LOG.getDefaultState()) - (name.contains("gold") ? 5 : 0);
            }
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(32), stack) * 0.0075F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 100F;
        }
        return score;
    }

    private boolean isBadPotion(ItemStack stack) {
//        if (stack != null && stack.getItem() instanceof PotionItem) {
//            List<EffectInstance> effects = PotionUtils.getEffectsFromStack(stack);
//            for (EffectInstance effect : effects) {
//                if (badPotionIDs.contains(Potion.getPotionTypeForName())) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private boolean isBestArmor(ItemStack stack, int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "helmet";
                break;
            case 2:
                typeStr = "chestplate";
                break;
            case 3:
                typeStr = "leggings";
                break;
            case 4:
                typeStr = "boots";
                break;
        }
        if (stack.getTranslationKey().contains(typeStr)) {
            float prot = getProtScore(stack);
            for (int i = 5; i < 45; i++) {
                Slot slot = mc.player.container.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getTranslationKey().contains(typeStr) && getProtScore(is) > prot) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static void swap2(int slot, int hSlot) {
        mc.playerController.windowClickFixed(mc.player.container.windowId, slot, hSlot, ClickType.SWAP, mc.player, false);
    }
    private void swap(int slot, int hSlot) {
        swap2(slot, hSlot);
        timer.reset();
    }

    private boolean canContinue() {
        return (type.is("OpenInv") && !(mc.currentScreen instanceof InventoryScreen)) || 
                (onlyWhileNotMoving.isEnable() && MovementUtils.isMoving());
    }

    @Getter
    @AllArgsConstructor
    private enum ItemType {
        WEAPON(1),
        AXE(2),
        PICKAXE(3),
        GAPPLE(4),
        BLOCK(5),
        BOW(6);

        private final int setting;

        public int getDesiredSlot() {
            return setting + 35;
        }

        public Slot getSlot() {
            return mc.player.container.getSlot(getDesiredSlot());
        }

        public ItemStack getStackInSlot() {
            return getSlot().getStack();
        }
    }
}
