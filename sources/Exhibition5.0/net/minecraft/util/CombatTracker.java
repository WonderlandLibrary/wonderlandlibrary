// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;

public class CombatTracker
{
    private final List combatEntries;
    private final EntityLivingBase fighter;
    private int field_94555_c;
    private int field_152775_d;
    private int field_152776_e;
    private boolean field_94552_d;
    private boolean field_94553_e;
    private String field_94551_f;
    private static final String __OBFID = "CL_00001520";
    
    public CombatTracker(final EntityLivingBase p_i1565_1_) {
        this.combatEntries = Lists.newArrayList();
        this.fighter = p_i1565_1_;
    }
    
    public void func_94545_a() {
        this.func_94542_g();
        if (this.fighter.isOnLadder()) {
            final Block var1 = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
            if (var1 == Blocks.ladder) {
                this.field_94551_f = "ladder";
            }
            else if (var1 == Blocks.vine) {
                this.field_94551_f = "vines";
            }
        }
        else if (this.fighter.isInWater()) {
            this.field_94551_f = "water";
        }
    }
    
    public void func_94547_a(final DamageSource p_94547_1_, final float p_94547_2_, final float p_94547_3_) {
        this.func_94549_h();
        this.func_94545_a();
        final CombatEntry var4 = new CombatEntry(p_94547_1_, this.fighter.ticksExisted, p_94547_2_, p_94547_3_, this.field_94551_f, this.fighter.fallDistance);
        this.combatEntries.add(var4);
        this.field_94555_c = this.fighter.ticksExisted;
        this.field_94553_e = true;
        if (var4.func_94559_f() && !this.field_94552_d && this.fighter.isEntityAlive()) {
            this.field_94552_d = true;
            this.field_152775_d = this.fighter.ticksExisted;
            this.field_152776_e = this.field_152775_d;
            this.fighter.func_152111_bt();
        }
    }
    
    public IChatComponent func_151521_b() {
        if (this.combatEntries.size() == 0) {
            return new ChatComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName() });
        }
        final CombatEntry var1 = this.func_94544_f();
        final CombatEntry var2 = this.combatEntries.get(this.combatEntries.size() - 1);
        final IChatComponent var3 = var2.func_151522_h();
        final Entity var4 = var2.getDamageSrc().getEntity();
        Object var8;
        if (var1 != null && var2.getDamageSrc() == DamageSource.fall) {
            final IChatComponent var5 = var1.func_151522_h();
            if (var1.getDamageSrc() != DamageSource.fall && var1.getDamageSrc() != DamageSource.outOfWorld) {
                if (var5 != null && (var3 == null || !var5.equals(var3))) {
                    final Entity var6 = var1.getDamageSrc().getEntity();
                    final ItemStack var7 = (var6 instanceof EntityLivingBase) ? ((EntityLivingBase)var6).getHeldItem() : null;
                    if (var7 != null && var7.hasDisplayName()) {
                        var8 = new ChatComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), var5, var7.getChatComponent() });
                    }
                    else {
                        var8 = new ChatComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), var5 });
                    }
                }
                else if (var3 != null) {
                    final ItemStack var9 = (var4 instanceof EntityLivingBase) ? ((EntityLivingBase)var4).getHeldItem() : null;
                    if (var9 != null && var9.hasDisplayName()) {
                        var8 = new ChatComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), var3, var9.getChatComponent() });
                    }
                    else {
                        var8 = new ChatComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), var3 });
                    }
                }
                else {
                    var8 = new ChatComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName() });
                }
            }
            else {
                var8 = new ChatComponentTranslation("death.fell.accident." + this.func_94548_b(var1), new Object[] { this.fighter.getDisplayName() });
            }
        }
        else {
            var8 = var2.getDamageSrc().getDeathMessage(this.fighter);
        }
        return (IChatComponent)var8;
    }
    
    public EntityLivingBase func_94550_c() {
        EntityLivingBase var1 = null;
        EntityPlayer var2 = null;
        float var3 = 0.0f;
        float var4 = 0.0f;
        for (final CombatEntry var6 : this.combatEntries) {
            if (var6.getDamageSrc().getEntity() instanceof EntityPlayer && (var2 == null || var6.func_94563_c() > var4)) {
                var4 = var6.func_94563_c();
                var2 = (EntityPlayer)var6.getDamageSrc().getEntity();
            }
            if (var6.getDamageSrc().getEntity() instanceof EntityLivingBase && (var1 == null || var6.func_94563_c() > var3)) {
                var3 = var6.func_94563_c();
                var1 = (EntityLivingBase)var6.getDamageSrc().getEntity();
            }
        }
        if (var2 != null && var4 >= var3 / 3.0f) {
            return var2;
        }
        return var1;
    }
    
    private CombatEntry func_94544_f() {
        CombatEntry var1 = null;
        CombatEntry var2 = null;
        final byte var3 = 0;
        float var4 = 0.0f;
        for (int var5 = 0; var5 < this.combatEntries.size(); ++var5) {
            final CombatEntry var6 = this.combatEntries.get(var5);
            final CombatEntry var7 = (var5 > 0) ? this.combatEntries.get(var5 - 1) : null;
            if ((var6.getDamageSrc() == DamageSource.fall || var6.getDamageSrc() == DamageSource.outOfWorld) && var6.func_94561_i() > 0.0f && (var1 == null || var6.func_94561_i() > var4)) {
                if (var5 > 0) {
                    var1 = var7;
                }
                else {
                    var1 = var6;
                }
                var4 = var6.func_94561_i();
            }
            if (var6.func_94562_g() != null && (var2 == null || var6.func_94563_c() > var3)) {
                var2 = var6;
            }
        }
        if (var4 > 5.0f && var1 != null) {
            return var1;
        }
        if (var3 > 5 && var2 != null) {
            return var2;
        }
        return null;
    }
    
    private String func_94548_b(final CombatEntry p_94548_1_) {
        return (p_94548_1_.func_94562_g() == null) ? "generic" : p_94548_1_.func_94562_g();
    }
    
    public int func_180134_f() {
        return this.field_94552_d ? (this.fighter.ticksExisted - this.field_152775_d) : (this.field_152776_e - this.field_152775_d);
    }
    
    private void func_94542_g() {
        this.field_94551_f = null;
    }
    
    public void func_94549_h() {
        final int var1 = this.field_94552_d ? 300 : 100;
        if (this.field_94553_e && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.field_94555_c > var1)) {
            final boolean var2 = this.field_94552_d;
            this.field_94553_e = false;
            this.field_94552_d = false;
            this.field_152776_e = this.fighter.ticksExisted;
            if (var2) {
                this.fighter.func_152112_bu();
            }
            this.combatEntries.clear();
        }
    }
    
    public EntityLivingBase func_180135_h() {
        return this.fighter;
    }
}
