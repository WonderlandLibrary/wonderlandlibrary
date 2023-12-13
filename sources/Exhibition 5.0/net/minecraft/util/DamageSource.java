// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.world.Explosion;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class DamageSource
{
    public static DamageSource inFire;
    public static DamageSource field_180137_b;
    public static DamageSource onFire;
    public static DamageSource lava;
    public static DamageSource inWall;
    public static DamageSource drown;
    public static DamageSource starve;
    public static DamageSource cactus;
    public static DamageSource fall;
    public static DamageSource outOfWorld;
    public static DamageSource generic;
    public static DamageSource magic;
    public static DamageSource wither;
    public static DamageSource anvil;
    public static DamageSource fallingBlock;
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    private static final String __OBFID = "CL_00001521";
    
    public static DamageSource causeMobDamage(final EntityLivingBase p_76358_0_) {
        return new EntityDamageSource("mob", p_76358_0_);
    }
    
    public static DamageSource causePlayerDamage(final EntityPlayer p_76365_0_) {
        return new EntityDamageSource("player", p_76365_0_);
    }
    
    public static DamageSource causeArrowDamage(final EntityArrow p_76353_0_, final Entity p_76353_1_) {
        return new EntityDamageSourceIndirect("arrow", p_76353_0_, p_76353_1_).setProjectile();
    }
    
    public static DamageSource causeFireballDamage(final EntityFireball p_76362_0_, final Entity p_76362_1_) {
        return (p_76362_1_ == null) ? new EntityDamageSourceIndirect("onFire", p_76362_0_, p_76362_0_).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", p_76362_0_, p_76362_1_).setFireDamage().setProjectile();
    }
    
    public static DamageSource causeThrownDamage(final Entity p_76356_0_, final Entity p_76356_1_) {
        return new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_).setProjectile();
    }
    
    public static DamageSource causeIndirectMagicDamage(final Entity p_76354_0_, final Entity p_76354_1_) {
        return new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_).setDamageBypassesArmor().setMagicDamage();
    }
    
    public static DamageSource causeThornsDamage(final Entity p_92087_0_) {
        return new EntityDamageSource("thorns", p_92087_0_).func_180138_v().setMagicDamage();
    }
    
    public static DamageSource setExplosionSource(final Explosion p_94539_0_) {
        return (p_94539_0_ != null && p_94539_0_.getExplosivePlacedBy() != null) ? new EntityDamageSource("explosion.player", p_94539_0_.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }
    
    public boolean isProjectile() {
        return this.projectile;
    }
    
    public DamageSource setProjectile() {
        this.projectile = true;
        return this;
    }
    
    public boolean isExplosion() {
        return this.explosion;
    }
    
    public DamageSource setExplosion() {
        this.explosion = true;
        return this;
    }
    
    public boolean isUnblockable() {
        return this.isUnblockable;
    }
    
    public float getHungerDamage() {
        return this.hungerDamage;
    }
    
    public boolean canHarmInCreative() {
        return this.isDamageAllowedInCreativeMode;
    }
    
    public boolean isDamageAbsolute() {
        return this.damageIsAbsolute;
    }
    
    protected DamageSource(final String p_i1566_1_) {
        this.hungerDamage = 0.3f;
        this.damageType = p_i1566_1_;
    }
    
    public Entity getSourceOfDamage() {
        return this.getEntity();
    }
    
    public Entity getEntity() {
        return null;
    }
    
    protected DamageSource setDamageBypassesArmor() {
        this.isUnblockable = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }
    
    protected DamageSource setDamageIsAbsolute() {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setFireDamage() {
        this.fireDamage = true;
        return this;
    }
    
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final EntityLivingBase var2 = p_151519_1_.func_94060_bK();
        final String var3 = "death.attack." + this.damageType;
        final String var4 = var3 + ".player";
        return (var2 != null && StatCollector.canTranslate(var4)) ? new ChatComponentTranslation(var4, new Object[] { p_151519_1_.getDisplayName(), var2.getDisplayName() }) : new ChatComponentTranslation(var3, new Object[] { p_151519_1_.getDisplayName() });
    }
    
    public boolean isFireDamage() {
        return this.fireDamage;
    }
    
    public String getDamageType() {
        return this.damageType;
    }
    
    public DamageSource setDifficultyScaled() {
        this.difficultyScaled = true;
        return this;
    }
    
    public boolean isDifficultyScaled() {
        return this.difficultyScaled;
    }
    
    public boolean isMagicDamage() {
        return this.magicDamage;
    }
    
    public DamageSource setMagicDamage() {
        this.magicDamage = true;
        return this;
    }
    
    public boolean func_180136_u() {
        final Entity var1 = this.getEntity();
        return var1 instanceof EntityPlayer && ((EntityPlayer)var1).capabilities.isCreativeMode;
    }
    
    static {
        DamageSource.inFire = new DamageSource("inFire").setFireDamage();
        DamageSource.field_180137_b = new DamageSource("lightningBolt");
        DamageSource.onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
        DamageSource.lava = new DamageSource("lava").setFireDamage();
        DamageSource.inWall = new DamageSource("inWall").setDamageBypassesArmor();
        DamageSource.drown = new DamageSource("drown").setDamageBypassesArmor();
        DamageSource.starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
        DamageSource.cactus = new DamageSource("cactus");
        DamageSource.fall = new DamageSource("fall").setDamageBypassesArmor();
        DamageSource.outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        DamageSource.generic = new DamageSource("generic").setDamageBypassesArmor();
        DamageSource.magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
        DamageSource.wither = new DamageSource("wither").setDamageBypassesArmor();
        DamageSource.anvil = new DamageSource("anvil");
        DamageSource.fallingBlock = new DamageSource("fallingBlock");
    }
}
