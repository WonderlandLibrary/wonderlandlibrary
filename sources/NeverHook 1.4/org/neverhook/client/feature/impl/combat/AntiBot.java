/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.input.StringHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class AntiBot
/*     */   extends Feature {
/*  22 */   public static List<Entity> isBotPlayer = new ArrayList<>();
/*     */   
/*  24 */   public ListSetting antiBotMode = new ListSetting("Anti Bot Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "Custom", "Reflex" });
/*     */   
/*  26 */   public BooleanSetting checkPing = new BooleanSetting("Check Ping", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  27 */   public BooleanSetting checkEntityId = new BooleanSetting("Check Entity Id", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  28 */   public BooleanSetting checkDuplicate = new BooleanSetting("Check Duplicate", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  29 */   public BooleanSetting checkDuplicateTab = new BooleanSetting("Check Duplicate Tab", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  30 */   public BooleanSetting checkInvisible = new BooleanSetting("Check Invisible", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  31 */   public BooleanSetting checkNameBot = new BooleanSetting("Check Name", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  32 */   public BooleanSetting checkDistance = new BooleanSetting("Check Distance", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  33 */   public NumberSetting distance = new NumberSetting("Distance", 25.0F, 0.0F, 100.0F, 0.1F, () -> Boolean.valueOf((this.antiBotMode.currentMode.equals("Custom") && this.checkDistance.getBoolValue())));
/*  34 */   public BooleanSetting checkHurtTime = new BooleanSetting("Check HurtTime", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  35 */   public NumberSetting hurtTime = new NumberSetting("HurtTime", 0.0F, -10.0F, 10.0F, 0.1F, () -> Boolean.valueOf((this.antiBotMode.currentMode.equals("Custom") && this.checkHurtTime.getBoolValue())));
/*  36 */   public BooleanSetting checkOnGround = new BooleanSetting("Check onGround", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  37 */   public BooleanSetting checkTick = new BooleanSetting("Check Tick", false, () -> Boolean.valueOf(this.antiBotMode.currentMode.equals("Custom")));
/*  38 */   public NumberSetting ticks = new NumberSetting("Tick", 5.0F, 0.0F, 10000.0F, 1.0F, () -> Boolean.valueOf((this.antiBotMode.currentMode.equals("Custom") && this.checkTick.getBoolValue())));
/*     */   
/*     */   public AntiBot() {
/*  41 */     super("AntiBot", "Добавляет сущностей заспавненых античитом в блэк-лист", Type.Combat);
/*  42 */     addSettings(new Setting[] { (Setting)this.antiBotMode, (Setting)this.checkPing, (Setting)this.checkEntityId, (Setting)this.checkDuplicate, (Setting)this.checkDuplicateTab, (Setting)this.checkInvisible, (Setting)this.checkNameBot, (Setting)this.checkDistance, (Setting)this.distance, (Setting)this.checkHurtTime, (Setting)this.hurtTime, (Setting)this.checkOnGround, (Setting)this.checkTick, (Setting)this.ticks });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  47 */     for (Entity entity : mc.world.loadedEntityList) {
/*  48 */       switch (this.antiBotMode.currentMode) {
/*     */         case "Matrix":
/*  50 */           if (entity != mc.player && entity.ticksExisted < 5 && entity instanceof EntityOtherPlayerMP && 
/*  51 */             ((EntityOtherPlayerMP)entity).hurtTime > 0 && mc.player.getDistanceToEntity(entity) <= 25.0F && mc.player.connection.getPlayerInfo(entity.getUniqueID()).getResponseTime() != 0 && entity.onGround) {
/*  52 */             isBotPlayer.add(entity);
/*     */           }
/*     */ 
/*     */         
/*     */         case "Custom":
/*  57 */           if (isBot(entity)) {
/*  58 */             isBotPlayer.add(entity);
/*     */           }
/*     */         
/*     */         case "Reflex":
/*  62 */           if (entity.ticksExisted >= 5 || !entity.onGround || mc.player.getDistanceToEntity(entity) > 25.0F || entity.getName().contains(mc.player.getName())) {
/*     */             continue;
/*     */           }
/*     */           
/*  66 */           isBotPlayer.add(entity);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBot(Entity entity) {
/*  74 */     if (this.checkPing.getBoolValue() && 
/*  75 */       entity instanceof EntityPlayer) {
/*  76 */       UUID id = entity.getUniqueID();
/*  77 */       NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(id);
/*  78 */       if (info.getResponseTime() == 0) {
/*  79 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  84 */     if (this.checkDistance.getBoolValue() && 
/*  85 */       mc.player.getDistanceToEntity(entity) <= this.distance.getNumberValue()) {
/*  86 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  90 */     if (this.checkHurtTime.getBoolValue() && 
/*  91 */       ((EntityPlayer)entity).hurtTime >= this.hurtTime.getNumberValue()) {
/*  92 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  96 */     if (this.checkTick.getBoolValue() && 
/*  97 */       entity.ticksExisted >= this.ticks.getNumberValue()) {
/*  98 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (this.checkOnGround.getBoolValue()) {
/* 103 */       if (entity.onGround) {
/* 104 */         return true;
/*     */       }
/*     */     }
/* 107 */     else if (!entity.onGround) {
/* 108 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     if (this.checkEntityId.getBoolValue()) {
/* 113 */       int id = entity.getEntityId();
/* 114 */       if (id <= -1 || id >= 1000000000) {
/* 115 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 119 */     if (this.checkInvisible.getBoolValue() && 
/* 120 */       entity.isInvisible() && entity != mc.player) {
/* 121 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 125 */     if (this.checkDuplicate.getBoolValue() && 
/* 126 */       mc.world.loadedEntityList.stream().filter(e -> (e instanceof EntityPlayer && entity instanceof EntityPlayer && entity.getName().equals(StringHelper.format(e.getName())))).count() > 1L) {
/* 127 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (this.checkDuplicateTab.getBoolValue() && 
/* 132 */       mc.player.connection.getPlayerInfoMap().stream().filter(entityPlayer -> (entity instanceof EntityPlayer && entity.getName().equals(StringHelper.format(entityPlayer.getGameProfile().getName())))).count() > 1L) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     return (entity.getName().isEmpty() || entity.getName().equals(mc.player.getName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AntiBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */