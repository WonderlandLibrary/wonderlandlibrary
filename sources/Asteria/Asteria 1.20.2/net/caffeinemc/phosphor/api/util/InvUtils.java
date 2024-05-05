package net.caffeinemc.phosphor.api.util;

import net.caffeinemc.phosphor.mixin.ClientPlayerInteractionManagerAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class InvUtils {
    public static void setInvSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();
    }

    public static int getItemSlot(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem()))
                continue;
            return i;
        }

        return -1;
    }

    public static int getItemSlot(Item item) {
        return getItemSlot((Item i) -> i == item);
    }

    public static boolean selectItemFromHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem()))
                continue;

            setInvSlot(i);
            return true;
        }
        return false;
    }

    public static boolean selectItemFromHotbar(Item item) {
        return selectItemFromHotbar((Item i) -> i == item);
    }

    public static boolean hasItemInHotbar(Item item) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            if (inventory.getStack(i).isOf(item)) return true;
        }
        return false;
    }

    public static boolean hasItemInInventory(Item item) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 9; i < 36; ++i) {
            if (inventory.getStack(i).isOf(item)) return true;
        }
        return false;
    }

    public static boolean hasItemInInventory(Predicate<ItemStack> itemPredicate) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 9; i < 36; ++i) {
            if (itemPredicate.test(inventory.getStack(i))) return true;
        }
        return false;
    }

    public static boolean hasItemInHotbar(Predicate<ItemStack> itemPredicate) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            if (itemPredicate.test(inventory.getStack(i))) return true;
        }
        return false;
    }

    public static boolean hasItemInMain(Predicate<ItemStack> itemPredicate) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 0; i < inventory.main.size(); ++i) {
            if (itemPredicate.test(inventory.getStack(i))) return true;
        }
        return false;
    }

    /**
     * Returns integer of a slot with splash potion of your specific potion effect
     *
     * @param  rawId  		You can get id of your specific effect from <a href="https://minecraft.fandom.com/wiki/Effect">MC Wiki</a>
     * @param  duration		Duration of potion effect
     * @param  amplifier	Multiplier of potion effect
     *
     * @return integer of slot with splash potion of your specific potion effect
     *
     * @author pycat
     */
    public static int findSplash(int rawId, int duration, int amplifier) {
        PlayerInventory inv = mc.player.getInventory();
        StatusEffectInstance potion = new StatusEffectInstance(Registries.STATUS_EFFECT.get(rawId), duration, amplifier);

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inv.getStack(i);

            if (!(itemStack.getItem() instanceof SplashPotionItem))
                continue;

            String s = PotionUtil.getPotion(itemStack).getEffects().toString();
            if (s.contains(potion.toString())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns true if itemStack has the specific splash potion
     *
     * @param  rawId            You can get id of your specific effect from <a href="https://minecraft.fandom.com/wiki/Effect">MC Wiki</a>
     * @param  duration         Duration of potion effect
     * @param  amplifier        Multiplier of potion effect
     * @param  itemStack        ItemStack to check
     *
     * @return boolean
     *
     * @author pycat
     */
    public static boolean isThatSplash(int rawId, int duration, int amplifier, ItemStack itemStack) {
        StatusEffectInstance potion = new StatusEffectInstance(Registries.STATUS_EFFECT.get(rawId), duration, amplifier);

        return itemStack.getItem() instanceof SplashPotionItem &&
                PotionUtil.getPotion(itemStack).getEffects().toString().contains(potion.toString());
    }

    public static int scrollToSlot(int toSlot) {
        int currentSlot = mc.player.getInventory().selectedSlot;

        if (currentSlot < toSlot)
            return currentSlot + 1;
        else
            return currentSlot - 1;
    }

    public static Map<ArmorItem.Type, Integer> slotNumByType = new HashMap<>();
    static {
        slotNumByType.put(ArmorItem.Type.HELMET, 3);
        slotNumByType.put(ArmorItem.Type.CHESTPLATE, 2);
        slotNumByType.put(ArmorItem.Type.LEGGINGS, 1);
        slotNumByType.put(ArmorItem.Type.BOOTS, 0);
    }

    public static boolean isArmorBetter(ItemStack armorStack) {
        ItemStack currentArmor = mc.player.getInventory().getArmorStack(slotNumByType.get(((ArmorItem) armorStack.getItem()).getType()));

        if (currentArmor == null || !(currentArmor.getItem() instanceof ArmorItem)) return true;

        return isArmorBetter(currentArmor, armorStack);
    }

    public static boolean isArmorBetter(ItemStack fromArmorStack, ItemStack thenArmorStack) {
        ArmorItem fromArmorItem = (ArmorItem) fromArmorStack.getItem();
        float fromPoints = fromArmorItem.getProtection() + fromArmorItem.getToughness();

        ArmorItem thenArmorItem = (ArmorItem) thenArmorStack.getItem();
        float thenPoints = thenArmorItem.getProtection() + thenArmorItem.getToughness();

        if (fromPoints < thenPoints) {
            return true;
        }

        NbtList fromArmorEnchantments = fromArmorStack.getEnchantments();
        if (fromArmorEnchantments == null) {
            return thenArmorStack.getEnchantments() != null;
        }

        NbtList thenArmorEnchantments = thenArmorStack.getEnchantments();
        if (thenArmorEnchantments == null) {
            return false;
        }

        fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.PROTECTION);
        fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.UNBREAKING);
        fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.MENDING) * 2;

        thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.PROTECTION);
        thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.UNBREAKING);
        thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.MENDING) * 2;

        if (fromPoints == thenPoints) {
            switch (fromArmorItem.getType()) {
                case HELMET -> {
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.AQUA_AFFINITY);
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.RESPIRATION);

                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.AQUA_AFFINITY);
                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.RESPIRATION);
                }
                case BOOTS -> {
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.DEPTH_STRIDER);
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.SWIFT_SNEAK);
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.SOUL_SPEED);
                    fromPoints += getEnchantmentLevel(fromArmorStack, Enchantments.FEATHER_FALLING);

                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.DEPTH_STRIDER);
                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.SWIFT_SNEAK);
                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.SOUL_SPEED);
                    thenPoints += getEnchantmentLevel(thenArmorStack, Enchantments.FEATHER_FALLING);
                }
            }
        }

        return fromPoints < thenPoints;
    }

    public static int getEnchantmentLevel(ItemStack armorStack, Enchantment enchantment) {
        if (EnchantmentHelper.get(armorStack).containsKey(enchantment)) {
            return EnchantmentHelper.getLevel(enchantment, armorStack);
        }

        return 0;
    }

    public static boolean hasEmptyArmor() {
        AtomicBoolean hasEmptyArmor = new AtomicBoolean(false);

        for (ItemStack armorStack : mc.player.getArmorItems()) {
            if (armorStack == null || !(armorStack.getItem() instanceof ArmorItem)) {
                hasEmptyArmor.set(true);
                break;
            }
        }

        return hasEmptyArmor.get();
    }

    public static boolean isArmorSlotEmpty(ArmorItem armorItem) {
        ItemStack armorStack = mc.player.getInventory().getArmorStack(slotNumByType.get(armorItem.getType()));
        return armorStack == null || !(armorStack.getItem() instanceof ArmorItem);
    }

    private static final Action ACTION = new Action();

    public static Action move() {
        ACTION.type = SlotActionType.PICKUP;
        ACTION.two = true;
        return ACTION;
    }

    public static Action click() {
        ACTION.type = SlotActionType.PICKUP;
        return ACTION;
    }

    /**
     * When writing code with quickSwap, both to and from should provide the ID of a slot, not the index.
     * From should be the slot in the hotbar, to should be the slot you're switching an item from.
     */

    public static Action quickSwap() {
        ACTION.type = SlotActionType.SWAP;
        return ACTION;
    }

    public static Action shiftClick() {
        ACTION.type = SlotActionType.QUICK_MOVE;
        return ACTION;
    }

    public static Action drop() {
        ACTION.type = SlotActionType.THROW;
        ACTION.data = 1;
        return ACTION;
    }

    public static void dropHand() {
        if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, ScreenHandler.EMPTY_SPACE_SLOT_INDEX, 0, SlotActionType.PICKUP, mc.player);
    }

    public static class Action {
        private SlotActionType type = null;
        private boolean two = false;
        private int from = -1;
        private int to = -1;
        private int data = 0;

        private boolean isRecursive = false;

        private Action() {}

        // From

        public Action fromId(int id) {
            from = id;
            return this;
        }

        public Action from(int index) {
            return fromId(SlotUtils.indexToId(index));
        }

        public Action fromHotbar(int i) {
            return from(SlotUtils.HOTBAR_START + i);
        }

        public Action fromOffhand() {
            return from(SlotUtils.OFFHAND);
        }

        public Action fromMain(int i) {
            return from(SlotUtils.MAIN_START + i);
        }

        public Action fromArmor(int i) {
            return from(SlotUtils.ARMOR_START + (3 - i));
        }

        // To

        public void toId(int id) {
            to = id;
            run();
        }

        public void to(int index) {
            toId(SlotUtils.indexToId(index));
        }

        public void toHotbar(int i) {
            to(SlotUtils.HOTBAR_START + i);
        }

        public void toOffhand() {
            to(SlotUtils.OFFHAND);
        }

        public void toMain(int i) {
            to(SlotUtils.MAIN_START + i);
        }

        public void toArmor(int i) {
            to(SlotUtils.ARMOR_START + (3 - i));
        }

        // Slot

        public void slotId(int id) {
            from = to = id;
            run();
        }

        public void slot(int index) {
            slotId(SlotUtils.indexToId(index));
        }

        public void slotHotbar(int i) {
            slot(SlotUtils.HOTBAR_START + i);
        }

        public void slotOffhand() {
            slot(SlotUtils.OFFHAND);
        }

        public void slotMain(int i) {
            slot(SlotUtils.MAIN_START + i);
        }

        public void slotArmor(int i) {
            slot(SlotUtils.ARMOR_START + (3 - i));
        }

        // Other

        private void run() {
            boolean hadEmptyCursor = mc.player.currentScreenHandler.getCursorStack().isEmpty();

            if (type == SlotActionType.SWAP) {
                data = from;
                from = to;
            }

            if (type != null && from != -1 && to != -1) {
                click(from);
                if (two) click(to);
            }

            SlotActionType preType = type;
            boolean preTwo = two;
            int preFrom = from;
            int preTo = to;

            type = null;
            two = false;
            from = -1;
            to = -1;
            data = 0;

            if (!isRecursive && hadEmptyCursor && preType == SlotActionType.PICKUP && preTwo && (preFrom != -1 && preTo != -1) && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                isRecursive = true;
                InvUtils.click().slotId(preFrom);
                isRecursive = false;
            }
        }

        private void click(int id) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, id, data, type, mc.player);
        }
    }
}
