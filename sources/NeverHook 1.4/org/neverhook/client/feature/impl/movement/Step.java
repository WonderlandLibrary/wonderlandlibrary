/*     */ package org.neverhook.client.feature.impl.movement;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketPlayer;
/*     */ import org.neverhook.client.NeverHook;
/*     */ import org.neverhook.client.event.EventTarget;
/*     */ import org.neverhook.client.event.events.impl.motion.EventStep;
/*     */ import org.neverhook.client.event.events.impl.player.EventPreMotion;
/*     */ import org.neverhook.client.feature.Feature;
/*     */ import org.neverhook.client.feature.impl.Type;
/*     */ import org.neverhook.client.helpers.misc.TimerHelper;
/*     */ import org.neverhook.client.settings.Setting;
/*     */ import org.neverhook.client.settings.impl.BooleanSetting;
/*     */ import org.neverhook.client.settings.impl.ListSetting;
/*     */ import org.neverhook.client.settings.impl.NumberSetting;
/*     */ 
/*     */ public class Step extends Feature {
/*  17 */   public static TimerHelper time = new TimerHelper();
/*     */   public static NumberSetting delay;
/*     */   public static NumberSetting heightStep;
/*     */   public static ListSetting stepMode;
/*     */   public BooleanSetting reverseStep;
/*     */   public boolean jump;
/*     */   boolean resetTimer;
/*     */   
/*     */   public Step() {
/*  26 */     super("Step", "Автоматически взбирается на блоки", Type.Movement);
/*  27 */     stepMode = new ListSetting("Step Mode", "Motion", () -> Boolean.valueOf(true), new String[] { "Motion", "Vanilla" });
/*  28 */     delay = new NumberSetting("Step Delay", 0.0F, 0.0F, 1.0F, 0.1F, () -> Boolean.valueOf(true));
/*  29 */     heightStep = new NumberSetting("Height", 1.0F, 1.0F, 10.0F, 0.5F, () -> Boolean.valueOf(true));
/*  30 */     this.reverseStep = new BooleanSetting("Reverse Step", false, () -> Boolean.valueOf(true));
/*  31 */     addSettings(new Setting[] { (Setting)stepMode, (Setting)heightStep, (Setting)delay, (Setting)this.reverseStep });
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onStep(EventStep step) {
/*  36 */     String mode = stepMode.getOptions();
/*  37 */     float delayValue = delay.getNumberValue() * 1000.0F;
/*  38 */     float stepValue = heightStep.getNumberValue();
/*  39 */     if (NeverHook.instance.featureManager.getFeatureByClass(NoClip.class).getState()) {
/*     */       return;
/*     */     }
/*  42 */     double height = (mc.player.getEntityBoundingBox()).minY - mc.player.posY;
/*  43 */     boolean canStep = (height >= 0.625D);
/*  44 */     if (canStep) {
/*  45 */       time.reset();
/*     */     }
/*  47 */     if (this.resetTimer) {
/*  48 */       this.resetTimer = false;
/*  49 */       mc.timer.timerSpeed = 1.0F;
/*     */     } 
/*  51 */     if (mode.equalsIgnoreCase("Motion")) {
/*  52 */       if (mc.player.isCollidedVertically && !mc.gameSettings.keyBindJump.isPressed() && time.hasReached(delayValue)) {
/*  53 */         step.setStepHeight(stepValue);
/*     */       }
/*  55 */       if (canStep) {
/*  56 */         mc.timer.timerSpeed = (height > 1.0D) ? 0.12F : 0.4F;
/*  57 */         this.resetTimer = true;
/*  58 */         ncpStep(height);
/*     */       } 
/*  60 */     } else if (mode.equalsIgnoreCase("Vanilla")) {
/*  61 */       mc.player.stepHeight = heightStep.getNumberValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ncpStep(double height) {
/*  66 */     double[] offset = { 0.42D, 0.333D, 0.248D, 0.083D, -0.078D };
/*  67 */     double posX = mc.player.posX;
/*  68 */     double posZ = mc.player.posZ;
/*  69 */     double y = mc.player.posY;
/*  70 */     if (height < 1.1D) {
/*  71 */       double first = 0.42D;
/*  72 */       double second = 0.75D;
/*  73 */       mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + first, posZ, false));
/*  74 */       if (y + second < y + height)
/*  75 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + second, posZ, true)); 
/*  76 */     } else if (height < 1.6D) {
/*  77 */       for (double off : offset) {
/*  78 */         y += off;
/*  79 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y, posZ, true));
/*     */       } 
/*  81 */     } else if (height < 2.1D) {
/*  82 */       double[] heights = { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D };
/*  83 */       for (double off : heights) {
/*  84 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + off, posZ, true));
/*     */       }
/*     */     } else {
/*  87 */       double[] heights = { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
/*  88 */       for (double off : heights) {
/*  89 */         mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + off, posZ, true));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPreMotion(EventPreMotion event) {
/*  96 */     String mode = stepMode.getOptions();
/*  97 */     setSuffix(mode);
/*  98 */     if (mc.player.motionY > 0.0D) {
/*  99 */       this.jump = true;
/* 100 */     } else if (mc.player.onGround) {
/* 101 */       this.jump = false;
/*     */     } 
/* 103 */     if (this.reverseStep.getBoolValue() && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.onGround && mc.player.motionY < 0.0D && mc.player.fallDistance < 1.0F && !this.jump) {
/* 104 */       mc.player.motionY = -1.0D;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 110 */     mc.player.stepHeight = 0.625F;
/* 111 */     mc.timer.timerSpeed = 1.0F;
/* 112 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */