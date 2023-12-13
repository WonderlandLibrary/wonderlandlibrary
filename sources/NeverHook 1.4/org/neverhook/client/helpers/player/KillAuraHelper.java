/*     */ package org.neverhook.client.helpers.player;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.feature.impl.combat.AntiBot;
/*     */ import org.neverhook.client.feature.impl.combat.KillAura;
/*     */ import org.neverhook.client.friend.Friend;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.world.EntityHelper;
/*     */ import org.neverhook.client.ui.notification.NotificationManager;
/*     */ import org.neverhook.client.ui.notification.NotificationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KillAuraHelper
/*     */   implements Helper
/*     */ {
/*     */   public static boolean canAttack(EntityLivingBase player) {
/*  33 */     for (Friend friend : NeverHook.instance.friendManager.getFriends()) {
/*  34 */       if (!player.getName().equals(friend.getName())) {
/*     */         continue;
/*     */       }
/*  37 */       return false;
/*     */     } 
/*  39 */     if (NeverHook.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player)) {
/*  40 */       return false;
/*     */     }
/*  42 */     if (KillAura.nakedPlayer.getBoolValue() && EntityHelper.checkArmor((Entity)player)) {
/*  43 */       return false;
/*     */     }
/*  45 */     if (player instanceof net.minecraft.entity.monster.EntitySlime && !KillAura.monsters.getBoolValue()) {
/*  46 */       return false;
/*     */     }
/*  48 */     if (player instanceof net.minecraft.entity.monster.EntityMagmaCube && !KillAura.monsters.getBoolValue()) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (player instanceof net.minecraft.entity.boss.EntityDragon && !KillAura.monsters.getBoolValue()) {
/*  52 */       return false;
/*     */     }
/*  54 */     if (player instanceof net.minecraft.entity.monster.EntityEnderman && !KillAura.monsters.getBoolValue()) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (player.isInvisible() && !KillAura.invis.getBoolValue()) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (player instanceof net.minecraft.entity.player.EntityPlayer || player instanceof net.minecraft.entity.passive.EntityAnimal || player instanceof net.minecraft.entity.monster.EntityMob || player instanceof net.minecraft.entity.passive.EntityVillager) {
/*  61 */       if (player instanceof net.minecraft.entity.player.EntityPlayer && !KillAura.players.getBoolValue()) {
/*  62 */         return false;
/*     */       }
/*  64 */       if (player instanceof net.minecraft.entity.passive.EntityAnimal && !KillAura.animals.getBoolValue()) {
/*  65 */         return false;
/*     */       }
/*  67 */       if (player instanceof net.minecraft.entity.monster.EntityMob && !KillAura.monsters.getBoolValue()) {
/*  68 */         return false;
/*     */       }
/*  70 */       if (player instanceof net.minecraft.entity.passive.EntityVillager && !KillAura.animals.getBoolValue()) {
/*  71 */         return false;
/*     */       }
/*  73 */       if (player instanceof net.minecraft.entity.passive.EntityOcelot && !KillAura.pets.getBoolValue()) {
/*  74 */         return false;
/*     */       }
/*  76 */       if (player instanceof net.minecraft.entity.passive.EntityWolf && !KillAura.pets.getBoolValue()) {
/*  77 */         return false;
/*     */       }
/*     */     } 
/*  80 */     if (!canSeeEntityAtFov((Entity)player, KillAura.fov.getNumberValue() * 2.0F)) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (!range(player, KillAura.range.getNumberValue())) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     if (!player.canEntityBeSeen((Entity)mc.player)) {
/*  88 */       return KillAura.walls.getBoolValue();
/*     */     }
/*  90 */     return (player != mc.player);
/*     */   }
/*     */   
/*     */   public static boolean canSeeEntityAtFov(Entity entityLiving, float scope) {
/*  94 */     double diffX = entityLiving.posX - mc.player.posX;
/*  95 */     double diffZ = entityLiving.posZ - mc.player.posZ;
/*  96 */     float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
/*  97 */     double difference = angleDifference(yaw, mc.player.rotationYaw);
/*  98 */     return (difference <= scope);
/*     */   }
/*     */   
/*     */   public static double angleDifference(float oldYaw, float newYaw) {
/* 102 */     float yaw = Math.abs(oldYaw - newYaw) % 360.0F;
/* 103 */     if (yaw > 180.0F) {
/* 104 */       yaw = 360.0F - yaw;
/*     */     }
/* 106 */     return yaw;
/*     */   }
/*     */   
/*     */   private static boolean range(EntityLivingBase entity, float range) {
/* 110 */     return (mc.player.getDistanceToEntity((Entity)entity) <= range);
/*     */   }
/*     */   
/*     */   public static void toggleOffChecks() {
/* 114 */     KillAura killAura = (KillAura)NeverHook.instance.featureManager.getFeatureByClass(KillAura.class);
/* 115 */     if ((mc.currentScreen instanceof net.minecraft.client.gui.GuiGameOver && !mc.player.isEntityAlive()) || (mc.player.ticksExisted <= 1 && KillAura.autoDisable.getBoolValue())) {
/* 116 */       killAura.state();
/* 117 */       NotificationManager.publicity(killAura.getLabel(), "KillAura was toggled off!", 2, NotificationType.SUCCESS);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static EntityLivingBase getSortEntities() {
/* 122 */     List<EntityLivingBase> entity = new ArrayList<>();
/* 123 */     for (Entity e : mc.world.loadedEntityList) {
/* 124 */       if (e instanceof EntityLivingBase) {
/* 125 */         EntityLivingBase player = (EntityLivingBase)e;
/* 126 */         if (mc.player.getDistanceToEntity((Entity)player) < KillAura.range.getNumberValue() && canAttack(player)) {
/* 127 */           if (player.getHealth() > 0.0F) {
/* 128 */             entity.add(player); continue;
/*     */           } 
/* 130 */           entity.remove(player);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 136 */     String sortMode = KillAura.sort.getOptions();
/* 137 */     if (sortMode.equalsIgnoreCase("Angle")) {
/* 138 */       entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity((Entity)o1) - mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity((Entity)o2) - mc.player.rotationYaw)));
/* 139 */     } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
/* 140 */       entity.sort(Comparator.<EntityLivingBase, Comparable>comparing(EntityLivingBase::getTotalArmorValue).reversed());
/* 141 */     } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
/* 142 */       entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
/* 143 */     } else if (sortMode.equalsIgnoreCase("Health")) {
/* 144 */       entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
/* 145 */     } else if (sortMode.equalsIgnoreCase("Distance")) {
/* 146 */       entity.sort(Comparator.comparingDouble(mc.player::getDistanceToEntity));
/* 147 */     } else if (sortMode.equalsIgnoreCase("HurtTime")) {
/* 148 */       entity.sort(Comparator.<EntityLivingBase, Comparable>comparing(EntityLivingBase::getHurtTime).reversed());
/* 149 */     } else if (sortMode.equalsIgnoreCase("Blocking Status") && 
/* 150 */       KillAura.target != null) {
/* 151 */       entity.sort(KillAura.target.isBlocking() ? Comparator.<EntityLivingBase, Comparable>comparing(EntityLivingBase::isBlocking) : Comparator.<EntityLivingBase>comparingDouble(mc.player::getDistanceToEntity));
/*     */     } 
/*     */ 
/*     */     
/* 155 */     if (entity.isEmpty()) {
/* 156 */       return null;
/*     */     }
/* 158 */     return entity.get(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\player\KillAuraHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */