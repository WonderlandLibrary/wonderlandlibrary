package summer.base.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class ItemUtil
{
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static int bestSwordSlot() {
        final float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtil.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = ItemUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    final float swordD = getItemDamage(is);
                    if (swordD > 0.0f) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }
    
    public static ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (ItemUtil.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = ItemUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    final float swordD = getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = is;
                    }
                }
            }
        }
        return best;
    }
    
    public static ItemStack compareDamage(final ItemStack item1, final ItemStack item2) {
        try {
            if (item1 == null || item2 == null) {
                return null;
            }
            if (!(item1.getItem() instanceof ItemSword) && item2.getItem() instanceof ItemSword) {
                return null;
            }
            if (getItemDamage(item1) > getItemDamage(item2)) {
                return item1;
            }
            if (getItemDamage(item2) > getItemDamage(item1)) {
                return item2;
            }
            return item1;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return item1;
        }
    }
    
    public static boolean isBad(final ItemStack item) {
        return item.getItem().getUnlocalizedName().contains("tnt") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().equalsIgnoreCase("egg") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || (item.getItem().getUnlocalizedName().equalsIgnoreCase("chest") && !item.getDisplayName().toLowerCase().contains("collect")) || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("arrow") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("skull") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem().getUnlocalizedName().contains("boat") || item.getItem().getUnlocalizedName().contains("fishing") || item.getItem().getUnlocalizedName().contains("wheat") || item.getItem().getUnlocalizedName().contains("flower") || item.getItem().getUnlocalizedName().contains("record") || item.getItem().getUnlocalizedName().contains("note") || item.getItem().getUnlocalizedName().contains("sugar") || item.getItem().getUnlocalizedName().contains("wire") || item.getItem().getUnlocalizedName().contains("trip") || item.getItem().getUnlocalizedName().contains("slime") || item.getItem().getUnlocalizedName().contains("web") || item.getItem() instanceof ItemGlassBottle || (item.getItem() instanceof ItemArmor && !getBest().contains(item)) || (item.getItem() instanceof ItemSword && item != bestSword()) || item.getItem().getUnlocalizedName().contains("piston") || (item.getItem().getUnlocalizedName().contains("potion") && isBadPotion(item));
    }
    
    public static List<ItemStack> getBest() {
        final List<ItemStack> best = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            ItemStack[] armorInventory;
            for (int length = (armorInventory = ItemUtil.mc.thePlayer.inventory.armorInventory).length, j = 0; j < length; ++j) {
                final ItemStack itemStack = armorInventory[j];
                if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                    final ItemArmor stackArmor = (ItemArmor)itemStack.getItem();
                    if (stackArmor.armorType == i) {
                        armorStack = itemStack;
                    }
                }
            }
            final double reduction = (armorStack == null) ? -1.0 : getArmorStrength(armorStack);
            ItemStack slotStack = findBestArmor(i);
            if (slotStack != null && getArmorStrength(slotStack) <= reduction) {
                slotStack = armorStack;
            }
            if (slotStack != null) {
                best.add(slotStack);
            }
        }
        return best;
    }
    
    public static ItemStack findBestArmor(final int itemSlot) {
        ItemStack i = null;
        double maxReduction = 0.0;
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack itemStack = ItemUtil.mc.thePlayer.inventory.mainInventory[slot];
            if (itemStack != null) {
                final double reduction = getArmorStrength(itemStack);
                if (reduction != -1.0) {
                    final ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
                    if (itemArmor.armorType == itemSlot && reduction >= maxReduction) {
                        maxReduction = reduction;
                        i = itemStack;
                    }
                }
            }
        }
        return i;
    }
    
    public static double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = (float)((ItemArmor)itemStack.getItem()).damageReduceAmount;
        final Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            final int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }
    
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static float getItemDamage(final ItemStack itemStack) {
        if (itemStack == null) {
            return 0.0f;
        }
        if (!(itemStack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
