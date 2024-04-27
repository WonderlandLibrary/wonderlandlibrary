/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.EnumHand
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.EnumCreatureAttributeImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.ccbluex.liquidbounce.injection.backend.PotionEffectImpl;
import net.ccbluex.liquidbounce.injection.backend.PotionImpl;
import net.ccbluex.liquidbounce.injection.backend.TeamImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumHand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000h\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020\tH\u0016J\u0010\u0010A\u001a\u00020\u001f2\u0006\u0010B\u001a\u00020CH\u0016J\u0012\u0010D\u001a\u0004\u0018\u00010\t2\u0006\u0010E\u001a\u00020FH\u0016J\u0012\u0010G\u001a\u0004\u0018\u00010H2\u0006\u0010I\u001a\u00020\u001bH\u0016J\u0010\u0010J\u001a\u00020\u001f2\u0006\u0010E\u001a\u00020FH\u0016J\u0010\u0010K\u001a\u00020?2\u0006\u0010L\u001a\u00020\u001bH\u0016J\b\u0010M\u001a\u00020?H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR$\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R$\u0010\u0017\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0018\u0010\u0010\"\u0004\b\u0019\u0010\u0012R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010 R\u0014\u0010!\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010 R$\u0010\"\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010\u0010\"\u0004\b$\u0010\u0012R\u0014\u0010%\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b&\u0010\u0010R\u0014\u0010'\u001a\u00020\u001bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0014\u0010)\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u001dR\u0014\u0010+\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0014\u0010-\u001a\u00020\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010\u0010R$\u0010/\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b0\u0010\u0010\"\u0004\b1\u0010\u0012R$\u00102\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b3\u0010\u0010\"\u0004\b4\u0010\u0012R$\u00105\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b6\u0010\u0010\"\u0004\b7\u0010\u0012R\u0016\u00108\u001a\u0004\u0018\u0001098VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b:\u0010;R\u0014\u0010<\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b=\u0010\u001d\u00a8\u0006N"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EntityLivingBaseImpl;", "T", "Lnet/minecraft/entity/EntityLivingBase;", "Lnet/ccbluex/liquidbounce/injection/backend/EntityImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "wrapped", "(Lnet/minecraft/entity/EntityLivingBase;)V", "activePotionEffects", "", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "getActivePotionEffects", "()Ljava/util/Collection;", "value", "", "cameraPitch", "getCameraPitch", "()F", "setCameraPitch", "(F)V", "creatureAttribute", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "getCreatureAttribute", "()Lnet/ccbluex/liquidbounce/api/minecraft/entity/IEnumCreatureAttribute;", "health", "getHealth", "setHealth", "hurtTime", "", "getHurtTime", "()I", "isOnLadder", "", "()Z", "isSwingInProgress", "jumpMovementFactor", "getJumpMovementFactor", "setJumpMovementFactor", "maxHealth", "getMaxHealth", "maxHurtTime", "getMaxHurtTime", "maxhurtTime", "getMaxhurtTime", "moveForward", "getMoveForward", "moveStrafing", "getMoveStrafing", "prevRotationYawHead", "getPrevRotationYawHead", "setPrevRotationYawHead", "renderYawOffset", "getRenderYawOffset", "setRenderYawOffset", "rotationYawHead", "getRotationYawHead", "setRotationYawHead", "team", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "getTeam", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "totalArmorValue", "getTotalArmorValue", "addPotionEffect", "", "effect", "canEntityBeSeen", "it", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getActivePotionEffect", "potion", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "getEquipmentInSlot", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "index", "isPotionActive", "removePotionEffectClient", "id", "swingItem", "LiKingSense"})
public class EntityLivingBaseImpl<T extends EntityLivingBase>
extends EntityImpl<T>
implements IEntityLivingBase {
    private final int maxHurtTime;

    @Override
    public float getMaxHealth() {
        return ((EntityLivingBase)this.getWrapped()).func_110138_aP();
    }

    @Override
    public float getPrevRotationYawHead() {
        return ((EntityLivingBase)this.getWrapped()).field_70758_at;
    }

    @Override
    public void setPrevRotationYawHead(float value) {
        ((EntityLivingBase)this.getWrapped()).field_70758_at = value;
    }

    @Override
    public float getRenderYawOffset() {
        return ((EntityLivingBase)this.getWrapped()).field_70761_aq;
    }

    @Override
    public void setRenderYawOffset(float value) {
        ((EntityLivingBase)this.getWrapped()).field_70761_aq = value;
    }

    @Override
    @NotNull
    public Collection<IPotionEffect> getActivePotionEffects() {
        Collection collection = ((EntityLivingBase)this.getWrapped()).func_70651_bq();
        Intrinsics.checkExpressionValueIsNotNull((Object)collection, (String)"wrapped.activePotionEffects");
        return new WrappedCollection(collection, activePotionEffects.1.INSTANCE, activePotionEffects.2.INSTANCE);
    }

    @Override
    public boolean isSwingInProgress() {
        return ((EntityLivingBase)this.getWrapped()).field_82175_bq;
    }

    @Override
    public float getCameraPitch() {
        return ((EntityLivingBase)this.getWrapped()).field_70726_aT;
    }

    @Override
    public void setCameraPitch(float value) {
        ((EntityLivingBase)this.getWrapped()).field_70726_aT = value;
    }

    @Override
    @Nullable
    public ITeam getTeam() {
        ITeam iTeam;
        Team team = ((EntityLivingBase)this.getWrapped()).func_96124_cp();
        if (team != null) {
            Team $this$wrap$iv = team;
            boolean $i$f$wrap = false;
            iTeam = new TeamImpl($this$wrap$iv);
        } else {
            iTeam = null;
        }
        return iTeam;
    }

    @Override
    @NotNull
    public IEnumCreatureAttribute getCreatureAttribute() {
        EnumCreatureAttribute enumCreatureAttribute = ((EntityLivingBase)this.getWrapped()).func_70668_bt();
        Intrinsics.checkExpressionValueIsNotNull((Object)enumCreatureAttribute, (String)"wrapped.creatureAttribute");
        EnumCreatureAttribute $this$wrap$iv = enumCreatureAttribute;
        boolean $i$f$wrap = false;
        return new EnumCreatureAttributeImpl($this$wrap$iv);
    }

    @Override
    public int getHurtTime() {
        return ((EntityLivingBase)this.getWrapped()).field_70737_aN;
    }

    @Override
    public int getMaxhurtTime() {
        return ((EntityLivingBase)this.getWrapped()).field_70738_aO;
    }

    @Override
    public boolean isOnLadder() {
        return ((EntityLivingBase)this.getWrapped()).func_70617_f_();
    }

    @Override
    public float getJumpMovementFactor() {
        return ((EntityLivingBase)this.getWrapped()).field_70747_aH;
    }

    @Override
    public void setJumpMovementFactor(float value) {
        ((EntityLivingBase)this.getWrapped()).field_70747_aH = value;
    }

    @Override
    public float getMoveStrafing() {
        return ((EntityLivingBase)this.getWrapped()).field_70702_br;
    }

    @Override
    public float getMoveForward() {
        return ((EntityLivingBase)this.getWrapped()).field_191988_bg;
    }

    @Override
    public float getHealth() {
        return ((EntityLivingBase)this.getWrapped()).func_110143_aJ();
    }

    @Override
    public void setHealth(float value) {
        ((EntityLivingBase)this.getWrapped()).func_70606_j(value);
    }

    @Override
    public float getRotationYawHead() {
        return ((EntityLivingBase)this.getWrapped()).field_70759_as;
    }

    @Override
    public void setRotationYawHead(float value) {
        ((EntityLivingBase)this.getWrapped()).field_70759_as = value;
    }

    @Override
    public int getTotalArmorValue() {
        return ((EntityLivingBase)this.getWrapped()).func_70658_aO();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean canEntityBeSeen(@NotNull IEntity it) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)it, (String)"it");
        IEntity iEntity = it;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return entityLivingBase.func_70685_l(t2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isPotionActive(@NotNull IPotion potion) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)potion, (String)"potion");
        IPotion iPotion = potion;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$unwrap = false;
        Potion potion2 = ((PotionImpl)$this$unwrap$iv).getWrapped();
        return entityLivingBase.func_70644_a(potion2);
    }

    @Override
    public void swingItem() {
        ((EntityLivingBase)this.getWrapped()).func_184609_a(EnumHand.MAIN_HAND);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public IPotionEffect getActivePotionEffect(@NotNull IPotion potion) {
        IPotionEffect iPotionEffect;
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)potion, (String)"potion");
        IPotion iPotion = potion;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$unwrap = false;
        Potion potion2 = ((PotionImpl)$this$unwrap$iv).getWrapped();
        PotionEffect potionEffect = entityLivingBase.func_70660_b(potion2);
        if (potionEffect != null) {
            PotionEffect $this$wrap$iv = potionEffect;
            boolean $i$f$wrap = false;
            iPotionEffect = new PotionEffectImpl($this$wrap$iv);
        } else {
            iPotionEffect = null;
        }
        return iPotionEffect;
    }

    @Override
    public void removePotionEffectClient(int id) {
        ((EntityLivingBase)this.getWrapped()).func_184596_c(Potion.func_188412_a((int)id));
    }

    @Override
    public int getMaxHurtTime() {
        return this.maxHurtTime;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void addPotionEffect(@NotNull IPotionEffect effect) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)effect, (String)"effect");
        IPotionEffect iPotionEffect = effect;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$unwrap = false;
        PotionEffect potionEffect = ((PotionEffectImpl)$this$unwrap$iv).getWrapped();
        entityLivingBase.func_70690_d(potionEffect);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public IItemStack getEquipmentInSlot(int index) {
        EntityEquipmentSlot entityEquipmentSlot;
        void $this$toEntityEquipmentSlot$iv;
        int n = index;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$toEntityEquipmentSlot = false;
        switch ($this$toEntityEquipmentSlot$iv) {
            case 0: {
                entityEquipmentSlot = EntityEquipmentSlot.FEET;
                break;
            }
            case 1: {
                entityEquipmentSlot = EntityEquipmentSlot.LEGS;
                break;
            }
            case 2: {
                entityEquipmentSlot = EntityEquipmentSlot.CHEST;
                break;
            }
            case 3: {
                entityEquipmentSlot = EntityEquipmentSlot.HEAD;
                break;
            }
            case 4: {
                entityEquipmentSlot = EntityEquipmentSlot.MAINHAND;
                break;
            }
            case 5: {
                entityEquipmentSlot = EntityEquipmentSlot.OFFHAND;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + (int)$this$toEntityEquipmentSlot$iv);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        ItemStack itemStack = entityLivingBase.func_184582_a(entityEquipmentSlot2);
        Intrinsics.checkExpressionValueIsNotNull((Object)itemStack, (String)"wrapped.getItemStackFrom\u2026.toEntityEquipmentSlot())");
        ItemStack $this$wrap$iv = itemStack;
        boolean $i$f$wrap = false;
        return new ItemStackImpl($this$wrap$iv);
    }

    public EntityLivingBaseImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Entity)wrapped);
        this.maxHurtTime = ((EntityLivingBase)wrapped).field_70738_aO;
    }
}

