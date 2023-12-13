/*     */ package org.neverhook.client.feature.impl.misc;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.math.RotationHelper;
/*     */ import org.neverhook.client.helpers.misc.ChatHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class FakeHack extends Feature {
/*  20 */   public static ArrayList<String> fakeHackers = new ArrayList<>();
/*     */   private final BooleanSetting hackerSneak;
/*     */   private final BooleanSetting hackerSpin;
/*     */   private final NumberSetting hackerAttackDistance;
/*  24 */   public float rot = 0.0F;
/*     */   
/*     */   public FakeHack() {
/*  27 */     super("FakeHack", "Позволяет сделать легитного игрока читером", Type.Misc);
/*  28 */     this.hackerAttackDistance = new NumberSetting("Hacker Attack Range", 3.0F, 1.0F, 7.0F, 1.0F, () -> Boolean.valueOf(true));
/*  29 */     this.hackerSneak = new BooleanSetting("Hacker Sneaking", false, () -> Boolean.valueOf(true));
/*  30 */     this.hackerSpin = new BooleanSetting("Hacker Spin", false, () -> Boolean.valueOf(true));
/*  31 */     addSettings(new Setting[] { (Setting)this.hackerAttackDistance, (Setting)this.hackerSneak, (Setting)this.hackerSpin });
/*     */   }
/*     */   
/*     */   public static boolean isFakeHacker(EntityPlayer player) {
/*  35 */     for (String name : fakeHackers) {
/*  36 */       EntityPlayer en = mc.world.getPlayerEntityByName(name);
/*  37 */       if (en == null) {
/*  38 */         return false;
/*     */       }
/*  40 */       if (player.isEntityEqual((Entity)en)) {
/*  41 */         return true;
/*     */       }
/*     */     } 
/*  44 */     return false;
/*     */   }
/*     */   
/*     */   public static void removeHacker(EntityPlayer entityPlayer) {
/*  48 */     Iterator<String> hackers = fakeHackers.iterator();
/*  49 */     while (hackers.hasNext()) {
/*  50 */       String name = hackers.next();
/*  51 */       if (mc.world.getPlayerEntityByName(name) == null) {
/*     */         continue;
/*     */       }
/*  54 */       if (entityPlayer.isEntityEqual((Entity)mc.world.getPlayerEntityByName(name))) {
/*  55 */         mc.world.getPlayerEntityByName(name).setSneaking(false);
/*  56 */         hackers.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  63 */     for (String name : fakeHackers) {
/*  64 */       if (this.hackerSneak.getBoolValue()) {
/*  65 */         EntityPlayer player = mc.world.getPlayerEntityByName(name);
/*  66 */         assert player != null;
/*  67 */         player.setSneaking(false);
/*  68 */         player.setSprinting(false);
/*     */       } 
/*     */     } 
/*  71 */     super.onDisable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  76 */     ChatHelper.addChatMessage("To use this function write - .fakehack add (nick)");
/*  77 */     fakeHackers.clear();
/*  78 */     super.onEnable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreUpdate(EventPreMotion event) {
/*  83 */     for (String name : fakeHackers) {
/*  84 */       EntityPlayer player = mc.world.getPlayerEntityByName(name);
/*  85 */       if (player == null) {
/*     */         return;
/*     */       }
/*  88 */       if (this.hackerSneak.getBoolValue()) {
/*  89 */         player.setSneaking(true);
/*  90 */         player.setSprinting(true);
/*     */       } else {
/*  92 */         player.setSneaking(false);
/*  93 */         player.setSprinting(false);
/*     */       } 
/*  95 */       float[] rots = RotationHelper.getFacePosRemote((EntityLivingBase)player, (Entity)mc.player, true);
/*  96 */       float hackerReach = this.hackerAttackDistance.getNumberValue();
/*  97 */       if (!this.hackerSpin.getBoolValue()) {
/*  98 */         if (player.getDistanceToEntity((Entity)mc.player) <= hackerReach) {
/*  99 */           player.rotationYaw = rots[0];
/* 100 */           player.rotationYawHead = rots[0];
/* 101 */           player.rotationPitch = rots[1];
/*     */         } 
/*     */       } else {
/* 104 */         float speed = 30.0F;
/* 105 */         float yaw = (float)Math.floor(spinAim(speed));
/* 106 */         player.rotationYaw = yaw;
/* 107 */         player.rotationYawHead = yaw;
/*     */       } 
/* 109 */       if (mc.player.ticksExisted % 4 == 0 && player.getDistanceToEntity((Entity)mc.player) <= hackerReach) {
/* 110 */         player.swingArm(EnumHand.MAIN_HAND);
/* 111 */         if (mc.player.getDistanceToEntity((Entity)player) <= hackerReach) {
/* 112 */           mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0F, 1.0F);
/*     */         }
/*     */       } 
/* 115 */       if (mc.player.getDistanceToEntity((Entity)player) > hackerReach && !this.hackerSneak.getBoolValue() && !this.hackerSpin.getBoolValue()) {
/* 116 */         float yaw = 75.0F;
/* 117 */         player.rotationYaw = yaw;
/* 118 */         player.rotationPitch = 0.0F;
/* 119 */         player.rotationYawHead = yaw;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float spinAim(float rots) {
/* 125 */     this.rot += rots;
/* 126 */     return this.rot;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\FakeHack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */