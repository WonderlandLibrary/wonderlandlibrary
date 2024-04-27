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

public class EntityLivingBaseImpl<T extends EntityLivingBase>
extends EntityImpl<T>
implements IEntityLivingBase {
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
    public int getTotalArmorValue() {
        return ((EntityLivingBase)this.getWrapped()).func_70658_aO();
    }

    @Override
    public Collection<IPotionEffect> getActivePotionEffects() {
        return new WrappedCollection(((EntityLivingBase)this.getWrapped()).func_70651_bq(), activePotionEffects.1.INSTANCE, activePotionEffects.2.INSTANCE);
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
    public IEnumCreatureAttribute getCreatureAttribute() {
        EnumCreatureAttribute $this$wrap$iv = ((EntityLivingBase)this.getWrapped()).func_70668_bt();
        boolean $i$f$wrap = false;
        return new EnumCreatureAttributeImpl($this$wrap$iv);
    }

    @Override
    public int getHurtTime() {
        return ((EntityLivingBase)this.getWrapped()).field_70737_aN;
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

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean canEntityBeSeen(IEntity it) {
        void $this$unwrap$iv;
        IEntity iEntity = it;
        EntityLivingBase entityLivingBase = (EntityLivingBase)this.getWrapped();
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        return entityLivingBase.func_70685_l(t);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isPotionActive(IPotion potion) {
        void $this$unwrap$iv;
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
    public IPotionEffect getActivePotionEffect(IPotion potion) {
        IPotionEffect iPotionEffect;
        void $this$unwrap$iv;
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

    /*
     * WARNING - void declaration
     */
    @Override
    public void addPotionEffect(IPotionEffect effect) {
        void $this$unwrap$iv;
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
        ItemStack $this$wrap$iv = entityLivingBase.func_184582_a(entityEquipmentSlot2);
        boolean $i$f$wrap = false;
        return new ItemStackImpl($this$wrap$iv);
    }

    public EntityLivingBaseImpl(T wrapped) {
        super((Entity)wrapped);
    }
}

