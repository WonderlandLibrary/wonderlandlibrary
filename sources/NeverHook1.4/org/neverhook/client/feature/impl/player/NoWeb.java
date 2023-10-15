/*    */ package org.neverhook.client.feature.impl.player;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.motion.EventMove;
/*    */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.player.MovementHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.ListSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class NoWeb extends Feature {
/*    */   public ListSetting noWebMode;
/*    */   public NumberSetting webSpeed;
/*    */   public NumberSetting webJumpMotion;
/*    */   
/*    */   public NoWeb() {
/* 21 */     super("NoWeb", "Позволяет быстро ходить в паутине", Type.Player);
/* 22 */     this.noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> Boolean.valueOf(true), new String[] { "Matrix", "Matrix New", "NCP" });
/* 23 */     this.webSpeed = new NumberSetting("Web Speed", 0.8F, 0.1F, 2.0F, 0.1F, () -> Boolean.valueOf(this.noWebMode.currentMode.equals("Matrix New")));
/* 24 */     this.webJumpMotion = new NumberSetting("Jump Motion", 2.0F, 0.0F, 10.0F, 1.0F, () -> Boolean.valueOf(this.noWebMode.currentMode.equals("Matrix New")));
/* 25 */     addSettings(new Setting[] { (Setting)this.noWebMode, (Setting)this.webJumpMotion, (Setting)this.webSpeed });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPreMotion(EventPreMotion event) {
/* 30 */     String mode = this.noWebMode.getOptions();
/* 31 */     setSuffix(mode);
/* 32 */     if (mode.equalsIgnoreCase("Matrix New")) {
/* 33 */       BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.6D, mc.player.posZ);
/* 34 */       Block block = mc.world.getBlockState(blockPos).getBlock();
/* 35 */       if (mc.player.isInWeb) {
/* 36 */         mc.player.motionY += 2.0D;
/* 37 */       } else if (Block.getIdFromBlock(block) == 30) {
/* 38 */         if (this.webJumpMotion.getNumberValue() > 0.0F) {
/* 39 */           mc.player.motionY += this.webJumpMotion.getNumberValue();
/*    */         } else {
/* 41 */           mc.player.motionY = 0.0D;
/*    */         } 
/* 43 */         MovementHelper.setSpeed(this.webSpeed.getNumberValue());
/* 44 */         mc.gameSettings.keyBindJump.pressed = false;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMove(EventMove event) {
/* 51 */     String mode = this.noWebMode.getOptions();
/* 52 */     setSuffix(mode);
/* 53 */     if (getState())
/* 54 */       if (mode.equalsIgnoreCase("Matrix")) {
/* 55 */         if (mc.player.onGround && mc.player.isInWeb) {
/* 56 */           mc.player.isInWeb = true;
/*    */         } else {
/* 58 */           if (mc.gameSettings.keyBindJump.isKeyDown())
/*    */             return; 
/* 60 */           mc.player.isInWeb = false;
/*    */         } 
/* 62 */         if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
/* 63 */           MovementHelper.setEventSpeed(event, 0.483D);
/*    */         }
/* 65 */       } else if (mode.equalsIgnoreCase("NCP")) {
/* 66 */         if (mc.player.onGround && mc.player.isInWeb) {
/* 67 */           mc.player.isInWeb = true;
/*    */         } else {
/* 69 */           if (mc.gameSettings.keyBindJump.isKeyDown())
/*    */             return; 
/* 71 */           mc.player.isInWeb = false;
/*    */         } 
/* 73 */         if (mc.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown())
/* 74 */           MovementHelper.setEventSpeed(event, 0.403D); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\player\NoWeb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */