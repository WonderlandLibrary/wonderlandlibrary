// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;

public class CombatTracker
{
    private final List<CombatEntry> combatEntries;
    private final EntityLivingBase fighter;
    private int lastDamageTime;
    private int combatStartTime;
    private int combatEndTime;
    private boolean inCombat;
    private boolean takingDamage;
    private String fallSuffix;
    
    public CombatTracker(final EntityLivingBase fighterIn) {
        this.combatEntries = (List<CombatEntry>)Lists.newArrayList();
        this.fighter = fighterIn;
    }
    
    public void calculateFallSuffix() {
        this.resetFallSuffix();
        if (this.fighter.isOnLadder()) {
            final Block block = this.fighter.world.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
            if (block == Blocks.LADDER) {
                this.fallSuffix = "ladder";
            }
            else if (block == Blocks.VINE) {
                this.fallSuffix = "vines";
            }
        }
        else if (this.fighter.isInWater()) {
            this.fallSuffix = "water";
        }
    }
    
    public void trackDamage(final DamageSource damageSrc, final float healthIn, final float damageAmount) {
        this.reset();
        this.calculateFallSuffix();
        final CombatEntry combatentry = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.fallSuffix, this.fighter.fallDistance);
        this.combatEntries.add(combatentry);
        this.lastDamageTime = this.fighter.ticksExisted;
        this.takingDamage = true;
        if (combatentry.isLivingDamageSrc() && !this.inCombat && this.fighter.isEntityAlive()) {
            this.inCombat = true;
            this.combatStartTime = this.fighter.ticksExisted;
            this.combatEndTime = this.combatStartTime;
            this.fighter.sendEnterCombat();
        }
    }
    
    public ITextComponent getDeathMessage() {
        if (this.combatEntries.isEmpty()) {
            return new TextComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName() });
        }
        final CombatEntry combatentry = this.getBestCombatEntry();
        final CombatEntry combatentry2 = this.combatEntries.get(this.combatEntries.size() - 1);
        final ITextComponent itextcomponent1 = combatentry2.getDamageSrcDisplayName();
        final Entity entity = combatentry2.getDamageSrc().getTrueSource();
        ITextComponent itextcomponent3;
        if (combatentry != null && combatentry2.getDamageSrc() == DamageSource.FALL) {
            final ITextComponent itextcomponent2 = combatentry.getDamageSrcDisplayName();
            if (combatentry.getDamageSrc() != DamageSource.FALL && combatentry.getDamageSrc() != DamageSource.OUT_OF_WORLD) {
                if (itextcomponent2 != null && (itextcomponent1 == null || !itextcomponent2.equals(itextcomponent1))) {
                    final Entity entity2 = combatentry.getDamageSrc().getTrueSource();
                    final ItemStack itemstack1 = (entity2 instanceof EntityLivingBase) ? ((EntityLivingBase)entity2).getHeldItemMainhand() : ItemStack.EMPTY;
                    if (!itemstack1.isEmpty() && itemstack1.hasDisplayName()) {
                        itextcomponent3 = new TextComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), itextcomponent2, itemstack1.getTextComponent() });
                    }
                    else {
                        itextcomponent3 = new TextComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), itextcomponent2 });
                    }
                }
                else if (itextcomponent1 != null) {
                    final ItemStack itemstack2 = (entity instanceof EntityLivingBase) ? ((EntityLivingBase)entity).getHeldItemMainhand() : ItemStack.EMPTY;
                    if (!itemstack2.isEmpty() && itemstack2.hasDisplayName()) {
                        itextcomponent3 = new TextComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), itextcomponent1, itemstack2.getTextComponent() });
                    }
                    else {
                        itextcomponent3 = new TextComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), itextcomponent1 });
                    }
                }
                else {
                    itextcomponent3 = new TextComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName() });
                }
            }
            else {
                itextcomponent3 = new TextComponentTranslation("death.fell.accident." + this.getFallSuffix(combatentry), new Object[] { this.fighter.getDisplayName() });
            }
        }
        else {
            itextcomponent3 = combatentry2.getDamageSrc().getDeathMessage(this.fighter);
        }
        return itextcomponent3;
    }
    
    @Nullable
    public EntityLivingBase getBestAttacker() {
        EntityLivingBase entitylivingbase = null;
        EntityPlayer entityplayer = null;
        float f = 0.0f;
        float f2 = 0.0f;
        for (final CombatEntry combatentry : this.combatEntries) {
            if (combatentry.getDamageSrc().getTrueSource() instanceof EntityPlayer && (entityplayer == null || combatentry.getDamage() > f2)) {
                f2 = combatentry.getDamage();
                entityplayer = (EntityPlayer)combatentry.getDamageSrc().getTrueSource();
            }
            if (combatentry.getDamageSrc().getTrueSource() instanceof EntityLivingBase && (entitylivingbase == null || combatentry.getDamage() > f)) {
                f = combatentry.getDamage();
                entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getTrueSource();
            }
        }
        if (entityplayer != null && f2 >= f / 3.0f) {
            return entityplayer;
        }
        return entitylivingbase;
    }
    
    @Nullable
    private CombatEntry getBestCombatEntry() {
        CombatEntry combatentry = null;
        CombatEntry combatentry2 = null;
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i < this.combatEntries.size(); ++i) {
            final CombatEntry combatentry3 = this.combatEntries.get(i);
            final CombatEntry combatentry4 = (i > 0) ? this.combatEntries.get(i - 1) : null;
            if ((combatentry3.getDamageSrc() == DamageSource.FALL || combatentry3.getDamageSrc() == DamageSource.OUT_OF_WORLD) && combatentry3.getDamageAmount() > 0.0f && (combatentry == null || combatentry3.getDamageAmount() > f2)) {
                if (i > 0) {
                    combatentry = combatentry4;
                }
                else {
                    combatentry = combatentry3;
                }
                f2 = combatentry3.getDamageAmount();
            }
            if (combatentry3.getFallSuffix() != null && (combatentry2 == null || combatentry3.getDamage() > f)) {
                combatentry2 = combatentry3;
                f = combatentry3.getDamage();
            }
        }
        if (f2 > 5.0f && combatentry != null) {
            return combatentry;
        }
        if (f > 5.0f && combatentry2 != null) {
            return combatentry2;
        }
        return null;
    }
    
    private String getFallSuffix(final CombatEntry entry) {
        return (entry.getFallSuffix() == null) ? "generic" : entry.getFallSuffix();
    }
    
    public int getCombatDuration() {
        return this.inCombat ? (this.fighter.ticksExisted - this.combatStartTime) : (this.combatEndTime - this.combatStartTime);
    }
    
    private void resetFallSuffix() {
        this.fallSuffix = null;
    }
    
    public void reset() {
        final int i = this.inCombat ? 300 : 100;
        if (this.takingDamage && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.lastDamageTime > i)) {
            final boolean flag = this.inCombat;
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
