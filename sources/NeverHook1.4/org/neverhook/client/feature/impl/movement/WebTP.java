/*    */ package org.neverhook.client.feature.impl.movement;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketPlayer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.RayTraceResult;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.player.EventBlockInteract;
/*    */ import org.neverhook.client.event.events.impl.player.EventUpdate;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender3D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.BooleanSetting;
/*    */ import org.neverhook.client.settings.impl.ColorSetting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ 
/*    */ public class WebTP
/*    */   extends Feature
/*    */ {
/*    */   public static NumberSetting maxBlockReachValue;
/*    */   public static BooleanSetting webESP;
/*    */   public static BooleanSetting autoDisable;
/*    */   public static ColorSetting webESPColor;
/*    */   
/*    */   public WebTP() {
/* 31 */     super("WebTP", "Позволяет телепортироваться на большие расстояния с помощью паутины", Type.Movement);
/* 32 */     maxBlockReachValue = new NumberSetting("Max reach value", 120.0F, 10.0F, 500.0F, 10.0F, () -> Boolean.valueOf(true));
/* 33 */     autoDisable = new BooleanSetting("Auto Disable", true, () -> Boolean.valueOf(true));
/* 34 */     webESP = new BooleanSetting("Position ESP", true, () -> Boolean.valueOf(true));
/* 35 */     webESPColor = new ColorSetting("Color", (new Color(16777215)).getRGB(), webESP::getBoolValue);
/* 36 */     addSettings(new Setting[] { (Setting)maxBlockReachValue, (Setting)autoDisable, (Setting)webESP, (Setting)webESPColor });
/*    */   }
/*    */   private int x; private int y; private int z; private boolean wasClick;
/*    */   
/*    */   public void onDisable() {
/* 41 */     this.x = (int)mc.player.posX;
/* 42 */     this.y = (int)mc.player.posY;
/* 43 */     this.z = (int)mc.player.posZ;
/* 44 */     this.wasClick = false;
/* 45 */     super.onDisable();
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender3D(EventRender3D event) {
/* 50 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 52 */     if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.player.isInWeb) {
/* 53 */       Color color = new Color(webESPColor.getColorValue());
/* 54 */       BlockPos pos = mc.objectMouseOver.getBlockPos();
/* 55 */       if (webESP.getBoolValue()) {
/* 56 */         GlStateManager.pushMatrix();
/* 57 */         RenderHelper.blockEsp(pos, color, true);
/* 58 */         GlStateManager.popMatrix();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 65 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 67 */     if (this.wasClick && mc.player.isInWeb) {
/* 68 */       mc.player.onGround = false;
/* 69 */       mc.player.motionY *= -12.0D;
/* 70 */       mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.x, (this.y + 3), this.z, true));
/* 71 */     } else if (mc.player.posX == this.x && mc.player.posY == (this.y + 3) && mc.player.posZ == this.z) {
/* 72 */       this.wasClick = false;
/* 73 */       if (autoDisable.getBoolValue()) {
/* 74 */         state();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onBlockInteract(EventBlockInteract event) {
/* 81 */     if (mc.player == null || mc.world == null)
/*    */       return; 
/* 83 */     if (event.getPos() != null) {
/* 84 */       BlockPos pos = event.getPos();
/* 85 */       if (!this.wasClick) {
/* 86 */         this.x = pos.getX();
/* 87 */         this.y = pos.getY();
/* 88 */         this.z = pos.getZ();
/* 89 */         this.wasClick = true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\movement\WebTP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */