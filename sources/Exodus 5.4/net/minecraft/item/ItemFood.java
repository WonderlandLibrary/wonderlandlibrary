/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFood
extends Item {
    private final int healAmount;
    private boolean alwaysEdible;
    private int potionId;
    private final float saturationModifier;
    private int potionAmplifier;
    private float potionEffectProbability;
    private int potionDuration;
    private final boolean isWolfsFavoriteMeat;
    public final int itemUseDuration;

    public ItemFood(int n, float f, boolean bl) {
        this.itemUseDuration = 32;
        this.healAmount = n;
        this.isWolfsFavoriteMeat = bl;
        this.saturationModifier = f;
        this.setCreativeTab(CreativeTabs.tabFood);
    }

    public float getSaturationModifier(ItemStack itemStack) {
        return this.saturationModifier;
    }

    public ItemFood setAlwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        --itemStack.stackSize;
        entityPlayer.getFoodStats().addStats(this, itemStack);
        world.playSoundAtEntity(entityPlayer, "random.burp", 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);
        this.onFoodEaten(itemStack, world, entityPlayer);
        entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStack;
    }

    public ItemFood(int n, boolean bl) {
        this(n, 0.6f, bl);
    }

    public boolean isWolfsFavoriteMeat() {
        return this.isWolfsFavoriteMeat;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.EAT;
    }

    public int getHealAmount(ItemStack itemStack) {
        return this.healAmount;
    }

    public ItemFood setPotionEffect(int n, int n2, int n3, float f) {
        this.potionId = n;
        this.potionDuration = n2;
        this.potionAmplifier = n3;
        this.potionEffectProbability = f;
        return this;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (entityPlayer.canEat(this.alwaysEdible)) {
            entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }
        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (!world.isRemote && this.potionId > 0 && world.rand.nextFloat() < this.potionEffectProbability) {
            entityPlayer.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }
}

