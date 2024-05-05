/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.ItemStack
 *  net.minecraft.stats.StatBase
 *  net.minecraft.util.FoodStats
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats;
import net.ccbluex.liquidbounce.injection.backend.ContainerImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.FoodStatsImpl;
import net.ccbluex.liquidbounce.injection.backend.InventoryPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.PlayerCapabilitiesImpl;
import net.ccbluex.liquidbounce.injection.backend.StatBaseImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.util.FoodStats;

public class EntityPlayerImpl<T extends EntityPlayer>
extends EntityLivingBaseImpl<T>
implements IEntityPlayer {
    @Override
    public GameProfile getGameProfile() {
        return ((EntityPlayer)this.getWrapped()).func_146103_bH();
    }

    @Override
    public IEntity getFishEntity() {
        IEntity iEntity;
        EntityFishHook entityFishHook = ((EntityPlayer)this.getWrapped()).field_71104_cf;
        if (entityFishHook != null) {
            Entity $this$wrap$iv = (Entity)entityFishHook;
            boolean $i$f$wrap = false;
            iEntity = new EntityImpl<Entity>($this$wrap$iv);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public IFoodStats getFoodStats() {
        FoodStats $this$wrap$iv = ((EntityPlayer)this.getWrapped()).func_71024_bL();
        boolean $i$f$wrap = false;
        return new FoodStatsImpl($this$wrap$iv);
    }

    @Override
    public double getPrevChasingPosY() {
        return ((EntityPlayer)this.getWrapped()).field_71096_bN;
    }

    @Override
    public int getSleepTimer() {
        return ((EntityPlayer)this.getWrapped()).field_71076_b;
    }

    @Override
    public void setSleepTimer(int value) {
        ((EntityPlayer)this.getWrapped()).field_71076_b = value;
    }

    @Override
    public boolean getSleeping() {
        return ((EntityPlayer)this.getWrapped()).field_71083_bS;
    }

    @Override
    public void setSleeping(boolean value) {
        ((EntityPlayer)this.getWrapped()).field_71083_bS = value;
    }

    @Override
    public boolean isPlayerSleeping() {
        return ((EntityPlayer)this.getWrapped()).func_70608_bn();
    }

    @Override
    public float getSpeedInAir() {
        return ((EntityPlayer)this.getWrapped()).field_71102_ce;
    }

    @Override
    public void setSpeedInAir(float value) {
        ((EntityPlayer)this.getWrapped()).field_71102_ce = value;
    }

    @Override
    public float getCameraYaw() {
        return ((EntityPlayer)this.getWrapped()).field_71109_bG;
    }

    @Override
    public void setCameraYaw(float value) {
        ((EntityPlayer)this.getWrapped()).field_71109_bG = value;
    }

    @Override
    public boolean isBlocking() {
        return ((EntityPlayer)this.getWrapped()).func_184585_cz();
    }

    @Override
    public int getItemInUseCount() {
        return ((EntityPlayer)this.getWrapped()).func_184605_cv();
    }

    @Override
    public void setItemInUseCount(int value) {
        ((EntityPlayer)this.getWrapped()).field_184628_bn = value;
    }

    @Override
    public IItemStack getItemInUse() {
        IItemStack iItemStack;
        ItemStack itemStack = ((EntityPlayer)this.getWrapped()).func_184607_cu();
        if (itemStack != null) {
            ItemStack $this$wrap$iv = itemStack;
            boolean $i$f$wrap = false;
            iItemStack = new ItemStackImpl($this$wrap$iv);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public IPlayerCapabilities getCapabilities() {
        PlayerCapabilities $this$wrap$iv = ((EntityPlayer)this.getWrapped()).field_71075_bZ;
        boolean $i$f$wrap = false;
        return new PlayerCapabilitiesImpl($this$wrap$iv);
    }

    @Override
    public IItemStack getHeldItem() {
        IItemStack iItemStack;
        ItemStack itemStack = ((EntityPlayer)this.getWrapped()).func_184614_ca();
        if (itemStack != null) {
            ItemStack $this$wrap$iv = itemStack;
            boolean $i$f$wrap = false;
            iItemStack = new ItemStackImpl($this$wrap$iv);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public boolean isUsingItem() {
        return ((EntityPlayer)this.getWrapped()).func_184587_cr();
    }

    @Override
    public IContainer getInventoryContainer() {
        Container $this$wrap$iv = ((EntityPlayer)this.getWrapped()).field_71069_bz;
        boolean $i$f$wrap = false;
        return new ContainerImpl($this$wrap$iv);
    }

    @Override
    public IInventoryPlayer getInventory() {
        InventoryPlayer $this$wrap$iv = ((EntityPlayer)this.getWrapped()).field_71071_by;
        boolean $i$f$wrap = false;
        return new InventoryPlayerImpl($this$wrap$iv);
    }

    @Override
    public IContainer getOpenContainer() {
        IContainer iContainer;
        Container container = ((EntityPlayer)this.getWrapped()).field_71070_bA;
        if (container != null) {
            Container $this$wrap$iv = container;
            boolean $i$f$wrap = false;
            iContainer = new ContainerImpl($this$wrap$iv);
        } else {
            iContainer = null;
        }
        return iContainer;
    }

    @Override
    public int getItemInUseDuration() {
        return ((EntityPlayer)this.getWrapped()).func_184612_cw();
    }

    @Override
    public String getDisplayNameString() {
        return ((EntityPlayer)this.getWrapped()).getDisplayNameString();
    }

    public boolean getSpectator() {
        return ((EntityPlayer)this.getWrapped()).func_175149_v();
    }

    @Override
    public void stopUsingItem() {
        ((EntityPlayer)this.getWrapped()).func_184597_cx();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onCriticalHit(IEntity entity) {
        void $this$unwrap$iv;
        IEntity iEntity = entity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71009_b(t);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onEnchantmentCritical(IEntityLivingBase entity) {
        void $this$unwrap$iv;
        IEntityLivingBase iEntityLivingBase = entity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        EntityLivingBase entityLivingBase = (EntityLivingBase)((EntityLivingBaseImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71047_c((Entity)entityLivingBase);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void attackTargetEntityWithCurrentItem(IEntity entity) {
        void $this$unwrap$iv;
        IEntity iEntity = entity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71059_n(t);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        ((EntityPlayer)this.getWrapped()).func_180430_e(distance, damageMultiplier);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void triggerAchievement(IStatBase stat) {
        void $this$unwrap$iv;
        IStatBase iStatBase = stat;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        StatBase statBase = ((StatBaseImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71029_a(statBase);
    }

    @Override
    public void jump() {
        ((EntityPlayer)this.getWrapped()).func_70664_aZ();
    }

    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return ((EntityPlayer)this.getWrapped()).func_184825_o(adjustTicks);
    }

    @Override
    public void resetCooldown() {
        ((EntityPlayer)this.getWrapped()).func_184821_cY();
    }

    public EntityPlayerImpl(T wrapped) {
        super((EntityLivingBase)wrapped);
    }
}

