// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.entity.projectile.EntityFireball;
import javax.annotation.Nullable;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class DamageSource
{
    public static final DamageSource IN_FIRE;
    public static final DamageSource LIGHTNING_BOLT;
    public static final DamageSource ON_FIRE;
    public static final DamageSource LAVA;
    public static final DamageSource HOT_FLOOR;
    public static final DamageSource IN_WALL;
    public static final DamageSource CRAMMING;
    public static final DamageSource DROWN;
    public static final DamageSource STARVE;
    public static final DamageSource CACTUS;
    public static final DamageSource FALL;
    public static final DamageSource FLY_INTO_WALL;
    public static final DamageSource OUT_OF_WORLD;
    public static final DamageSource GENERIC;
    public static final DamageSource MAGIC;
    public static final DamageSource WITHER;
    public static final DamageSource ANVIL;
    public static final DamageSource FALLING_BLOCK;
    public static final DamageSource DRAGON_BREATH;
    public static final DamageSource FIREWORKS;
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
    
    public static DamageSource causeMobDamage(final EntityLivingBase mob) {
        return new EntityDamageSource("mob", mob);
    }
    
    public static DamageSource causeIndirectDamage(final Entity source, final EntityLivingBase indirectEntityIn) {
        return new EntityDamageSourceIndirect("mob", source, indirectEntityIn);
    }
    
    public static DamageSource causePlayerDamage(final EntityPlayer player) {
        return new EntityDamageSource("player", player);
    }
    
    public static DamageSource causeArrowDamage(final EntityArrow arrow, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn).setProjectile();
    }
    
    public static DamageSource causeFireballDamage(final EntityFireball fireball, @Nullable final Entity indirectEntityIn) {
        return (indirectEntityIn == null) ? new EntityDamageSourceIndirect("onFire", fireball, fireball).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", fireball, indirectEntityIn).setFireDamage().setProjectile();
    }
    
    public static DamageSource causeThrownDamage(final Entity source, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("thrown", source, indirectEntityIn).setProjectile();
    }
    
    public static DamageSource causeIndirectMagicDamage(final Entity source, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn).setDamageBypassesArmor().setMagicDamage();
    }
    
    public static DamageSource causeThornsDamage(final Entity source) {
        return new EntityDamageSource("thorns", source).setIsThornsDamage().setMagicDamage();
    }
    
    public static DamageSource causeExplosionDamage(@Nullable final Explosion explosionIn) {
        return (explosionIn != null && explosionIn.getExplosivePlacedBy() != null) ? new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }
    
    public static DamageSource causeExplosionDamage(@Nullable final EntityLivingBase entityLivingBaseIn) {
        return (entityLivingBaseIn != null) ? new EntityDamageSource("explosion.player", entityLivingBaseIn).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
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
    
    protected DamageSource(final String damageTypeIn) {
        this.hungerDamage = 0.1f;
        this.damageType = damageTypeIn;
    }
    
    @Nullable
    public Entity getImmediateSource() {
        return this.getTrueSource();
    }
    
    @Nullable
    public Entity getTrueSource() {
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
    
    public ITextComponent getDeathMessage(final EntityLivingBase entityLivingBaseIn) {
        final EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        final String s = "death.attack." + this.damageType;
        final String s2 = s + ".player";
        return (entitylivingbase != null && I18n.canTranslate(s2)) ? new TextComponentTranslation(s2, new Object[] { entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName() }) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName() });
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
    
    public boolean isCreativePlayer() {
        final Entity entity = this.getTrueSource();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }
    
    @Nullable
    public Vec3d getDamageLocation() {
        return null;
    }
    
    static {
        IN_FIRE = new DamageSource("inFire").setFireDamage();
        LIGHTNING_BOLT = new DamageSource("lightningBolt");
        ON_FIRE = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
        LAVA = new DamageSource("lava").setFireDamage();
        HOT_FLOOR = new DamageSource("hotFloor").setFireDamage();
        IN_WALL = new DamageSource("inWall").setDamageBypassesArmor();
        CRAMMING = new DamageSource("cramming").setDamageBypassesArmor();
        DROWN = new DamageSource("drown").setDamageBypassesArmor();
        STARVE = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
        CACTUS = new DamageSource("cactus");
        FALL = new DamageSource("fall").setDamageBypassesArmor();
        FLY_INTO_WALL = new DamageSource("flyIntoWall").setDamageBypassesArmor();
        OUT_OF_WORLD = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        GENERIC = new DamageSource("generic").setDamageBypassesArmor();
        MAGIC = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
        WITHER = new DamageSource("wither").setDamageBypassesArmor();
        ANVIL = new DamageSource("anvil");
        FALLING_BLOCK = new DamageSource("fallingBlock");
        DRAGON_BREATH = new DamageSource("dragonBreath").setDamageBypassesArmor();
        FIREWORKS = new DamageSource("fireworks").setExplosion();
    }
}
