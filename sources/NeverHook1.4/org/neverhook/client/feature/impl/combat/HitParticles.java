/*    */ package org.neverhook.client.feature.impl.combat;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventAttackSilent;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class HitParticles
/*    */   extends Feature {
/*    */   private final NumberSetting particleMultiplier;
/*    */   private final ListSetting particleMode;
/* 19 */   private final Random random = new Random();
/*    */   
/*    */   public HitParticles() {
/* 22 */     super("DamageParticles", "При ударе спавнит партиклы вокруг сущности", Type.Combat);
/* 23 */     this.particleMode = new ListSetting("Particle Mode", "Spell", () -> Boolean.valueOf(true), new String[] { "Spell", "Enchant", "Criticals", "Heart", "Flame", "HappyVillager", "AngryVillager", "Portal", "Redstone", "Cloud" });
/* 24 */     this.particleMultiplier = new NumberSetting("Particle Multiplier", 5.0F, 1.0F, 15.0F, 1.0F, () -> Boolean.valueOf(true));
/* 25 */     addSettings(new Setting[] { (Setting)this.particleMode, (Setting)this.particleMultiplier });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onAttackSilent(EventAttackSilent event) {
/* 30 */     String mode = this.particleMode.getOptions();
/* 31 */     if (mode.equalsIgnoreCase("Redstone")) {
/* 32 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 33 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 34 */           mc.effectRenderer.spawnEffectParticle(37, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[] { Block.getIdFromBlock(Blocks.REDSTONE_BLOCK) });
/*    */         } 
/*    */       } 
/* 37 */     } else if (mode.equalsIgnoreCase("Heart")) {
/* 38 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 39 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 40 */           mc.world.spawnParticle(EnumParticleTypes.HEART, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 43 */     } else if (mode.equalsIgnoreCase("Flame")) {
/* 44 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 45 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 46 */           mc.world.spawnParticle(EnumParticleTypes.FLAME, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 49 */     } else if (mode.equalsIgnoreCase("Cloud")) {
/* 50 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 51 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 52 */           mc.world.spawnParticle(EnumParticleTypes.CLOUD, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 55 */     } else if (mode.equalsIgnoreCase("HappyVillager")) {
/* 56 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 57 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 58 */           mc.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 61 */     } else if (mode.equalsIgnoreCase("AngryVillager")) {
/* 62 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 63 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 64 */           mc.world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 67 */     } else if (mode.equalsIgnoreCase("Spell")) {
/* 68 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 69 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 70 */           mc.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 73 */     } else if (mode.equalsIgnoreCase("Portal")) {
/* 74 */       for (float i = 0.0F; i < (event.getTargetEntity()).height; i += 0.2F) {
/* 75 */         for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 76 */           mc.world.spawnParticle(EnumParticleTypes.PORTAL, (event.getTargetEntity()).posX, (event.getTargetEntity()).posY + i, (event.getTargetEntity()).posZ, ((this.random.nextInt(6) - 3) / 5.0F), 0.1D, ((this.random.nextInt(6) - 3) / 5.0F), new int[0]);
/*    */         }
/*    */       } 
/* 79 */     } else if (mode.equalsIgnoreCase("Enchant")) {
/* 80 */       for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++) {
/* 81 */         mc.player.onEnchantmentCritical(event.getTargetEntity());
/*    */       }
/* 83 */     } else if (mode.equalsIgnoreCase("Criticals")) {
/* 84 */       for (int i2 = 0; i2 < this.particleMultiplier.getNumberValue(); i2++)
/* 85 */         mc.player.onCriticalHit(event.getTargetEntity()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\HitParticles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */