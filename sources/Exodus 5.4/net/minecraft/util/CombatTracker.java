/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class CombatTracker {
    private int field_152776_e;
    private String field_94551_f;
    private int field_152775_d;
    private boolean field_94553_e;
    private boolean field_94552_d;
    private final List<CombatEntry> combatEntries = Lists.newArrayList();
    private int field_94555_c;
    private final EntityLivingBase fighter;

    public EntityLivingBase getFighter() {
        return this.fighter;
    }

    public void func_94545_a() {
        this.func_94542_g();
        if (this.fighter.isOnLadder()) {
            Block block = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
            if (block == Blocks.ladder) {
                this.field_94551_f = "ladder";
            } else if (block == Blocks.vine) {
                this.field_94551_f = "vines";
            }
        } else if (this.fighter.isInWater()) {
            this.field_94551_f = "water";
        }
    }

    public void reset() {
        int n;
        int n2 = n = this.field_94552_d ? 300 : 100;
        if (this.field_94553_e && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.field_94555_c > n)) {
            boolean bl = this.field_94552_d;
            this.field_94553_e = false;
            this.field_94552_d = false;
            this.field_152776_e = this.fighter.ticksExisted;
            if (bl) {
                this.fighter.sendEndCombat();
            }
            this.combatEntries.clear();
        }
    }

    public EntityLivingBase func_94550_c() {
        EntityLivingBase entityLivingBase = null;
        EntityPlayer entityPlayer = null;
        float f = 0.0f;
        float f2 = 0.0f;
        for (CombatEntry combatEntry : this.combatEntries) {
            if (combatEntry.getDamageSrc().getEntity() instanceof EntityPlayer && (entityPlayer == null || combatEntry.func_94563_c() > f2)) {
                f2 = combatEntry.func_94563_c();
                entityPlayer = (EntityPlayer)combatEntry.getDamageSrc().getEntity();
            }
            if (!(combatEntry.getDamageSrc().getEntity() instanceof EntityLivingBase) || entityLivingBase != null && !(combatEntry.func_94563_c() > f)) continue;
            f = combatEntry.func_94563_c();
            entityLivingBase = (EntityLivingBase)combatEntry.getDamageSrc().getEntity();
        }
        if (entityPlayer != null && f2 >= f / 3.0f) {
            return entityPlayer;
        }
        return entityLivingBase;
    }

    public int func_180134_f() {
        return this.field_94552_d ? this.fighter.ticksExisted - this.field_152775_d : this.field_152776_e - this.field_152775_d;
    }

    public void trackDamage(DamageSource damageSource, float f, float f2) {
        this.reset();
        this.func_94545_a();
        CombatEntry combatEntry = new CombatEntry(damageSource, this.fighter.ticksExisted, f, f2, this.field_94551_f, this.fighter.fallDistance);
        this.combatEntries.add(combatEntry);
        this.field_94555_c = this.fighter.ticksExisted;
        this.field_94553_e = true;
        if (combatEntry.isLivingDamageSrc() && !this.field_94552_d && this.fighter.isEntityAlive()) {
            this.field_94552_d = true;
            this.field_152776_e = this.field_152775_d = this.fighter.ticksExisted;
            this.fighter.sendEnterCombat();
        }
    }

    public IChatComponent getDeathMessage() {
        IChatComponent iChatComponent;
        if (this.combatEntries.size() == 0) {
            return new ChatComponentTranslation("death.attack.generic", this.fighter.getDisplayName());
        }
        CombatEntry combatEntry = this.func_94544_f();
        CombatEntry combatEntry2 = this.combatEntries.get(this.combatEntries.size() - 1);
        IChatComponent iChatComponent2 = combatEntry2.getDamageSrcDisplayName();
        Entity entity = combatEntry2.getDamageSrc().getEntity();
        if (combatEntry != null && combatEntry2.getDamageSrc() == DamageSource.fall) {
            IChatComponent iChatComponent3 = combatEntry.getDamageSrcDisplayName();
            if (combatEntry.getDamageSrc() != DamageSource.fall && combatEntry.getDamageSrc() != DamageSource.outOfWorld) {
                if (!(iChatComponent3 == null || iChatComponent2 != null && iChatComponent3.equals(iChatComponent2))) {
                    ItemStack itemStack;
                    Entity entity2 = combatEntry.getDamageSrc().getEntity();
                    ItemStack itemStack2 = itemStack = entity2 instanceof EntityLivingBase ? ((EntityLivingBase)entity2).getHeldItem() : null;
                    iChatComponent = itemStack != null && itemStack.hasDisplayName() ? new ChatComponentTranslation("death.fell.assist.item", this.fighter.getDisplayName(), iChatComponent3, itemStack.getChatComponent()) : new ChatComponentTranslation("death.fell.assist", this.fighter.getDisplayName(), iChatComponent3);
                } else if (iChatComponent2 != null) {
                    ItemStack itemStack;
                    ItemStack itemStack3 = itemStack = entity instanceof EntityLivingBase ? ((EntityLivingBase)entity).getHeldItem() : null;
                    iChatComponent = itemStack != null && itemStack.hasDisplayName() ? new ChatComponentTranslation("death.fell.finish.item", this.fighter.getDisplayName(), iChatComponent2, itemStack.getChatComponent()) : new ChatComponentTranslation("death.fell.finish", this.fighter.getDisplayName(), iChatComponent2);
                } else {
                    iChatComponent = new ChatComponentTranslation("death.fell.killer", this.fighter.getDisplayName());
                }
            } else {
                iChatComponent = new ChatComponentTranslation("death.fell.accident." + this.func_94548_b(combatEntry), this.fighter.getDisplayName());
            }
        } else {
            iChatComponent = combatEntry2.getDamageSrc().getDeathMessage(this.fighter);
        }
        return iChatComponent;
    }

    private void func_94542_g() {
        this.field_94551_f = null;
    }

    private CombatEntry func_94544_f() {
        CombatEntry combatEntry = null;
        CombatEntry combatEntry2 = null;
        int n = 0;
        float f = 0.0f;
        int n2 = 0;
        while (n2 < this.combatEntries.size()) {
            CombatEntry combatEntry3;
            CombatEntry combatEntry4 = this.combatEntries.get(n2);
            CombatEntry combatEntry5 = combatEntry3 = n2 > 0 ? this.combatEntries.get(n2 - 1) : null;
            if ((combatEntry4.getDamageSrc() == DamageSource.fall || combatEntry4.getDamageSrc() == DamageSource.outOfWorld) && combatEntry4.getDamageAmount() > 0.0f && (combatEntry == null || combatEntry4.getDamageAmount() > f)) {
                combatEntry = n2 > 0 ? combatEntry3 : combatEntry4;
                f = combatEntry4.getDamageAmount();
            }
            if (combatEntry4.func_94562_g() != null && (combatEntry2 == null || combatEntry4.func_94563_c() > (float)n)) {
                combatEntry2 = combatEntry4;
            }
            ++n2;
        }
        if (f > 5.0f && combatEntry != null) {
            return combatEntry;
        }
        if (n > 5 && combatEntry2 != null) {
            return combatEntry2;
        }
        return null;
    }

    private String func_94548_b(CombatEntry combatEntry) {
        return combatEntry.func_94562_g() == null ? "generic" : combatEntry.func_94562_g();
    }

    public CombatTracker(EntityLivingBase entityLivingBase) {
        this.fighter = entityLivingBase;
    }
}

