/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CombatTracker {
    private final List<CombatEntry> combatEntries = Lists.newArrayList();
    private final EntityLivingBase fighter;
    private int lastDamageTime;
    private int combatStartTime;
    private int combatEndTime;
    private boolean inCombat;
    private boolean takingDamage;
    private String fallSuffix;

    public CombatTracker(EntityLivingBase fighterIn) {
        this.fighter = fighterIn;
    }

    public void calculateFallSuffix() {
        this.resetFallSuffix();
        if (this.fighter.isOnLadder()) {
            Block block = this.fighter.world.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
            if (block == Blocks.LADDER) {
                this.fallSuffix = "ladder";
            } else if (block == Blocks.VINE) {
                this.fallSuffix = "vines";
            }
        } else if (this.fighter.isInWater()) {
            this.fallSuffix = "water";
        }
    }

    public void trackDamage(DamageSource damageSrc, float healthIn, float damageAmount) {
        this.reset();
        this.calculateFallSuffix();
        CombatEntry combatentry = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.fallSuffix, this.fighter.fallDistance);
        this.combatEntries.add(combatentry);
        this.lastDamageTime = this.fighter.ticksExisted;
        this.takingDamage = true;
        if (combatentry.isLivingDamageSrc() && !this.inCombat && this.fighter.isEntityAlive()) {
            this.inCombat = true;
            this.combatEndTime = this.combatStartTime = this.fighter.ticksExisted;
            this.fighter.sendEnterCombat();
        }
    }

    public ITextComponent getDeathMessage() {
        ITextComponent itextcomponent;
        if (this.combatEntries.isEmpty()) {
            return new TextComponentTranslation("death.attack.generic", this.fighter.getDisplayName());
        }
        CombatEntry combatentry = this.getBestCombatEntry();
        CombatEntry combatentry1 = this.combatEntries.get(this.combatEntries.size() - 1);
        ITextComponent itextcomponent1 = combatentry1.getDamageSrcDisplayName();
        Entity entity = combatentry1.getDamageSrc().getEntity();
        if (combatentry != null && combatentry1.getDamageSrc() == DamageSource.fall) {
            ITextComponent itextcomponent2 = combatentry.getDamageSrcDisplayName();
            if (combatentry.getDamageSrc() != DamageSource.fall && combatentry.getDamageSrc() != DamageSource.outOfWorld) {
                if (!(itextcomponent2 == null || itextcomponent1 != null && itextcomponent2.equals(itextcomponent1))) {
                    ItemStack itemstack1;
                    Entity entity1 = combatentry.getDamageSrc().getEntity();
                    ItemStack itemStack = itemstack1 = entity1 instanceof EntityLivingBase ? ((EntityLivingBase)entity1).getHeldItemMainhand() : ItemStack.field_190927_a;
                    itextcomponent = !itemstack1.isEmpty() && itemstack1.hasDisplayName() ? new TextComponentTranslation("death.fell.assist.item", this.fighter.getDisplayName(), itextcomponent2, itemstack1.getTextComponent()) : new TextComponentTranslation("death.fell.assist", this.fighter.getDisplayName(), itextcomponent2);
                } else if (itextcomponent1 != null) {
                    ItemStack itemstack;
                    ItemStack itemStack = itemstack = entity instanceof EntityLivingBase ? ((EntityLivingBase)entity).getHeldItemMainhand() : ItemStack.field_190927_a;
                    itextcomponent = !itemstack.isEmpty() && itemstack.hasDisplayName() ? new TextComponentTranslation("death.fell.finish.item", this.fighter.getDisplayName(), itextcomponent1, itemstack.getTextComponent()) : new TextComponentTranslation("death.fell.finish", this.fighter.getDisplayName(), itextcomponent1);
                } else {
                    itextcomponent = new TextComponentTranslation("death.fell.killer", this.fighter.getDisplayName());
                }
            } else {
                itextcomponent = new TextComponentTranslation("death.fell.accident." + this.getFallSuffix(combatentry), this.fighter.getDisplayName());
            }
        } else {
            itextcomponent = combatentry1.getDamageSrc().getDeathMessage(this.fighter);
        }
        return itextcomponent;
    }

    @Nullable
    public EntityLivingBase getBestAttacker() {
        EntityLivingBase entitylivingbase = null;
        EntityPlayer entityplayer = null;
        float f = 0.0f;
        float f1 = 0.0f;
        for (CombatEntry combatentry : this.combatEntries) {
            if (combatentry.getDamageSrc().getEntity() instanceof EntityPlayer && (entityplayer == null || combatentry.getDamage() > f1)) {
                f1 = combatentry.getDamage();
                entityplayer = (EntityPlayer)combatentry.getDamageSrc().getEntity();
            }
            if (!(combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase) || entitylivingbase != null && !(combatentry.getDamage() > f)) continue;
            f = combatentry.getDamage();
            entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getEntity();
        }
        if (entityplayer != null && f1 >= f / 3.0f) {
            return entityplayer;
        }
        return entitylivingbase;
    }

    @Nullable
    private CombatEntry getBestCombatEntry() {
        CombatEntry combatentry = null;
        CombatEntry combatentry1 = null;
        float f = 0.0f;
        float f1 = 0.0f;
        for (int i = 0; i < this.combatEntries.size(); ++i) {
            CombatEntry combatentry3;
            CombatEntry combatentry2 = this.combatEntries.get(i);
            CombatEntry combatEntry = combatentry3 = i > 0 ? this.combatEntries.get(i - 1) : null;
            if ((combatentry2.getDamageSrc() == DamageSource.fall || combatentry2.getDamageSrc() == DamageSource.outOfWorld) && combatentry2.getDamageAmount() > 0.0f && (combatentry == null || combatentry2.getDamageAmount() > f1)) {
                combatentry = i > 0 ? combatentry3 : combatentry2;
                f1 = combatentry2.getDamageAmount();
            }
            if (combatentry2.getFallSuffix() == null || combatentry1 != null && !(combatentry2.getDamage() > f)) continue;
            combatentry1 = combatentry2;
            f = combatentry2.getDamage();
        }
        if (f1 > 5.0f && combatentry != null) {
            return combatentry;
        }
        if (f > 5.0f && combatentry1 != null) {
            return combatentry1;
        }
        return null;
    }

    private String getFallSuffix(CombatEntry entry) {
        return entry.getFallSuffix() == null ? "generic" : entry.getFallSuffix();
    }

    public int getCombatDuration() {
        return this.inCombat ? this.fighter.ticksExisted - this.combatStartTime : this.combatEndTime - this.combatStartTime;
    }

    private void resetFallSuffix() {
        this.fallSuffix = null;
    }

    public void reset() {
        int i;
        int n = i = this.inCombat ? 300 : 100;
        if (this.takingDamage && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.lastDamageTime > i)) {
            boolean flag = this.inCombat;
            this.takingDamage = false;
            this.inCombat = false;
            this.combatEndTime = this.fighter.ticksExisted;
            if (flag) {
                this.fighter.sendEndCombat();
            }
            this.combatEntries.clear();
        }
    }

    public EntityLivingBase getFighter() {
        return this.fighter;
    }
}

