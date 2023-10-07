package best.azura.client.util.other;

import best.azura.client.impl.module.impl.player.Scaffold;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;

import java.util.Arrays;
import java.util.List;

public class ItemUtil {

    public static float getCombatDamage(ItemStack stack) {
        if (stack == null || stack.getItem() == null || stack.stackSize == 0) return 0.0F;
        final Item item = stack.getItem();
        if (item instanceof ItemSword) {
            return ((ItemSword) item).getDamageVsEntity() + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, stack) +
                    ((((ItemSword) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        } else if (item instanceof ItemBow) {
            return 1.0F + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
        } else if (item instanceof ItemPickaxe) {
            return ((ItemPickaxe) item).getToolMaterial().getDamageVsEntity() + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED) +
                    ((((ItemPickaxe) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        } else if (item instanceof ItemAxe) {
            return ((ItemAxe) item).getToolMaterial().getDamageVsEntity() + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, stack) +
                    ((((ItemAxe) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        }
        return 0.0F;
    }

    public static float getItemDamage(ItemStack stack) {
        if (stack == null || stack.getItem() == null || stack.stackSize == 0) return 0.0F;
        final Item item = stack.getItem();
        if (item instanceof ItemSword) {
            return getCombatDamage(stack) +
                    ((((ItemSword) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        } else if (item instanceof ItemBow) {
            if (stack.hasDisplayName()) return 1.0F;
            return getCombatDamage(stack);
        } else if (item instanceof ItemArmor) {
            return ((ItemArmor) item).damageReduceAmount +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.respiration.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.depthStrider.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack);
        } else if (item instanceof ItemPickaxe) {
            return getCombatDamage(stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack) +
                    ((((ItemPickaxe) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        } else if (item instanceof ItemAxe) {
            return getCombatDamage(stack) +
                    0.5F * EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) +
                    ((((ItemAxe) item).getToolMaterialName().equals(Item.ToolMaterial.GOLD.name())) ? 0.5F : 1.0F);
        } else if (item instanceof ItemFood) {
            ItemFood food = (ItemFood) item;
            return food.getSaturationModifier(stack) + stack.stackSize / 64.0F;
        } else if (item instanceof ItemBlock) {
            if (((ItemBlock)item).getBlock() == Blocks.slime_block) return 1.0F;
            if (Scaffold.invalidBlocks.contains(((ItemBlock)item).getBlock()) && !stack.hasDisplayName()) return 0.0F;
            return 1.0F;
        } else if (item instanceof ItemPotion) {
            try {
                List<Integer> invalidEffects = Arrays.asList(Potion.poison.getId(), Potion.moveSlowdown.getId(),
                        Potion.weakness.getId(), Potion.harm.getId());
                if (((ItemPotion) item).getEffects(stack.getMetadata()).stream().anyMatch(p -> p != null && invalidEffects.contains(p.getPotionID())))
                    return 0.0F;
            } catch (Exception e) {
                return 0.0F;
            }
            return 1.0F;
        } else if (item instanceof ItemSkull) {
            if (stack.getTagCompound().getCompoundTag("SkullOwner") != null &&
                    stack.getTagCompound().getCompoundTag("SkullOwner").getBoolean("hypixelPopulated")) {
                NBTBase base = stack.getTagCompound().getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", 10).get(0);
                if (base instanceof NBTTagCompound) {
                    NBTTagCompound compound = (NBTTagCompound) base;
                    if (compound.getString("Value").equals("eyJ0aW1lc3RhbXAiOjE" +
                            "0NTY5OTg2MzgyOTIsInByb2ZpbGVJZCI6Ijc5ZmI4NDEwNWQ2ZDQ2YmY5MGFmOTA1NzE5YjliMzA" +
                            "zIiwicHJvZmlsZU5hbWUiOiJGcm9nXyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6e" +
                            "yJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE3MmQ3ZDIxOTVhN" +
                            "TFmYmU1NDZlNzM0OGE2Yjc1ZGUxMTk3YmNlMjFlYTFkZWM2MjI3ZGYxZjJlZDhiNzMifX19"))
                        return -1.0F;
                }
                if (stack.getDisplayName().contains("Frog")) return 0.0F;
            }
            return 1.0F;
        } else if (stack.hasDisplayName()) {
            return 1.0F;
        } else if (item == Items.arrow) {
            return 1.0F;
        }
        return 0.0F;
    }

}