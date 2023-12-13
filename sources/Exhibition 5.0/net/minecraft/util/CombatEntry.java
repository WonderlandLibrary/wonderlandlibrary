// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.entity.EntityLivingBase;

public class CombatEntry
{
    private final DamageSource damageSrc;
    private final int field_94567_b;
    private final float field_94568_c;
    private final float field_94565_d;
    private final String field_94566_e;
    private final float field_94564_f;
    private static final String __OBFID = "CL_00001519";
    
    public CombatEntry(final DamageSource p_i1564_1_, final int p_i1564_2_, final float p_i1564_3_, final float p_i1564_4_, final String p_i1564_5_, final float p_i1564_6_) {
        this.damageSrc = p_i1564_1_;
        this.field_94567_b = p_i1564_2_;
        this.field_94568_c = p_i1564_4_;
        this.field_94565_d = p_i1564_3_;
        this.field_94566_e = p_i1564_5_;
        this.field_94564_f = p_i1564_6_;
    }
    
    public DamageSource getDamageSrc() {
        return this.damageSrc;
    }
    
    public float func_94563_c() {
        return this.field_94568_c;
    }
    
    public boolean func_94559_f() {
        return this.damageSrc.getEntity() instanceof EntityLivingBase;
    }
    
    public String func_94562_g() {
        return this.field_94566_e;
    }
    
    public IChatComponent func_151522_h() {
        return (this.getDamageSrc().getEntity() == null) ? null : this.getDamageSrc().getEntity().getDisplayName();
    }
    
    public float func_94561_i() {
        return (this.damageSrc == DamageSource.outOfWorld) ? Float.MAX_VALUE : this.field_94564_f;
    }
}
