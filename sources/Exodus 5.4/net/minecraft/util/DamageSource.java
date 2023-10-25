/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;

public class DamageSource {
    private boolean isDamageAllowedInCreativeMode;
    public static DamageSource fall;
    public static DamageSource outOfWorld;
    public static DamageSource inFire;
    public static DamageSource cactus;
    public static DamageSource inWall;
    public static DamageSource fallingBlock;
    private boolean magicDamage;
    public static DamageSource generic;
    private boolean projectile;
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.3f;
    private boolean difficultyScaled;
    public static DamageSource magic;
    public static DamageSource lightningBolt;
    public static DamageSource wither;
    public String damageType;
    public static DamageSource drown;
    public static DamageSource anvil;
    public static DamageSource onFire;
    private boolean fireDamage;
    public static DamageSource starve;
    private boolean isUnblockable;
    private boolean explosion;
    public static DamageSource lava;

    public static DamageSource causePlayerDamage(EntityPlayer entityPlayer) {
        return new EntityDamageSource("player", entityPlayer);
    }

    public boolean isExplosion() {
        return this.explosion;
    }

    protected DamageSource setDamageIsAbsolute() {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0f;
        return this;
    }

    public boolean isProjectile() {
        return this.projectile;
    }

    public static DamageSource setExplosionSource(Explosion explosion) {
        return explosion != null && explosion.getExplosivePlacedBy() != null ? new EntityDamageSource("explosion.player", explosion.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }

    static {
        inFire = new DamageSource("inFire").setFireDamage();
        lightningBolt = new DamageSource("lightningBolt");
        onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
        lava = new DamageSource("lava").setFireDamage();
        inWall = new DamageSource("inWall").setDamageBypassesArmor();
        drown = new DamageSource("drown").setDamageBypassesArmor();
        starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
        cactus = new DamageSource("cactus");
        fall = new DamageSource("fall").setDamageBypassesArmor();
        outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        generic = new DamageSource("generic").setDamageBypassesArmor();
        magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
        wither = new DamageSource("wither").setDamageBypassesArmor();
        anvil = new DamageSource("anvil");
        fallingBlock = new DamageSource("fallingBlock");
    }

    protected DamageSource setDamageBypassesArmor() {
        this.isUnblockable = true;
        this.hungerDamage = 0.0f;
        return this;
    }

    public boolean isCreativePlayer() {
        Entity entity = this.getEntity();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }

    protected DamageSource setDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }

    protected DamageSource setFireDamage() {
        this.fireDamage = true;
        return this;
    }

    public DamageSource setProjectile() {
        this.projectile = true;
        return this;
    }

    public boolean isDamageAbsolute() {
        return this.damageIsAbsolute;
    }

    public static DamageSource causeArrowDamage(EntityArrow entityArrow, Entity entity) {
        return new EntityDamageSourceIndirect("arrow", entityArrow, entity).setProjectile();
    }

    public static DamageSource causeMobDamage(EntityLivingBase entityLivingBase) {
        return new EntityDamageSource("mob", entityLivingBase);
    }

    public DamageSource setExplosion() {
        this.explosion = true;
        return this;
    }

    public static DamageSource causeFireballDamage(EntityFireball entityFireball, Entity entity) {
        return entity == null ? new EntityDamageSourceIndirect("onFire", entityFireball, entityFireball).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", entityFireball, entity).setFireDamage().setProjectile();
    }

    public DamageSource setDifficultyScaled() {
        this.difficultyScaled = true;
        return this;
    }

    public boolean isMagicDamage() {
        return this.magicDamage;
    }

    public String getDamageType() {
        return this.damageType;
    }

    public static DamageSource causeIndirectMagicDamage(Entity entity, Entity entity2) {
        return new EntityDamageSourceIndirect("indirectMagic", entity, entity2).setDamageBypassesArmor().setMagicDamage();
    }

    public DamageSource setMagicDamage() {
        this.magicDamage = true;
        return this;
    }

    public Entity getSourceOfDamage() {
        return this.getEntity();
    }

    public static DamageSource causeThrownDamage(Entity entity, Entity entity2) {
        return new EntityDamageSourceIndirect("thrown", entity, entity2).setProjectile();
    }

    public boolean isUnblockable() {
        return this.isUnblockable;
    }

    public boolean isFireDamage() {
        return this.fireDamage;
    }

    public static DamageSource causeThornsDamage(Entity entity) {
        return new EntityDamageSource("thorns", entity).setIsThornsDamage().setMagicDamage();
    }

    protected DamageSource(String string) {
        this.damageType = string;
    }

    public boolean canHarmInCreative() {
        return this.isDamageAllowedInCreativeMode;
    }

    public IChatComponent getDeathMessage(EntityLivingBase entityLivingBase) {
        EntityLivingBase entityLivingBase2 = entityLivingBase.func_94060_bK();
        String string = "death.attack." + this.damageType;
        String string2 = String.valueOf(string) + ".player";
        return entityLivingBase2 != null && StatCollector.canTranslate(string2) ? new ChatComponentTranslation(string2, entityLivingBase.getDisplayName(), entityLivingBase2.getDisplayName()) : new ChatComponentTranslation(string, entityLivingBase.getDisplayName());
    }

    public Entity getEntity() {
        return null;
    }

    public float getHungerDamage() {
        return this.hungerDamage;
    }

    public boolean isDifficultyScaled() {
        return this.difficultyScaled;
    }
}

