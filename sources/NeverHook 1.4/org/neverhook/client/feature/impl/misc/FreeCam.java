/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ 
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.world.World;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*     */ import org.neverhook.client.event.events.impl.packet.EventSendPacket;
/*     */ import org.neverhook.client.event.events.impl.player.EventFullCube;
/*     */ import org.neverhook.client.event.events.impl.player.EventPush;
/*     */ import org.neverhook.client.event.events.impl.player.EventUpdateLiving;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ 
/*     */ public class FreeCam
/*     */   extends Feature
/*     */ {
/*     */   public NumberSetting speed;
/*  24 */   public BooleanSetting AntiAction = new BooleanSetting("AntiAction", false, () -> Boolean.valueOf(true));
/*  25 */   public BooleanSetting autoDamageDisable = new BooleanSetting("Auto Damage Disable", false, () -> Boolean.valueOf(true));
/*     */   private float old;
/*     */   private double oldX;
/*     */   private double oldY;
/*     */   private double oldZ;
/*     */   
/*     */   public FreeCam() {
/*  32 */     super("FreeCam", "Позволяет летать в свободной камере", Type.Misc);
/*  33 */     this.speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1.0F, 0.1F, () -> Boolean.valueOf(true));
/*  34 */     addSettings(new Setting[] { (Setting)this.speed, (Setting)this.autoDamageDisable, (Setting)this.AntiAction });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  39 */     mc.player.capabilities.isFlying = false;
/*  40 */     mc.player.capabilities.setFlySpeed(this.old);
/*  41 */     mc.player.noClip = false;
/*  42 */     mc.renderGlobal.loadRenderers();
/*  43 */     mc.player.noClip = false;
/*  44 */     mc.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
/*  45 */     mc.world.removeEntityFromWorld(-69);
/*  46 */     mc.player.motionZ = 0.0D;
/*  47 */     mc.player.motionX = 0.0D;
/*  48 */     super.onDisable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  53 */     this.oldX = mc.player.posX;
/*  54 */     this.oldY = mc.player.posY;
/*  55 */     this.oldZ = mc.player.posZ;
/*  56 */     mc.player.noClip = true;
/*  57 */     EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)mc.world, mc.player.getGameProfile());
/*  58 */     fakePlayer.copyLocationAndAnglesFrom((Entity)mc.player);
/*  59 */     fakePlayer.posY -= 0.0D;
/*  60 */     fakePlayer.rotationYawHead = mc.player.rotationYawHead;
/*  61 */     mc.world.addEntityToWorld(-69, (Entity)fakePlayer);
/*  62 */     super.onEnable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onFullCube(EventFullCube event) {
/*  67 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPush(EventPush event) {
/*  72 */     event.setCancelled(true);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdateLiving event) {
/*  78 */     if (this.autoDamageDisable.getBoolValue() && mc.player.hurtTime > 0 && NeverHook.instance.featureManager.getFeatureByClass(FreeCam.class).getState()) {
/*  79 */       NeverHook.instance.featureManager.getFeatureByClass(FreeCam.class).state();
/*     */     }
/*  81 */     mc.player.noClip = true;
/*  82 */     mc.player.onGround = false;
/*  83 */     mc.player.capabilities.setFlySpeed(this.speed.getNumberValue() / 5.0F);
/*  84 */     mc.player.capabilities.isFlying = true;
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPacket(EventSendPacket event) {
/*  89 */     mc.player.setSprinting(false);
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onReceivePacket(EventReceivePacket event) {
/*  94 */     if (this.AntiAction.getBoolValue() && 
/*  95 */       event.getPacket() instanceof net.minecraft.network.play.server.SPacketPlayerPosLook) {
/*  96 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventTarget
/*     */   public void onSendPacket(EventSendPacket event) {
/* 103 */     if (this.AntiAction.getBoolValue()) {
/* 104 */       if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer) {
/* 105 */         event.setCancelled(true);
/*     */       }
/* 107 */       if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.Position) {
/* 108 */         event.setCancelled(true);
/*     */       }
/* 110 */       if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer.PositionRotation) {
/* 111 */         event.setCancelled(true);
/*     */       }
/* 113 */       if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketEntityAction)
/* 114 */         event.setCancelled(true); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\FreeCam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */