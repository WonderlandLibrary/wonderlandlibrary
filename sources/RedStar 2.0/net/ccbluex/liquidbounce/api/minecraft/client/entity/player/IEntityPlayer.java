package net.ccbluex.liquidbounce.api.minecraft.client.entity.player;

import com.mojang.authlib.GameProfile;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.SupportsMinecraftVersions;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000x\n\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\b\n\b\t\n\n\b\n\n\b\r\n\n\u0000\bf\u000020JI0J2K0H&JL0J2M02N0H&JO02P0H'J\bQ0JH&JR0J2K0H&JS0J2K0H&J\bT0JH&J\bU0JH&JV0J2W0XH&R0X¦¢\f\b\"\bR\b0\tX¦¢\b\nR\f0\rX¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR 0!X¦¢\b\"#R$0%X¦¢\b&'R(0)8gX¦¢\b(*R+0)X¦¢\b+*R,0)X¦¢\b,*R-0X¦¢\b.R/00X¦¢\f\b12\"\b34R500X¦¢\b62R70%X¦¢\b8'R90:X¦¢\b;<R=00X¦¢\f\b>2\"\b?4R@0)X¦¢\f\bA*\"\bBCRD0)8gX¦¢\bE*RF0X¦¢\f\bG\"\bH¨Y"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "cameraYaw", "", "getCameraYaw", "()F", "setCameraYaw", "(F)V", "capabilities", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "getCapabilities", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "displayNameString", "", "getDisplayNameString", "()Ljava/lang/String;", "fishEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getFishEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "foodStats", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "getFoodStats", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "gameProfile", "Lcom/mojang/authlib/GameProfile;", "getGameProfile", "()Lcom/mojang/authlib/GameProfile;", "heldItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getHeldItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "inventory", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "getInventory", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "inventoryContainer", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventoryContainer", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "isBlocking", "", "()Z", "isPlayerSleeping", "isUsingItem", "itemInUse", "getItemInUse", "itemInUseCount", "", "getItemInUseCount", "()I", "setItemInUseCount", "(I)V", "itemInUseDuration", "getItemInUseDuration", "openContainer", "getOpenContainer", "prevChasingPosY", "", "getPrevChasingPosY", "()D", "sleepTimer", "getSleepTimer", "setSleepTimer", "sleeping", "getSleeping", "setSleeping", "(Z)V", "spectator", "isSpectator", "speedInAir", "getSpeedInAir", "setSpeedInAir", "attackTargetEntityWithCurrentItem", "", "entity", "fall", "distance", "damageMultiplier", "getCooledAttackStrength", "fl", "jump", "onCriticalHit", "onEnchantmentCritical", "resetCooldown", "stopUsingItem", "triggerAchievement", "stat", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "Pride"})
public interface IEntityPlayer
extends IEntityLivingBase {
    @NotNull
    public GameProfile getGameProfile();

    @Nullable
    public IEntity getFishEntity();

    @NotNull
    public IFoodStats getFoodStats();

    public double getPrevChasingPosY();

    public int getSleepTimer();

    public void setSleepTimer(int var1);

    public boolean getSleeping();

    public void setSleeping(boolean var1);

    public boolean isPlayerSleeping();

    public float getSpeedInAir();

    public void setSpeedInAir(float var1);

    public float getCameraYaw();

    public void setCameraYaw(float var1);

    @JvmName(name="isBlocking")
    public boolean isBlocking();

    public int getItemInUseCount();

    public void setItemInUseCount(int var1);

    @Nullable
    public IItemStack getItemInUse();

    @NotNull
    public IPlayerCapabilities getCapabilities();

    @Nullable
    public IItemStack getHeldItem();

    public boolean isUsingItem();

    @NotNull
    public IContainer getInventoryContainer();

    @NotNull
    public IInventoryPlayer getInventory();

    @Nullable
    public IContainer getOpenContainer();

    public int getItemInUseDuration();

    @NotNull
    public String getDisplayNameString();

    @JvmName(name="isSpectator")
    public boolean isSpectator();

    public void stopUsingItem();

    public void onCriticalHit(@NotNull IEntity var1);

    public void onEnchantmentCritical(@NotNull IEntityLivingBase var1);

    public void attackTargetEntityWithCurrentItem(@NotNull IEntity var1);

    public void fall(float var1, float var2);

    public void triggerAchievement(@NotNull IStatBase var1);

    public void jump();

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    public float getCooledAttackStrength(float var1);

    public void resetCooldown();
}
