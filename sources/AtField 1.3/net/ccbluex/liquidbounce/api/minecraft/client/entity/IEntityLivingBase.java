/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.client.entity;

import java.util.Collection;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0004H&J\u0010\u00105\u001a\u00020\u00192\u0006\u00106\u001a\u00020\u0001H&J\u0012\u00107\u001a\u0004\u0018\u00010\u00042\u0006\u00108\u001a\u000209H&J\u0012\u0010:\u001a\u0004\u0018\u00010;2\u0006\u0010<\u001a\u00020\u0015H&J\u0010\u0010=\u001a\u00020\u00192\u0006\u00108\u001a\u000209H&J\u0010\u0010>\u001a\u0002032\u0006\u0010?\u001a\u00020\u0015H&J\b\u0010@\u001a\u000203H&R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0012\u0010\r\u001a\u00020\u000eX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0011\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0012\u0010\n\"\u0004\b\u0013\u0010\fR\u0012\u0010\u0014\u001a\u00020\u0015X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0018\u001a\u00020\u0019X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u0012\u0010\u001b\u001a\u00020\u0019X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001aR\u0018\u0010\u001c\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u001d\u0010\n\"\u0004\b\u001e\u0010\fR\u0012\u0010\u001f\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010\nR\u0012\u0010!\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\"\u0010\nR\u0012\u0010#\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b$\u0010\nR\u0018\u0010%\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b&\u0010\n\"\u0004\b'\u0010\fR\u0018\u0010(\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b)\u0010\n\"\u0004\b*\u0010\fR\u0018\u0010+\u001a\u00020\bX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b,\u0010\n\"\u0004\b-\u0010\fR\u0014\u0010.\u001a\u0004\u0018\u00010/X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u00101\u00a8\u0006A"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "activePotionEffects", "", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "getActivePotionEffects", "()Ljava/util/Collection;", "cameraPitch", "", "getCameraPitch", "()F", "setCameraPitch", "(F)V", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getCreatureAttribute", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "health", "getHealth", "setHealth", "hurtTime", "", "getHurtTime", "()I", "isOnLadder", "", "()Z", "isSwingInProgress", "jumpMovementFactor", "getJumpMovementFactor", "setJumpMovementFactor", "maxHealth", "getMaxHealth", "moveForward", "getMoveForward", "moveStrafing", "getMoveStrafing", "prevRotationYawHead", "getPrevRotationYawHead", "setPrevRotationYawHead", "renderYawOffset", "getRenderYawOffset", "setRenderYawOffset", "rotationYawHead", "getRotationYawHead", "setRotationYawHead", "team", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "getTeam", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "addPotionEffect", "", "effect", "canEntityBeSeen", "it", "getActivePotionEffect", "potion", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "getEquipmentInSlot", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "index", "isPotionActive", "removePotionEffectClient", "id", "swingItem", "AtField"})
public interface IEntityLivingBase
extends IEntity {
    public float getRotationYawHead();

    @Nullable
    public IItemStack getEquipmentInSlot(int var1);

    public void swingItem();

    public void removePotionEffectClient(int var1);

    public float getMoveStrafing();

    public boolean isPotionActive(@NotNull IPotion var1);

    @NotNull
    public IEnumCreatureAttribute getCreatureAttribute();

    public boolean canEntityBeSeen(@NotNull IEntity var1);

    public void setJumpMovementFactor(float var1);

    public float getMoveForward();

    public float getHealth();

    public void setHealth(float var1);

    @Nullable
    public IPotionEffect getActivePotionEffect(@NotNull IPotion var1);

    public float getPrevRotationYawHead();

    public float getMaxHealth();

    @Nullable
    public ITeam getTeam();

    public int getHurtTime();

    public float getJumpMovementFactor();

    public boolean isSwingInProgress();

    public float getRenderYawOffset();

    public float getCameraPitch();

    public void setPrevRotationYawHead(float var1);

    public boolean isOnLadder();

    @NotNull
    public Collection getActivePotionEffects();

    public void addPotionEffect(@NotNull IPotionEffect var1);

    public void setRotationYawHead(float var1);

    public void setRenderYawOffset(float var1);

    public void setCameraPitch(float var1);
}

