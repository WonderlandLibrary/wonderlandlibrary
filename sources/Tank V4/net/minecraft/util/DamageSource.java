package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

public class DamageSource {
   public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
   private boolean damageIsAbsolute;
   public static DamageSource cactus = new DamageSource("cactus");
   private boolean fireDamage;
   public static DamageSource anvil = new DamageSource("anvil");
   private boolean difficultyScaled;
   public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
   private boolean magicDamage;
   public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
   public String damageType;
   public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
   public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
   public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
   public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
   public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
   private boolean projectile;
   private float hungerDamage = 0.3F;
   private boolean isDamageAllowedInCreativeMode;
   public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
   public static DamageSource fallingBlock = new DamageSource("fallingBlock");
   private boolean isUnblockable;
   private boolean explosion;
   public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
   public static DamageSource lightningBolt = new DamageSource("lightningBolt");
   public static DamageSource lava = (new DamageSource("lava")).setFireDamage();

   public DamageSource setMagicDamage() {
      this.magicDamage = true;
      return this;
   }

   public boolean isExplosion() {
      return this.explosion;
   }

   public boolean isDifficultyScaled() {
      return this.difficultyScaled;
   }

   public IChatComponent getDeathMessage(EntityLivingBase var1) {
      EntityLivingBase var2 = var1.func_94060_bK();
      String var3 = "death.attack." + this.damageType;
      String var4 = var3 + ".player";
      return var2 != null && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(var4, new Object[]{var1.getDisplayName(), var2.getDisplayName()}) : new ChatComponentTranslation(var3, new Object[]{var1.getDisplayName()});
   }

   public boolean isDamageAbsolute() {
      return this.damageIsAbsolute;
   }

   public boolean canHarmInCreative() {
      return this.isDamageAllowedInCreativeMode;
   }

   public boolean isCreativePlayer() {
      Entity var1 = this.getEntity();
      return var1 instanceof EntityPlayer && ((EntityPlayer)var1).capabilities.isCreativeMode;
   }

   public Entity getSourceOfDamage() {
      return this.getEntity();
   }

   public static DamageSource causePlayerDamage(EntityPlayer var0) {
      return new EntityDamageSource("player", var0);
   }

   protected DamageSource setDamageIsAbsolute() {
      this.damageIsAbsolute = true;
      this.hungerDamage = 0.0F;
      return this;
   }

   public String getDamageType() {
      return this.damageType;
   }

   public static DamageSource causeArrowDamage(EntityArrow var0, Entity var1) {
      return (new EntityDamageSourceIndirect("arrow", var0, var1)).setProjectile();
   }

   protected DamageSource setDamageAllowedInCreativeMode() {
      this.isDamageAllowedInCreativeMode = true;
      return this;
   }

   public static DamageSource causeFireballDamage(EntityFireball var0, Entity var1) {
      return var1 == null ? (new EntityDamageSourceIndirect("onFire", var0, var0)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", var0, var1)).setFireDamage().setProjectile();
   }

   public static DamageSource setExplosionSource(Explosion var0) {
      return var0 != null && var0.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", var0.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
   }

   public DamageSource setExplosion() {
      this.explosion = true;
      return this;
   }

   public boolean isMagicDamage() {
      return this.magicDamage;
   }

   public float getHungerDamage() {
      return this.hungerDamage;
   }

   public static DamageSource causeThrownDamage(Entity var0, Entity var1) {
      return (new EntityDamageSourceIndirect("thrown", var0, var1)).setProjectile();
   }

   public boolean isUnblockable() {
      return this.isUnblockable;
   }

   public boolean isFireDamage() {
      return this.fireDamage;
   }

   public boolean isProjectile() {
      return this.projectile;
   }

   public DamageSource setDifficultyScaled() {
      this.difficultyScaled = true;
      return this;
   }

   public static DamageSource causeMobDamage(EntityLivingBase var0) {
      return new EntityDamageSource("mob", var0);
   }

   public static DamageSource causeThornsDamage(Entity var0) {
      return (new EntityDamageSource("thorns", var0)).setIsThornsDamage().setMagicDamage();
   }

   public DamageSource setProjectile() {
      this.projectile = true;
      return this;
   }

   protected DamageSource setFireDamage() {
      this.fireDamage = true;
      return this;
   }

   protected DamageSource setDamageBypassesArmor() {
      this.isUnblockable = true;
      this.hungerDamage = 0.0F;
      return this;
   }

   public static DamageSource causeIndirectMagicDamage(Entity var0, Entity var1) {
      return (new EntityDamageSourceIndirect("indirectMagic", var0, var1)).setDamageBypassesArmor().setMagicDamage();
   }

   protected DamageSource(String var1) {
      this.damageType = var1;
   }

   public Entity getEntity() {
      return null;
   }
}
