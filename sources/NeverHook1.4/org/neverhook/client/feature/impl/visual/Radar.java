/*    */ package org.neverhook.client.feature.impl.visual;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.render.EventRender2D;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ import org.neverhook.client.helpers.palette.PaletteHelper;
/*    */ import org.neverhook.client.helpers.render.RenderHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ import org.neverhook.client.settings.Setting;
/*    */ import org.neverhook.client.settings.impl.NumberSetting;
/*    */ 
/*    */ public class Radar extends Feature {
/*    */   private final NumberSetting size;
/*    */   private final NumberSetting posx;
/*    */   private final NumberSetting posy;
/*    */   public int scale;
/*    */   
/*    */   public Radar() {
/* 25 */     super("Radar", "Показывает радар и игроков на нем", Type.Visuals);
/* 26 */     this.posx = new NumberSetting("PosX", 860.0F, 0.0F, 900.0F, 1.0F, () -> Boolean.valueOf(true));
/* 27 */     this.posy = new NumberSetting("PosY", 15.0F, 0.0F, 350.0F, 1.0F, () -> Boolean.valueOf(true));
/* 28 */     this.size = new NumberSetting("Size", 100.0F, 30.0F, 300.0F, 1.0F, () -> Boolean.valueOf(true));
/* 29 */     addSettings(new Setting[] { (Setting)this.posx, (Setting)this.posy, (Setting)this.size });
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender2D(EventRender2D event) {
/* 34 */     double psx = this.posx.getNumberValue();
/* 35 */     double psy = this.posy.getNumberValue();
/* 36 */     ScaledResolution sr = new ScaledResolution(mc);
/* 37 */     this.scale = 2;
/* 38 */     int sizeRect = (int)this.size.getNumberValue();
/* 39 */     float xOffset = (float)((sr.getScaledWidth() - sizeRect) - psx);
/* 40 */     float yOffset = (float)psy;
/* 41 */     double playerPosX = mc.player.posX;
/* 42 */     double playerPosZ = mc.player.posZ;
/* 43 */     RectHelper.drawBorderedRect(xOffset + 2.5F, yOffset + 2.5F, xOffset + sizeRect - 2.5F, yOffset + sizeRect - 2.5F, 0.5F, PaletteHelper.getColor(2), PaletteHelper.getColor(11), false);
/* 44 */     RectHelper.drawBorderedRect(xOffset + 3.0F, yOffset + 3.0F, xOffset + sizeRect - 3.0F, yOffset + sizeRect - 3.0F, 0.2F, PaletteHelper.getColor(2), PaletteHelper.getColor(11), false);
/* 45 */     RectHelper.drawRect(xOffset + (sizeRect / 2.0F) - 0.5D, yOffset + 3.5D, xOffset + (sizeRect / 2.0F) + 0.2D, (yOffset + sizeRect) - 3.5D, PaletteHelper.getColor(155, 100));
/* 46 */     RectHelper.drawRect(xOffset + 3.5D, yOffset + (sizeRect / 2.0F) - 0.2D, (xOffset + sizeRect) - 3.5D, yOffset + (sizeRect / 2.0F) + 0.5D, PaletteHelper.getColor(155, 100));
/* 47 */     RenderHelper.drawImage(new ResourceLocation("neverhook/skeet.png"), xOffset + 3.5F, yOffset + 3.5F, (sizeRect - 7), 1.0F, Color.WHITE);
/* 48 */     for (EntityPlayer entityPlayer : mc.world.playerEntities) {
/* 49 */       if (entityPlayer == mc.player) {
/*    */         continue;
/*    */       }
/* 52 */       float partialTicks = mc.timer.renderPartialTicks;
/* 53 */       float posX = (float)(entityPlayer.posX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks - playerPosX) * 2.0F;
/* 54 */       float posZ = (float)(entityPlayer.posZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks - playerPosZ) * 2.0F;
/* 55 */       int color = mc.player.canEntityBeSeen((Entity)entityPlayer) ? (new Color(255, 0, 0)).getRGB() : (new Color(255, 255, 0)).getRGB();
/* 56 */       float cos = (float)Math.cos(mc.player.rotationYaw * 0.017453292D);
/* 57 */       float sin = (float)Math.sin(mc.player.rotationYaw * 0.017453292D);
/* 58 */       float rotY = -(posZ * cos - posX * sin);
/* 59 */       float rotX = -(posX * cos + posZ * sin);
/* 60 */       if (rotY > sizeRect / 2.0F - 6.0F) {
/* 61 */         rotY = sizeRect / 2.0F - 6.0F;
/* 62 */       } else if (rotY < -(sizeRect / 2.0F - 8.0F)) {
/* 63 */         rotY = -(sizeRect / 2.0F - 8.0F);
/*    */       } 
/* 65 */       if (rotX > sizeRect / 2.0F - 5.0F) {
/* 66 */         rotX = sizeRect / 2.0F - 5.0F;
/* 67 */       } else if (rotX < -(sizeRect / 2.0F - 5.0F)) {
/* 68 */         rotX = -(sizeRect / 2.0F - 5.0F);
/*    */       } 
/* 70 */       RectHelper.drawRect((xOffset + sizeRect / 2.0F + rotX - 1.5F), (yOffset + sizeRect / 2.0F + rotY - 1.5F), (xOffset + sizeRect / 2.0F + rotX + 1.5F), (yOffset + sizeRect / 2.0F + rotY + 1.5F), color);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\visual\Radar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */