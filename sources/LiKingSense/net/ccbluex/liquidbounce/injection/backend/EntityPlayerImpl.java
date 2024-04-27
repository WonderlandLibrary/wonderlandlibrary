/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
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
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010O\u001a\u00020P2\u0006\u0010Q\u001a\u00020\u0017H\u0016J\u0018\u0010R\u001a\u00020P2\u0006\u0010S\u001a\u00020\b2\u0006\u0010T\u001a\u00020\bH\u0016J\u0010\u0010U\u001a\u00020\b2\u0006\u0010V\u001a\u00020\bH\u0016J\b\u0010W\u001a\u00020PH\u0016J\u0010\u0010X\u001a\u00020P2\u0006\u0010Q\u001a\u00020\u0017H\u0016J\u0010\u0010Y\u001a\u00020P2\u0006\u0010Q\u001a\u00020ZH\u0016J\b\u0010[\u001a\u00020PH\u0016J\b\u0010\\\u001a\u00020PH\u0016J\u0010\u0010]\u001a\u00020P2\u0006\u0010^\u001a\u00020_H\u0016R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0016\u001a\u0004\u0018\u00010\u00178VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0016\u0010\"\u001a\u0004\u0018\u00010#8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b$\u0010%R\u0014\u0010&\u001a\u00020'8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b(\u0010)R\u0014\u0010*\u001a\u00020+8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010-R\u0014\u0010.\u001a\u00020/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u00100R\u0014\u00101\u001a\u00020/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b1\u00100R\u0014\u00102\u001a\u00020/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u00100R\u0016\u00103\u001a\u0004\u0018\u00010#8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b4\u0010%R$\u00106\u001a\u0002052\u0006\u0010\u0007\u001a\u0002058V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R\u0014\u0010;\u001a\u0002058VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b<\u00108R\u0016\u0010=\u001a\u0004\u0018\u00010+8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b>\u0010-R\u0014\u0010?\u001a\u00020@8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bA\u0010BR$\u0010C\u001a\u0002052\u0006\u0010\u0007\u001a\u0002058V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bD\u00108\"\u0004\bE\u0010:R$\u0010F\u001a\u00020/2\u0006\u0010\u0007\u001a\u00020/8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bG\u00100\"\u0004\bH\u0010IR\u0014\u0010J\u001a\u00020/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\bK\u00100R$\u0010L\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\bM\u0010\u000b\"\u0004\bN\u0010\r\u00a8\u0006`"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EntityPlayerImpl;", "T", "Lnet/minecraft/entity/player/EntityPlayer;", "Lnet/ccbluex/liquidbounce/injection/backend/EntityLivingBaseImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "wrapped", "(Lnet/minecraft/entity/player/EntityPlayer;)V", "value", "", "cameraYaw", "getCameraYaw", "()F", "setCameraYaw", "(F)V", "capabilities", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "getCapabilities", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "displayNameString", "", "getDisplayNameString", "()Ljava/lang/String;", "fishEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getFishEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "foodStats", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "getFoodStats", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "gameProfile", "Lcom/mojang/authlib/GameProfile;", "getGameProfile", "()Lcom/mojang/authlib/GameProfile;", "heldItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getHeldItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "inventory", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "getInventory", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "inventoryContainer", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventoryContainer", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "isBlocking", "", "()Z", "isPlayerSleeping", "isUsingItem", "itemInUse", "getItemInUse", "", "itemInUseCount", "getItemInUseCount", "()I", "setItemInUseCount", "(I)V", "itemInUseDuration", "getItemInUseDuration", "openContainer", "getOpenContainer", "prevChasingPosY", "", "getPrevChasingPosY", "()D", "sleepTimer", "getSleepTimer", "setSleepTimer", "sleeping", "getSleeping", "setSleeping", "(Z)V", "spectator", "getSpectator", "speedInAir", "getSpeedInAir", "setSpeedInAir", "attackTargetEntityWithCurrentItem", "", "entity", "fall", "distance", "damageMultiplier", "getCooledAttackStrength", "adjustTicks", "jump", "onCriticalHit", "onEnchantmentCritical", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "resetCooldown", "stopUsingItem", "triggerAchievement", "stat", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "LiKingSense"})
public class EntityPlayerImpl<T extends EntityPlayer>
extends EntityLivingBaseImpl<T>
implements IEntityPlayer {
    @Override
    @NotNull
    public GameProfile getGameProfile() {
        GameProfile gameProfile = ((EntityPlayer)this.getWrapped()).func_146103_bH();
        Intrinsics.checkExpressionValueIsNotNull((Object)gameProfile, (String)"wrapped.gameProfile");
        return gameProfile;
    }

    @Override
    @Nullable
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
    @NotNull
    public IFoodStats getFoodStats() {
        FoodStats foodStats = ((EntityPlayer)this.getWrapped()).func_71024_bL();
        Intrinsics.checkExpressionValueIsNotNull((Object)foodStats, (String)"wrapped.foodStats");
        FoodStats $this$wrap$iv = foodStats;
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
    @Nullable
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
    @NotNull
    public IPlayerCapabilities getCapabilities() {
        PlayerCapabilities playerCapabilities = ((EntityPlayer)this.getWrapped()).field_71075_bZ;
        Intrinsics.checkExpressionValueIsNotNull((Object)playerCapabilities, (String)"wrapped.capabilities");
        PlayerCapabilities $this$wrap$iv = playerCapabilities;
        boolean $i$f$wrap = false;
        return new PlayerCapabilitiesImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @NotNull
    public IContainer getInventoryContainer() {
        Container container = ((EntityPlayer)this.getWrapped()).field_71069_bz;
        Intrinsics.checkExpressionValueIsNotNull((Object)container, (String)"wrapped.inventoryContainer");
        Container $this$wrap$iv = container;
        boolean $i$f$wrap = false;
        return new ContainerImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IInventoryPlayer getInventory() {
        InventoryPlayer inventoryPlayer = ((EntityPlayer)this.getWrapped()).field_71071_by;
        Intrinsics.checkExpressionValueIsNotNull((Object)inventoryPlayer, (String)"wrapped.inventory");
        InventoryPlayer $this$wrap$iv = inventoryPlayer;
        boolean $i$f$wrap = false;
        return new InventoryPlayerImpl($this$wrap$iv);
    }

    @Override
    @Nullable
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
    @NotNull
    public String getDisplayNameString() {
        String string = ((EntityPlayer)this.getWrapped()).getDisplayNameString();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.displayNameString");
        return string;
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
    public void onCriticalHit(@NotNull IEntity entity) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        IEntity iEntity = entity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71009_b(t2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onEnchantmentCritical(@NotNull IEntityLivingBase entity) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
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
    public void attackTargetEntityWithCurrentItem(@NotNull IEntity entity) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        IEntity iEntity = entity;
        EntityPlayer entityPlayer = (EntityPlayer)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        entityPlayer.func_71059_n(t2);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        ((EntityPlayer)this.getWrapped()).func_180430_e(distance, damageMultiplier);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void triggerAchievement(@NotNull IStatBase stat) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)stat, (String)"stat");
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

    public EntityPlayerImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((EntityLivingBase)wrapped);
    }
}

