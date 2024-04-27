/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.EnumHand
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
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

public class EntityLivingBaseImpl
extends EntityImpl
implements IEntityLivingBase {
    @Override
    public float getPrevRotationYawHead() {
        return ((EntityLivingBase)this.getWrapped()).field_70758_at;
    }

    @Override
    public void swingItem() {
        ((EntityLivingBase)this.getWrapped()).func_184609_a(EnumHand.MAIN_HAND);
    }

    @Override
    public boolean isPotionActive(IPotion iPotion) {
        IPotion iPotion2 = iPotion;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean bl = false;
        Potion potion = ((PotionImpl)iPotion2).getWrapped();
        return entityLivingBase.func_70644_a(potion);
    }

    @Override
    public void addPotionEffect(IPotionEffect iPotionEffect) {
        IPotionEffect iPotionEffect2 = iPotionEffect;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean bl = false;
        PotionEffect potionEffect = ((PotionEffectImpl)iPotionEffect2).getWrapped();
        entityLivingBase.func_70690_d(potionEffect);
    }

    @Override
    public void setRotationYawHead(float f) {
        ((EntityLivingBase)this.getWrapped()).field_70759_as = f;
    }

    @Override
    public boolean isSwingInProgress() {
        return ((EntityLivingBase)this.getWrapped()).field_82175_bq;
    }

    @Override
    public int getHurtTime() {
        return ((EntityLivingBase)this.getWrapped()).field_70737_aN;
    }

    @Override
    public ITeam getTeam() {
        ITeam iTeam;
        Team team = ((EntityLivingBase)this.getWrapped()).func_96124_cp();
        if (team != null) {
            Team team2 = team;
            boolean bl = false;
            iTeam = new TeamImpl(team2);
        } else {
            iTeam = null;
        }
        return iTeam;
    }

    public EntityLivingBaseImpl(EntityLivingBase entityLivingBase) {
        super((Entity)entityLivingBase);
    }

    @Override
    public float getMaxHealth() {
        return ((EntityLivingBase)this.getWrapped()).func_110138_aP();
    }

    @Override
    public void setJumpMovementFactor(float f) {
        ((EntityLivingBase)this.getWrapped()).field_70747_aH = f;
    }

    @Override
    public float getMoveStrafing() {
        return ((EntityLivingBase)this.getWrapped()).field_70702_br;
    }

    @Override
    public void setPrevRotationYawHead(float f) {
        ((EntityLivingBase)this.getWrapped()).field_70758_at = f;
    }

    @Override
    public IPotionEffect getActivePotionEffect(IPotion iPotion) {
        IPotionEffect iPotionEffect;
        IPotion iPotion2 = iPotion;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean bl = false;
        Potion potion = ((PotionImpl)iPotion2).getWrapped();
        PotionEffect potionEffect = entityLivingBase.func_70660_b(potion);
        if (potionEffect != null) {
            iPotion2 = potionEffect;
            bl = false;
            iPotionEffect = new PotionEffectImpl((PotionEffect)iPotion2);
        } else {
            iPotionEffect = null;
        }
        return iPotionEffect;
    }

    @Override
    public float getRenderYawOffset() {
        return ((EntityLivingBase)this.getWrapped()).field_70761_aq;
    }

    @Override
    public boolean isOnLadder() {
        return ((EntityLivingBase)this.getWrapped()).func_70617_f_();
    }

    @Override
    public void removePotionEffectClient(int n) {
        ((EntityLivingBase)this.getWrapped()).func_184596_c(Potion.func_188412_a((int)n));
    }

    @Override
    public void setRenderYawOffset(float f) {
        ((EntityLivingBase)this.getWrapped()).field_70761_aq = f;
    }

    @Override
    public void setHealth(float f) {
        ((EntityLivingBase)this.getWrapped()).func_70606_j(f);
    }

    @Override
    public float getMoveForward() {
        return ((EntityLivingBase)this.getWrapped()).field_191988_bg;
    }

    @Override
    public IEnumCreatureAttribute getCreatureAttribute() {
        EnumCreatureAttribute enumCreatureAttribute = ((EntityLivingBase)this.getWrapped()).func_70668_bt();
        boolean bl = false;
        return new EnumCreatureAttributeImpl(enumCreatureAttribute);
    }

    @Override
    public float getHealth() {
        return ((EntityLivingBase)this.getWrapped()).func_110143_aJ();
    }

    @Override
    public float getJumpMovementFactor() {
        return ((EntityLivingBase)this.getWrapped()).field_70747_aH;
    }

    @Override
    public IItemStack getEquipmentInSlot(int n) {
        EntityEquipmentSlot entityEquipmentSlot;
        int n2 = n;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean bl = false;
        switch (n2) {
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
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + n2);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        ItemStack itemStack = entityLivingBase.func_184582_a(entityEquipmentSlot2);
        bl = false;
        return new ItemStackImpl(itemStack);
    }

    @Override
    public Collection getActivePotionEffects() {
        return new WrappedCollection(((EntityLivingBase)this.getWrapped()).func_70651_bq(), activePotionEffects.1.INSTANCE, activePotionEffects.2.INSTANCE);
    }

    @Override
    public float getCameraPitch() {
        return ((EntityLivingBase)this.getWrapped()).field_70726_aT;
    }

    @Override
    public void setCameraPitch(float f) {
        ((EntityLivingBase)this.getWrapped()).field_70726_aT = f;
    }

    @Override
    public float getRotationYawHead() {
        return ((EntityLivingBase)this.getWrapped()).field_70759_as;
    }

    @Override
    public boolean canEntityBeSeen(IEntity iEntity) {
        IEntity iEntity2 = iEntity;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean bl = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        return entityLivingBase.func_70685_l(entity);
    }
}

