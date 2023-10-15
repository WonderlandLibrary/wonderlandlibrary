/*     */ package org.neverhook.client.feature.impl.combat;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.potion.PotionUtils;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class AutoPotion extends Feature {
/*  22 */   public TimerHelper timerHelper = new TimerHelper();
/*     */   public NumberSetting delay;
/*  24 */   public BooleanSetting onlyGround = new BooleanSetting("Only Ground", true, () -> Boolean.valueOf(true));
/*     */   
/*     */   public AutoPotion() {
/*  27 */     super("AutoPotion", "Автоматически бросает Splash зелья находящиеся в инвентаре", Type.Combat);
/*  28 */     this.delay = new NumberSetting("Throw Delay", 300.0F, 10.0F, 300.0F, 10.0F, () -> Boolean.valueOf(true));
/*  29 */     addSettings(new Setting[] { (Setting)this.delay, (Setting)this.onlyGround });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  34 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  39 */     if (this.onlyGround.getBoolValue() && !mc.player.onGround) {
/*     */       return;
/*     */     }
/*     */     
/*  43 */     if (!mc.player.isPotionActive(Potion.getPotionById(1)) && isPotionOnHotBar(Potions.SPEED)) {
/*  44 */       event.setPitch(90.0F);
/*  45 */       mc.player.rotationPitchHead = 90.0F;
/*  46 */       if (event.getPitch() == 90.0F) {
/*  47 */         throwPot(Potions.SPEED);
/*     */       }
/*  49 */     } else if (!mc.player.isPotionActive(Potion.getPotionById(5)) && isPotionOnHotBar(Potions.STRENGTH)) {
/*  50 */       event.setPitch(90.0F);
/*  51 */       mc.player.rotationPitchHead = 90.0F;
/*  52 */       if (event.getPitch() == 90.0F) {
/*  53 */         throwPot(Potions.STRENGTH);
/*     */       }
/*  55 */     } else if (!mc.player.isPotionActive(Potion.getPotionById(12)) && isPotionOnHotBar(Potions.FIRERES)) {
/*  56 */       event.setPitch(90.0F);
/*  57 */       mc.player.rotationPitchHead = 90.0F;
/*  58 */       if (event.getPitch() == 90.0F) {
/*  59 */         throwPot(Potions.FIRERES);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void throwPot(Potions potion) {
/*  65 */     int slot = getPotionSlot(potion);
/*  66 */     if (this.timerHelper.hasReached(this.delay.getNumberValue())) {
/*  67 */       mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
/*  68 */       mc.playerController.updateController();
/*  69 */       mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
/*  70 */       mc.playerController.updateController();
/*  71 */       mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(mc.player.inventory.currentItem));
/*  72 */       this.timerHelper.reset();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getPotionSlot(Potions potion) {
/*  77 */     for (int i = 0; i < 9; i++) {
/*  78 */       if (isStackPotion(mc.player.inventory.getStackInSlot(i), potion))
/*  79 */         return i; 
/*     */     } 
/*  81 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean isPotionOnHotBar(Potions potions) {
/*  85 */     for (int i = 0; i < 9; i++) {
/*  86 */       if (isStackPotion(mc.player.inventory.getStackInSlot(i), potions)) {
/*  87 */         return true;
/*     */       }
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isStackPotion(ItemStack stack, Potions potion) {
/*  94 */     if (stack == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     Item item = stack.getItem();
/*     */     
/*  99 */     if (item == Items.SPLASH_POTION) {
/* 100 */       int id = 5;
/*     */       
/* 102 */       switch (potion) {
/*     */         case STRENGTH:
/* 104 */           id = 5;
/*     */           break;
/*     */         
/*     */         case SPEED:
/* 108 */           id = 1;
/*     */           break;
/*     */         
/*     */         case FIRERES:
/* 112 */           id = 12;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 117 */       for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
/* 118 */         if (effect.getPotion() == Potion.getPotionById(id)) {
/* 119 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   public enum Potions {
/* 127 */     STRENGTH, SPEED, FIRERES;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\combat\AutoPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */