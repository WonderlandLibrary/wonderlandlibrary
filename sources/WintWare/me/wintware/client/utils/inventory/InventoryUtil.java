/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.inventory;

import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    static TimerUtils timer = new TimerUtils();

    public static int getItemCount(Container container, Item item) {
        int itemCount = 0;
        for (int i = 0; i < 45; ++i) {
            ItemStack is;
            if (!container.getSlot(i).getHasStack() || (is = container.getSlot(i).getStack()).getItem() != item) continue;
            itemCount += is.getMaxStackSize();
        }
        return itemCount;
    }

    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = Minecraft.player.inventoryContainer.getSlot(index).getStack();
            if (stack != null) continue;
            return false;
        }
        return true;
    }

    public static void swapInventoryItems(int slot1, int slot2) {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        short short1 = Minecraft.player.inventoryContainer.getNextTransactionID(Minecraft.player.inventory);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        ItemStack itemstack = Minecraft.player.inventoryContainer.slotClick(slot1, 0, ClickType.PICKUP, Minecraft.player);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.player.connection.sendPacket(new CPacketClickWindow(Minecraft.player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, itemstack, short1));
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        itemstack = Minecraft.player.inventoryContainer.slotClick(slot2, 0, ClickType.PICKUP, Minecraft.player);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        itemstack = Minecraft.player.inventoryContainer.slotClick(slot1, 0, ClickType.PICKUP, Minecraft.player);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.player.connection.sendPacket(new CPacketClickWindow(Minecraft.player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, itemstack, short1));
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static int findHotbarPotion() {
        for (int o = 0; o < 9; ++o) {
            ItemStack item = Minecraft.player.inventory.getStackInSlot(o);
            if (item == null || !InventoryUtil.isPotion(item)) continue;
            return o;
        }
        return -1;
    }

    public static int findEmptyPotion() {
        for (int o = 36; o < 45; ++o) {
            ItemStack item = Minecraft.player.inventoryContainer.getSlot(o).getStack();
            if (item == null) {
                return o;
            }
            if (!(item.getItem() instanceof ItemGlassBottle)) continue;
            return o;
        }
        return -1;
    }

    public static boolean isShiftable(ItemStack preferedItem) {
        if (preferedItem == null) {
            return true;
        }
        for (int o = 36; o < 45; ++o) {
            if (Minecraft.player.inventoryContainer.getSlot(o).getHasStack()) {
                ItemStack item = Minecraft.player.inventoryContainer.getSlot(o).getStack();
                if (item == null) {
                    return true;
                }
                if (Item.getIdFromItem(item.getItem()) != Item.getIdFromItem(preferedItem.getItem()) || item.getMaxStackSize() + preferedItem.getMaxStackSize() > preferedItem.getMaxStackSize()) continue;
                return true;
            }
            return true;
        }
        return false;
    }

    public static int getUseablePotion() {
        for (int o = 9; o < 36; ++o) {
            if (!Minecraft.player.inventoryContainer.getSlot(o).getHasStack()) continue;
            ItemStack item = Minecraft.player.inventoryContainer.getSlot(o).getStack();
            if (!InventoryUtil.isPotion(item)) continue;
            return o;
        }
        return -1;
    }

    public static boolean isPotion(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSplashPotion) {
            for (PotionEffect effect : PotionUtils.getEffectsFromStack(itemStack)) {
                Potion potion = effect.getPotion();
                effect.getPotion();
                if (potion != Potion.getPotionById(5)) continue;
                return true;
            }
        }
        return false;
    }

    public static void usePotion() {
        EnumHand hand = EnumHand.MAIN_HAND;
        ItemStack item = Minecraft.player.getHeldItem(EnumHand.MAIN_HAND);
        if (item != null) {
            if (InventoryUtil.mc.playerController.processRightClick(Minecraft.player, InventoryUtil.mc.world, hand) == EnumActionResult.SUCCESS) {
                // empty if block
            }
        }
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!new AxisAlignedBB(pos).intersectsWith(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft();
            Item item = Minecraft.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock) || !((ItemBlock)item).getBlock().equals(block)) continue;
            return i;
        }
        return -1;
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft();
            Item item = Minecraft.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static int getItemInHotbar(Item designatedItem) {
        for (int i = 0; i < 9; ++i) {
            Minecraft.getMinecraft();
            Item item = Minecraft.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof Item) || !item.equals(designatedItem)) continue;
            return i;
        }
        return -1;
    }

    public boolean inventoryIsFull() {
        for (int i = 9; i < 45; ++i) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (stack != null) continue;
            return false;
        }
        return true;
    }

    public static int getArmorItemsEquipSlot(ItemStack stack, boolean equipmentSlot) {
        if (stack.getUnlocalizedName().contains("helmet")) {
            return equipmentSlot ? 4 : 5;
        }
        if (stack.getUnlocalizedName().contains("chestplate")) {
            return equipmentSlot ? 3 : 6;
        }
        if (stack.getUnlocalizedName().contains("leggings")) {
            return equipmentSlot ? 2 : 7;
        }
        if (stack.getUnlocalizedName().contains("boots")) {
            return equipmentSlot ? 1 : 8;
        }
        return -1;
    }

    public static int getPotsInInventory(int potID) {
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            Minecraft.getMinecraft();
            if (!Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            Minecraft.getMinecraft();
            ItemStack is = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(item instanceof ItemPotion)) continue;
            ItemPotion potion = (ItemPotion)item;
            if (PotionUtils.getEffectsFromStack(is) == null) continue;
            for (PotionEffect o : PotionUtils.getEffectsFromStack(is)) {
                PotionEffect effect = o;
                if (effect.getPotion() != Potion.getPotionById(potID) || !ItemPotion.isSplash(is.getItemDamage())) continue;
                ++counter;
            }
        }
        return counter;
    }

    public static boolean isPotion(ItemStack stack, Potion potion, boolean splash) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemPotion)) {
            return false;
        }
        ItemPotion potionItem = (ItemPotion)stack.getItem();
        if (splash && !ItemPotion.isSplash(stack.getItemDamage())) {
            return false;
        }
        if (PotionUtils.getEffectsFromStack(stack) == null) {
            return potion == null;
        }
        if (potion == null) {
            return false;
        }
        return false;
    }

    public static boolean hotbarHasPotion(Potion effect, boolean splash) {
        for (int index = 0; index <= 8; ++index) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(index);
            if (stack == null || !InventoryUtil.isPotion(stack, effect, splash)) continue;
            return true;
        }
        return false;
    }

    public static void useFirstPotionSilent(Potion effect, boolean splash) {
        for (int index = 0; index <= 8; ++index) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(index);
            if (stack == null || !InventoryUtil.isPotion(stack, effect, splash)) continue;
            int oldItem = Minecraft.player.inventory.currentItem;
            mc.getConnection().sendPacket(new CPacketHeldItemChange(index));
            mc.getConnection().sendPacket(new CPacketCreativeInventoryAction(oldItem, Minecraft.player.inventory.getCurrentItem()));
            mc.getConnection().sendPacket(new CPacketHeldItemChange(oldItem));
            break;
        }
    }

    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i) == null) continue;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) continue;
            return true;
        }
        return false;
    }

    public static boolean inventoryHasPotion(Potion effect, boolean splash) {
        for (int index = 0; index <= 36; ++index) {
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(index);
            if (stack == null || !InventoryUtil.isPotion(stack, effect, splash)) continue;
            return true;
        }
        return false;
    }

    public static int getSwordSlot() {
        int bestSword = -1;
        float bestDamage = 1.0f;
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack item = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (item == null || !(item.getItem() instanceof ItemSword)) continue;
            ItemSword is = (ItemSword)item.getItem();
            float damage = is.getDamageVsEntity();
            if (!((damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(16), item) * 1.26f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), item) * 0.01f) > bestDamage)) continue;
            bestDamage = damage;
            bestSword = i;
        }
        return bestSword;
    }

    public static int getBestSword() {
        int bestSword = -1;
        float bestDamage = 1.0f;
        int k = 0;
        while (true) {
            if (k >= Minecraft.player.inventory.mainInventory.size()) break;
            ItemStack is = Minecraft.player.inventoryContainer.getSlot(k).getStack();
            if (is != null && is.getItem() instanceof ItemSword) {
                ItemSword itemSword = (ItemSword)is.getItem();
                float damage = itemSword.getMaxDamage();
                if ((damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(20), is)) > bestDamage) {
                    bestDamage = damage;
                    bestSword = k;
                }
            }
            ++k;
        }
        return bestSword;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            for (PotionEffect o : PotionUtils.getEffectsFromStack(stack)) {
                PotionEffect effect = o;
                if (effect.getPotion() != Potion.getPotionById(19) && effect.getPotion() != Potion.getPotionById(7) && effect.getPotion() != Potion.getPotionById(2) && effect.getPotion() != Potion.getPotionById(18)) continue;
                return true;
            }
        }
        return false;
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(p_147046_0_, p_147046_1_, 40.0f);
        GlStateManager.scale(-p_147046_2_, p_147046_2_, p_147046_2_);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float var6 = p_147046_5_.renderYawOffset;
        float var7 = p_147046_5_.rotationYaw;
        float var8 = p_147046_5_.rotationPitch;
        float var9 = p_147046_5_.prevRotationYawHead;
        float var10 = p_147046_5_.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float)Math.atan(p_147046_4_ / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        p_147046_5_.renderYawOffset = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationYaw = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationPitch = -((float)Math.atan(p_147046_4_ / 40.0f)) * 15.0f;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.doRenderEntity(p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f, true);
        var11.setRenderShadow(true);
        p_147046_5_.renderYawOffset = var6;
        p_147046_5_.rotationYaw = var7;
        p_147046_5_.rotationPitch = var8;
        p_147046_5_.prevRotationYawHead = var9;
        p_147046_5_.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static boolean doesNextSlotHavePot() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i) == null) continue;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemPotion)) continue;
            return true;
        }
        return false;
    }

    public static boolean doesNextSlotHaveSoup() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i) == null) continue;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemSoup)) continue;
            return true;
        }
        return false;
    }

    public static int getSlotWithPot() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i) == null) continue;
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != Items.SPLASH_POTION) continue;
            return i;
        }
        return 0;
    }

    public static int getSlotWithSoup() {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i) == null) continue;
            if (!(Minecraft.player.inventory.getStackInSlot(i).getItem() instanceof ItemSoup)) continue;
            return i;
        }
        return 0;
    }

    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemSword)) continue;
            return i;
        }
        return 1;
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.player.inventory.getStackInSlot(i).getItem() != Items.EXPERIENCE_BOTTLE) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int getArmorDurability() {
        int TotalDurability = 0;
        for (ItemStack itemStack : Minecraft.player.inventory.armorInventory) {
            TotalDurability += itemStack.getItemDamage();
        }
        return TotalDurability;
    }

    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!container.getSlot(i).getHasStack() || (is = container.getSlot(i).getStack()).getItem() != item) continue;
            slot = i;
        }
        return slot;
    }

    public static int getItemSlotInHotbar(Item item) {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack is = Minecraft.player.inventory.getStackInSlot(i);
            if (is.getItem() != item) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static void swap(int slot, int hotbarNum) {
        InventoryUtil.mc.playerController.windowClick(Minecraft.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, Minecraft.player);
        InventoryUtil.mc.playerController.windowClick(Minecraft.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, Minecraft.player);
        InventoryUtil.mc.playerController.windowClick(Minecraft.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, Minecraft.player);
        InventoryUtil.mc.playerController.updateController();
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = InventoryUtil.getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (!Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (!(InventoryUtil.getProtection(is) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(3), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(1), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(7), stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(4), stack) / 100.0);
        }
        return prot;
    }

    public static double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0 : (double)((ItemArmor)stack.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), stack) * 4) * 0.0075;
    }
}

