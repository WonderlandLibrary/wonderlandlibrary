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

public class EntityPlayerImpl
extends EntityLivingBaseImpl
implements IEntityPlayer {
    @Override
    public boolean isUsingItem() {
        return ((EntityPlayer)this.getWrapped()).func_184587_cr();
    }

    @Override
    public int getSleepTimer() {
        return ((EntityPlayer)this.getWrapped()).field_71076_b;
    }

    public boolean getSpectator() {
        return ((EntityPlayer)this.getWrapped()).func_175149_v();
    }

    @Override
    public float getCooledAttackStrength(float f) {
        return ((EntityPlayer)this.getWrapped()).func_184825_o(f);
    }

    @Override
    public String getDisplayNameString() {
        return ((EntityPlayer)this.getWrapped()).getDisplayNameString();
    }

    @Override
    public int getItemInUseCount() {
        return ((EntityPlayer)this.getWrapped()).func_184605_cv();
    }

    public EntityPlayerImpl(EntityPlayer entityPlayer) {
        super((EntityLivingBase)entityPlayer);
    }

    @Override
    public void triggerAchievement(IStatBase iStatBase) {
        IStatBase iStatBase2 = iStatBase;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean bl = false;
        StatBase statBase = ((StatBaseImpl)iStatBase2).getWrapped();
        entityPlayer.func_71029_a(statBase);
    }

    @Override
    public void attackTargetEntityWithCurrentItem(IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean bl = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        entityPlayer.func_71059_n(entity);
    }

    @Override
    public void setSpeedInAir(float f) {
        ((EntityPlayer)this.getWrapped()).field_71102_ce = f;
    }

    @Override
    public IEntity getFishEntity() {
        IEntity iEntity;
        EntityFishHook entityFishHook = ((EntityPlayer)this.getWrapped()).field_71104_cf;
        if (entityFishHook != null) {
            Entity entity = (Entity)entityFishHook;
            boolean bl = false;
            iEntity = new EntityImpl(entity);
        } else {
            iEntity = null;
        }
        return iEntity;
    }

    @Override
    public boolean isBlocking() {
        return ((EntityPlayer)this.getWrapped()).func_184585_cz();
    }

    @Override
    public int getItemInUseDuration() {
        return ((EntityPlayer)this.getWrapped()).func_184612_cw();
    }

    @Override
    public IPlayerCapabilities getCapabilities() {
        PlayerCapabilities playerCapabilities = ((EntityPlayer)this.getWrapped()).field_71075_bZ;
        boolean bl = false;
        return new PlayerCapabilitiesImpl(playerCapabilities);
    }

    @Override
    public boolean isSpectator() {
        return this.getSpectator();
    }

    @Override
    public double getPrevChasingPosY() {
        return ((EntityPlayer)this.getWrapped()).field_71096_bN;
    }

    @Override
    public boolean isPlayerSleeping() {
        return ((EntityPlayer)this.getWrapped()).func_70608_bn();
    }

    @Override
    public IContainer getInventoryContainer() {
        Container container = ((EntityPlayer)this.getWrapped()).field_71069_bz;
        boolean bl = false;
        return new ContainerImpl(container);
    }

    @Override
    public IInventoryPlayer getInventory() {
        InventoryPlayer inventoryPlayer = ((EntityPlayer)this.getWrapped()).field_71071_by;
        boolean bl = false;
        return new InventoryPlayerImpl(inventoryPlayer);
    }

    @Override
    public void jump() {
        ((EntityPlayer)this.getWrapped()).func_70664_aZ();
    }

    @Override
    public void setItemInUseCount(int n) {
        ((EntityPlayer)this.getWrapped()).field_184628_bn = n;
    }

    @Override
    public void setSleepTimer(int n) {
        ((EntityPlayer)this.getWrapped()).field_71076_b = n;
    }

    @Override
    public boolean getSleeping() {
        return ((EntityPlayer)this.getWrapped()).field_71083_bS;
    }

    @Override
    public GameProfile getGameProfile() {
        return ((EntityPlayer)this.getWrapped()).func_146103_bH();
    }

    @Override
    public IContainer getOpenContainer() {
        IContainer iContainer;
        Container container = ((EntityPlayer)this.getWrapped()).field_71070_bA;
        if (container != null) {
            Container container2 = container;
            boolean bl = false;
            iContainer = new ContainerImpl(container2);
        } else {
            iContainer = null;
        }
        return iContainer;
    }

    @Override
    public IItemStack getItemInUse() {
        IItemStack iItemStack;
        ItemStack itemStack = ((EntityPlayer)this.getWrapped()).func_184607_cu();
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public void onCriticalHit(IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean bl = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        entityPlayer.func_71009_b(entity);
    }

    @Override
    public void setSleeping(boolean bl) {
        ((EntityPlayer)this.getWrapped()).field_71083_bS = bl;
    }

    @Override
    public IItemStack getHeldItem() {
        IItemStack iItemStack;
        ItemStack itemStack = ((EntityPlayer)this.getWrapped()).func_184614_ca();
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public void resetCooldown() {
        ((EntityPlayer)this.getWrapped()).func_184821_cY();
    }

    @Override
    public void fall(float f, float f2) {
        ((EntityPlayer)this.getWrapped()).func_180430_e(f, f2);
    }

    @Override
    public float getSpeedInAir() {
        return ((EntityPlayer)this.getWrapped()).field_71102_ce;
    }

    @Override
    public void setCameraYaw(float f) {
        ((EntityPlayer)this.getWrapped()).field_71109_bG = f;
    }

    @Override
    public float getCameraYaw() {
        return ((EntityPlayer)this.getWrapped()).field_71109_bG;
    }

    @Override
    public void stopUsingItem() {
        ((EntityPlayer)this.getWrapped()).func_184597_cx();
    }

    @Override
    public IFoodStats getFoodStats() {
        FoodStats foodStats = ((EntityPlayer)this.getWrapped()).func_71024_bL();
        boolean bl = false;
        return new FoodStatsImpl(foodStats);
    }

    @Override
    public void onEnchantmentCritical(IEntityLivingBase iEntityLivingBase) {
        IEntityLivingBase iEntityLivingBase2 = iEntityLivingBase;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean bl = false;
        EntityLivingBase entityLivingBase = (EntityLivingBase)((EntityLivingBaseImpl)iEntityLivingBase2).getWrapped();
        entityPlayer.func_71047_c((Entity)entityLivingBase);
    }
}

