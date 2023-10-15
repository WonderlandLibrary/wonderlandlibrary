/*    */ package org.neverhook.client.feature.impl.ghost;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.friend.Friend;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.helpers.world.EntityHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TriggerBot
/*    */   extends Feature
/*    */ {
/*    */   public static NumberSetting range;
/*    */   public static BooleanSetting players;
/*    */   public static BooleanSetting mobs;
/*    */   public static BooleanSetting team;
/* 28 */   public static BooleanSetting onlyCrit = new BooleanSetting("Only Crits", false, () -> Boolean.valueOf(true));
/* 29 */   public static NumberSetting critFallDist = new NumberSetting("Fall Distance", 0.2F, 0.08F, 1.0F, 0.01F, () -> Boolean.valueOf(onlyCrit.getBoolValue()));
/*    */   
/*    */   public TriggerBot() {
/* 32 */     super("TriggerBot", "Автоматически наносит удар при наводке на сущность", Type.Ghost);
/* 33 */     players = new BooleanSetting("Players", true, () -> Boolean.valueOf(true));
/* 34 */     mobs = new BooleanSetting("Mobs", false, () -> Boolean.valueOf(true));
/* 35 */     team = new BooleanSetting("Team Check", false, () -> Boolean.valueOf(true));
/* 36 */     range = new NumberSetting("Trigger Range", 4.0F, 1.0F, 6.0F, 0.1F, () -> Boolean.valueOf(true));
/* 37 */     addSettings(new Setting[] { (Setting)range, (Setting)players, (Setting)mobs, (Setting)team, (Setting)onlyCrit, (Setting)critFallDist });
/*    */   }
/*    */   
/*    */   public static boolean canTrigger(EntityLivingBase player) {
/* 41 */     for (Friend friend : NeverHook.instance.friendManager.getFriends()) {
/* 42 */       if (!player.getName().equals(friend.getName())) {
/*    */         continue;
/*    */       }
/* 45 */       return false;
/*    */     } 
/*    */     
/* 48 */     if (team.getBoolValue() && EntityHelper.isTeamWithYou(player)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     if (player instanceof EntityPlayer || player instanceof net.minecraft.entity.passive.EntityAnimal || player instanceof net.minecraft.entity.monster.EntityMob || player instanceof net.minecraft.entity.passive.EntityVillager) {
/* 53 */       if (player instanceof EntityPlayer && !players.getBoolValue()) {
/* 54 */         return false;
/*    */       }
/* 56 */       if (player instanceof net.minecraft.entity.passive.EntityAnimal && !mobs.getBoolValue()) {
/* 57 */         return false;
/*    */       }
/* 59 */       if (player instanceof net.minecraft.entity.monster.EntityMob && !mobs.getBoolValue()) {
/* 60 */         return false;
/*    */       }
/* 62 */       if (player instanceof net.minecraft.entity.passive.EntityVillager && !mobs.getBoolValue()) {
/* 63 */         return false;
/*    */       }
/*    */     } 
/* 66 */     return (player != mc.player);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 71 */     Entity entity = mc.objectMouseOver.entityHit;
/* 72 */     if (entity == null || mc.player.getDistanceToEntity(entity) > range.getNumberValue() || entity instanceof net.minecraft.entity.item.EntityEnderCrystal || entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0F) {
/*    */       return;
/*    */     }
/*    */     
/* 76 */     if (MovementHelper.isBlockAboveHead()) {
/* 77 */       if (mc.player.fallDistance < critFallDist.getNumberValue() && mc.player.getCooledAttackStrength(0.8F) == 1.0F && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
/*    */         return;
/*    */       }
/*    */     }
/* 81 */     else if (mc.player.fallDistance != 0.0F && onlyCrit.getBoolValue() && !mc.player.isOnLadder() && !mc.player.isInLiquid() && !mc.player.isInWeb && mc.player.getRidingEntity() == null) {
/*    */       return;
/*    */     } 
/*    */ 
/*    */     
/* 86 */     if (canTrigger((EntityLivingBase)entity) && 
/* 87 */       mc.player.getCooledAttackStrength(0.0F) == 1.0F) {
/* 88 */       mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
/* 89 */       mc.player.swingArm(EnumHand.MAIN_HAND);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\ghost\TriggerBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */