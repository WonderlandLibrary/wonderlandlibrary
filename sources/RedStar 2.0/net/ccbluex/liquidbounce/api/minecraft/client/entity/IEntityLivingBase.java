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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000T\n\n\n\u0000\n\n\n\b\n\n\b\n\n\b\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\bf\u000020J405260H&J70280H&J902:0;H&J<0=2>0H&J?02:0;H&J@052A0H&J\bB05H&R\b00X¦¢\bR0\bX¦¢\f\b\t\n\"\b\fR\r0X¦¢\bR0\bX¦¢\f\b\n\"\b\fR0X¦¢\bR0X¦¢\bR0X¦¢\bR0\bX¦¢\f\b\n\"\b\fR0\bX¦¢\b \nR!0\bX¦¢\b\"\nR#0\bX¦¢\b$\nR%0\bX¦¢\f\b&\n\"\b'\fR(0\bX¦¢\f\b)\n\"\b*\fR+0\bX¦¢\f\b,\n\"\b-\fR.0/X¦¢\b01R20X¦¢\b3¨C"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "activePotionEffects", "", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "getActivePotionEffects", "()Ljava/util/Collection;", "cameraPitch", "", "getCameraPitch", "()F", "setCameraPitch", "(F)V", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getCreatureAttribute", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "health", "getHealth", "setHealth", "hurtTime", "", "getHurtTime", "()I", "isOnLadder", "", "()Z", "isSwingInProgress", "jumpMovementFactor", "getJumpMovementFactor", "setJumpMovementFactor", "maxHealth", "getMaxHealth", "moveForward", "getMoveForward", "moveStrafing", "getMoveStrafing", "prevRotationYawHead", "getPrevRotationYawHead", "setPrevRotationYawHead", "renderYawOffset", "getRenderYawOffset", "setRenderYawOffset", "rotationYawHead", "getRotationYawHead", "setRotationYawHead", "team", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "getTeam", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "totalArmorValue", "getTotalArmorValue", "addPotionEffect", "", "effect", "canEntityBeSeen", "it", "getActivePotionEffect", "potion", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "getEquipmentInSlot", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "index", "isPotionActive", "removePotionEffectClient", "id", "swingItem", "Pride"})
public interface IEntityLivingBase
extends IEntity {
    public float getMaxHealth();

    public float getPrevRotationYawHead();

    public void setPrevRotationYawHead(float var1);

    public int getTotalArmorValue();

    public float getRenderYawOffset();

    public void setRenderYawOffset(float var1);

    @NotNull
    public Collection<IPotionEffect> getActivePotionEffects();

    public boolean isSwingInProgress();

    public float getCameraPitch();

    public void setCameraPitch(float var1);

    @Nullable
    public ITeam getTeam();

    @NotNull
    public IEnumCreatureAttribute getCreatureAttribute();

    public int getHurtTime();

    public boolean isOnLadder();

    public float getJumpMovementFactor();

    public void setJumpMovementFactor(float var1);

    public float getMoveStrafing();

    public float getMoveForward();

    public float getHealth();

    public void setHealth(float var1);

    public float getRotationYawHead();

    public void setRotationYawHead(float var1);

    public boolean canEntityBeSeen(@NotNull IEntity var1);

    public boolean isPotionActive(@NotNull IPotion var1);

    public void swingItem();

    @Nullable
    public IPotionEffect getActivePotionEffect(@NotNull IPotion var1);

    public void removePotionEffectClient(int var1);

    public void addPotionEffect(@NotNull IPotionEffect var1);

    @Nullable
    public IItemStack getEquipmentInSlot(int var1);
}
