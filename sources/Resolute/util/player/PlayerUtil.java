// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemCloth;
import java.util.Iterator;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.Minecraft;

public class PlayerUtil
{
    public static Minecraft mc;
    public static int MAX_HURT_RESISTANT_TIME;
    public static final int INCLUDE_ARMOR_BEGIN = 5;
    public static final int EXCLUDE_ARMOR_BEGIN = 9;
    public static final int ONLY_HOT_BAR_BEGIN = 36;
    public static final int END = 45;
    
    public static boolean isInventoryEmpty(final IInventory inventory, final boolean archery, final boolean smart) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (isValid(inventory.getStackInSlot(i), archery, smart)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInventoryFull() {
        for (int i = 9; i < 45; ++i) {
            if (!PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isValid(final ItemStack stack, final boolean archery, final boolean check) {
        if (stack == null) {
            return false;
        }
        if (!check) {
            return true;
        }
        if (stack.getItem() instanceof ItemBlock) {
            return isGoodBlockStack(stack);
        }
        if (stack.getItem() instanceof ItemSword) {
            return isBestSword(stack);
        }
        if (stack.getItem() instanceof ItemTool) {
            return isBestTool(stack);
        }
        if (stack.getItem() instanceof ItemArmor) {
            return isBestArmor(stack);
        }
        if (stack.getItem() instanceof ItemPotion) {
            return isBuffPotion(stack);
        }
        if (stack.getItem() instanceof ItemFood) {
            return isGoodFood(stack);
        }
        if (stack.getItem() instanceof ItemBow && archery) {
            return isBestBow(stack);
        }
        return (archery && stack.getItem().getUnlocalizedName().equals("item.arrow")) || stack.getItem() instanceof ItemEnderPearl || isGoodItem(stack);
    }
    
    public static boolean isGoodItem(final ItemStack stack) {
        final Item item = stack.getItem();
        return (!(item instanceof ItemBucket) || ((ItemBucket)item).isFull == Blocks.flowing_water) && !(item instanceof ItemExpBottle) && !(item instanceof ItemFishingRod) && !(item instanceof ItemEgg) && !(item instanceof ItemSnowball) && !(item instanceof ItemSkull) && !(item instanceof ItemBucket);
    }
    
    public static boolean isHoldingSword() {
        return PlayerUtil.mc.thePlayer.getHeldItem() != null && PlayerUtil.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }
    
    public static boolean isBestSword(final ItemStack itemStack) {
        double damage = 0.0;
        ItemStack bestStack = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemSword) {
                final double newDamage = getItemDamage(stack);
                if (newDamage > damage) {
                    damage = newDamage;
                    bestStack = stack;
                }
            }
        }
        return bestStack == itemStack || getItemDamage(itemStack) >= damage;
    }
    
    public static boolean isBestBow(final ItemStack itemStack) {
        double bestBowDmg = -1.0;
        ItemStack bestBow = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBow) {
                final double damage = getBowDamage(stack);
                if (damage > bestBowDmg) {
                    bestBow = stack;
                    bestBowDmg = damage;
                }
            }
        }
        return itemStack == bestBow || getBowDamage(itemStack) > bestBowDmg;
    }
    
    public static double getBowDamage(final ItemStack stack) {
        double damage = 0.0;
        if (stack.getItem() instanceof ItemBow && stack.isItemEnchanted()) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
        }
        return damage;
    }
    
    public static int getToolType(final ItemStack stack) {
        final ItemTool tool = (ItemTool)stack.getItem();
        if (tool instanceof ItemPickaxe) {
            return 0;
        }
        if (tool instanceof ItemAxe) {
            return 1;
        }
        if (tool instanceof ItemSpade) {
            return 2;
        }
        return -1;
    }
    
    public static boolean isBestTool(final ItemStack itemStack) {
        final int type = getToolType(itemStack);
        Tool bestTool = new Tool(-1, -1.0, null);
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemTool && type == getToolType(stack)) {
                final double efficiency = getToolEfficiency(stack);
                if (efficiency > bestTool.getEfficiency()) {
                    bestTool = new Tool(i, efficiency, stack);
                }
            }
        }
        return bestTool.getStack() == itemStack || getToolEfficiency(itemStack) > bestTool.getEfficiency();
    }
    
    public static float getToolEfficiency(final ItemStack itemStack) {
        final ItemTool tool = (ItemTool)itemStack.getItem();
        float efficiency = tool.getToolMaterial().getEfficiencyOnProperMaterial();
        final int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
        if (efficiency > 1.0f && lvl > 0) {
            efficiency += lvl * lvl + 1;
        }
        return efficiency;
    }
    
    public static boolean isBestArmor(final ItemStack itemStack) {
        final ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        double reduction = 0.0;
        ItemStack bestStack = null;
        for (int i = 5; i < 45; ++i) {
            final ItemStack stack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemArmor) {
                final ItemArmor stackArmor = (ItemArmor)stack.getItem();
                if (stackArmor.armorType == itemArmor.armorType) {
                    final double newReduction = getDamageReduction(stack);
                    if (newReduction > reduction) {
                        reduction = newReduction;
                        bestStack = stack;
                    }
                }
            }
        }
        return bestStack == itemStack || getDamageReduction(itemStack) > reduction;
    }
    
    public static double getItemDamage(final ItemStack stack) {
        double damage = 0.0;
        final Multimap<String, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers();
        for (final String attributeName : attributeModifierMap.keySet()) {
            if (attributeName.equals("generic.attackDamage")) {
                final Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get((Object)attributeName).iterator();
                if (attributeModifiers.hasNext()) {
                    damage += attributeModifiers.next().getAmount();
                    break;
                }
                break;
            }
        }
        if (stack.isItemEnchanted()) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
        }
        return damage;
    }
    
    public static double getDamageReduction(final ItemStack stack) {
        double reduction = 0.0;
        if (!(stack.getItem() instanceof ItemSkull) && !(stack.getItem() instanceof ItemCloth) && !(stack.getItem() instanceof ItemBanner)) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            reduction += armor.damageReduceAmount;
            if (stack.isItemEnchanted()) {
                reduction += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25;
            }
        }
        return reduction;
    }
    
    public static boolean isGoodBlockStack(final ItemStack stack) {
        return stack.stackSize >= 1 && isValidBlock(Block.getBlockFromItem(stack.getItem()), true);
    }
    
    public static boolean isValidBlock(final Block block, final boolean toPlace) {
        if (block instanceof BlockContainer) {
            return false;
        }
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        }
        final Material material = block.getMaterial();
        return !material.isReplaceable() && !material.isLiquid();
    }
    
    public static boolean isBuffPotion(final ItemStack stack) {
        final ItemPotion potion = (ItemPotion)stack.getItem();
        final List<PotionEffect> effects = potion.getEffects(stack);
        for (final PotionEffect effect : effects) {
            if (Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean potionHasEffect(final ItemStack itemStack, final int id) {
        if (itemStack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)itemStack.getItem();
            for (final PotionEffect effect : potion.getEffects(itemStack)) {
                if (effect.getPotionID() == id) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static float[] getPredictedRotations(final EntityLivingBase ent) {
        final double x = ent.posX + (ent.posX - ent.lastTickPosX) * Math.random();
        final double z = ent.posZ + (ent.posZ - ent.lastTickPosZ) * Math.random();
        final double y = ent.posY + ent.getEyeHeight() / 2.0f * Math.random();
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - PlayerUtil.mc.thePlayer.posX;
        final double zDiff = z - PlayerUtil.mc.thePlayer.posZ;
        final double yDiff = y - PlayerUtil.mc.thePlayer.posY - 0.8;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(yDiff, dist)));
        return new float[] { yaw, pitch };
    }
    
    public static Vec3 getVectorForRotation(final float yaw, final float pitch) {
        final float f = MathHelper.cos((float)(-yaw * 0.017163292f - 3.141592653589793));
        final float f2 = MathHelper.sin((float)(-yaw * 0.017163292f - 3.141592653589793));
        final float f3 = -MathHelper.cos(-pitch * 0.017163292f);
        final float f4 = MathHelper.sin(-pitch * 0.017163292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }
    
    public static double getArmorStrength(final EntityPlayer entityPlayer) {
        double armorstrength = 0.0;
        for (int index = 3; index >= 0; --index) {
            final ItemStack stack = entityPlayer.inventory.armorInventory[index];
            if (stack != null) {
                armorstrength += getDamageReduction(stack);
            }
        }
        return armorstrength;
    }
    
    public static boolean isGoodFood(final ItemStack stack) {
        final ItemFood food = (ItemFood)stack.getItem();
        return food instanceof ItemAppleGold || (food.getHealAmount(stack) >= 4 && food.getSaturationModifier(stack) >= 0.3f);
    }
    
    public static void windowClick(final int windowId, final int slotId, final int mouseButtonClicked, final ClickType mode) {
        PlayerUtil.mc.playerController.windowClick(windowId, slotId, mouseButtonClicked, mode.ordinal(), PlayerUtil.mc.thePlayer);
    }
    
    public static void windowClick(final int slotId, final int mouseButtonClicked, final ClickType mode) {
        PlayerUtil.mc.playerController.windowClick(PlayerUtil.mc.thePlayer.inventoryContainer.windowId, slotId, mouseButtonClicked, mode.ordinal(), PlayerUtil.mc.thePlayer);
    }
    
    public static void openInventory() {
        PlayerUtil.mc.getNetHandler().getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }
    
    public static void closeInventory() {
        PlayerUtil.mc.getNetHandler().getNetworkManager().sendPacket(new C0DPacketCloseWindow(PlayerUtil.mc.thePlayer.inventoryContainer.windowId));
    }
    
    public static boolean isOnServer(final String ip) {
        return !PlayerUtil.mc.isSingleplayer() && PlayerUtil.mc.getCurrentServerData().serverIP.endsWith(ip);
    }
    
    public static double getEffectiveHealth(final EntityLivingBase entity) {
        return entity.getHealth() * (entity.getMaxHealth() / entity.getTotalArmorValue());
    }
    
    public static boolean isBlockUnder(final double x, final double y, final double z) {
        for (int i = (int)y - 1; i > 0; --i) {
            if (!(new BlockPos(x, i, z).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockUnder() {
        return isBlockUnder(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ);
    }
    
    public static boolean isTeammate(final EntityPlayer entityPlayer) {
        if (entityPlayer != null) {
            final String text = entityPlayer.getDisplayName().getFormattedText();
            final String playerText = PlayerUtil.mc.thePlayer.getDisplayName().getFormattedText();
            return text.length() >= 2 && playerText.length() >= 2 && text.startsWith("§") && playerText.startsWith("§") && text.charAt(1) == playerText.charAt(1);
        }
        return false;
    }
    
    static {
        PlayerUtil.mc = Minecraft.getMinecraft();
        PlayerUtil.MAX_HURT_RESISTANT_TIME = 20;
    }
    
    public enum ClickType
    {
        CLICK, 
        SHIFT_CLICK, 
        SWAP_WITH_HOT_BAR_SLOT, 
        PLACEHOLDER, 
        DROP_ITEM;
    }
    
    private static class Tool
    {
        private final int slot;
        private final double efficiency;
        private final ItemStack stack;
        
        public Tool(final int slot, final double efficiency, final ItemStack stack) {
            this.slot = slot;
            this.efficiency = efficiency;
            this.stack = stack;
        }
        
        public int getSlot() {
            return this.slot;
        }
        
        public double getEfficiency() {
            return this.efficiency;
        }
        
        public ItemStack getStack() {
            return this.stack;
        }
    }
}
