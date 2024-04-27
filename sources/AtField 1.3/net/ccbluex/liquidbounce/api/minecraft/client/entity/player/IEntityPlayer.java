/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  kotlin.Metadata
 *  kotlin.jvm.JvmName
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020\u0011H&J\u0018\u0010L\u001a\u00020J2\u0006\u0010M\u001a\u00020\u00032\u0006\u0010N\u001a\u00020\u0003H&J\u0010\u0010O\u001a\u00020\u00032\u0006\u0010P\u001a\u00020\u0003H'J\b\u0010Q\u001a\u00020JH&J\u0010\u0010R\u001a\u00020J2\u0006\u0010K\u001a\u00020\u0011H&J\u0010\u0010S\u001a\u00020J2\u0006\u0010K\u001a\u00020\u0001H&J\b\u0010T\u001a\u00020JH&J\b\u0010U\u001a\u00020JH&J\u0010\u0010V\u001a\u00020J2\u0006\u0010W\u001a\u00020XH&R\u0018\u0010\u0002\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0012\u0010\u0014\u001a\u00020\u0015X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0018\u001a\u00020\u0019X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u0012\u0010 \u001a\u00020!X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010#R\u0012\u0010$\u001a\u00020%X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b&\u0010'R\u0014\u0010(\u001a\u00020)8gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b(\u0010*R\u0012\u0010+\u001a\u00020)X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b+\u0010*R\u0012\u0010,\u001a\u00020)X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010*R\u0014\u0010-\u001a\u0004\u0018\u00010\u001dX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010\u001fR\u0018\u0010/\u001a\u000200X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u0012\u00105\u001a\u000200X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b6\u00102R\u0014\u00107\u001a\u0004\u0018\u00010%X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u0010'R\u0012\u00109\u001a\u00020:X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b;\u0010<R\u0018\u0010=\u001a\u000200X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b>\u00102\"\u0004\b?\u00104R\u0018\u0010@\u001a\u00020)X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bA\u0010*\"\u0004\bB\u0010CR\u0014\u0010D\u001a\u00020)8gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\bE\u0010*R\u0018\u0010F\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\bG\u0010\u0005\"\u0004\bH\u0010\u0007\u00a8\u0006Y"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "cameraYaw", "", "getCameraYaw", "()F", "setCameraYaw", "(F)V", "capabilities", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "getCapabilities", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "displayNameString", "", "getDisplayNameString", "()Ljava/lang/String;", "fishEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getFishEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "foodStats", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "getFoodStats", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "gameProfile", "Lcom/mojang/authlib/GameProfile;", "getGameProfile", "()Lcom/mojang/authlib/GameProfile;", "heldItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getHeldItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "inventory", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "getInventory", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "inventoryContainer", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventoryContainer", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "isBlocking", "", "()Z", "isPlayerSleeping", "isUsingItem", "itemInUse", "getItemInUse", "itemInUseCount", "", "getItemInUseCount", "()I", "setItemInUseCount", "(I)V", "itemInUseDuration", "getItemInUseDuration", "openContainer", "getOpenContainer", "prevChasingPosY", "", "getPrevChasingPosY", "()D", "sleepTimer", "getSleepTimer", "setSleepTimer", "sleeping", "getSleeping", "setSleeping", "(Z)V", "spectator", "isSpectator", "speedInAir", "getSpeedInAir", "setSpeedInAir", "attackTargetEntityWithCurrentItem", "", "entity", "fall", "distance", "damageMultiplier", "getCooledAttackStrength", "fl", "jump", "onCriticalHit", "onEnchantmentCritical", "resetCooldown", "stopUsingItem", "triggerAchievement", "stat", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "AtField"})
public interface IEntityPlayer
extends IEntityLivingBase {
    public float getSpeedInAir();

    public void jump();

    public int getItemInUseCount();

    public void setSleepTimer(int var1);

    @NotNull
    public String getDisplayNameString();

    public void setSpeedInAir(float var1);

    @Nullable
    public IItemStack getHeldItem();

    public void attackTargetEntityWithCurrentItem(@NotNull IEntity var1);

    public boolean getSleeping();

    @Nullable
    public IContainer getOpenContainer();

    @NotNull
    public IContainer getInventoryContainer();

    public void resetCooldown();

    public void fall(float var1, float var2);

    public void triggerAchievement(@NotNull IStatBase var1);

    @NotNull
    public IInventoryPlayer getInventory();

    public double getPrevChasingPosY();

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    public float getCooledAttackStrength(float var1);

    @JvmName(name="isBlocking")
    public boolean isBlocking();

    public boolean isUsingItem();

    public float getCameraYaw();

    public void setCameraYaw(float var1);

    public boolean isPlayerSleeping();

    public void onCriticalHit(@NotNull IEntity var1);

    @Nullable
    public IEntity getFishEntity();

    @Nullable
    public IItemStack getItemInUse();

    @NotNull
    public IFoodStats getFoodStats();

    public void setSleeping(boolean var1);

    @NotNull
    public IPlayerCapabilities getCapabilities();

    @NotNull
    public GameProfile getGameProfile();

    public int getItemInUseDuration();

    public void stopUsingItem();

    @JvmName(name="isSpectator")
    public boolean isSpectator();

    public void onEnchantmentCritical(@NotNull IEntityLivingBase var1);

    public void setItemInUseCount(int var1);

    public int getSleepTimer();
}

