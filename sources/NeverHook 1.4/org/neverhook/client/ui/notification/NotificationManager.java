/*    */ package org.neverhook.client.ui.notification;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import org.neverhook.client.NeverHook;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.feature.impl.hud.Notifications;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.helpers.misc.ClientHelper;
/*    */ import org.neverhook.client.helpers.render.ScreenHelper;
/*    */ import org.neverhook.client.helpers.render.rect.RectHelper;
/*    */ 
/*    */ public class NotificationManager
/*    */   implements Helper
/*    */ {
/* 21 */   private static final List<Notification> notifications = new CopyOnWriteArrayList<>();
/*    */   
/*    */   public static void publicity(String title, String content, int second, NotificationType type) {
/* 24 */     FontRenderer fontRenderer = mc.fontRendererObj;
/* 25 */     notifications.add(new Notification(title, content, type, second * 1000, fontRenderer));
/*    */   }
/*    */   
/*    */   public static void renderNotification(ScaledResolution sr) {
/* 29 */     if (NeverHook.instance.featureManager.getFeatureByClass(Notifications.class).getState() && 
/* 30 */       !notifications.isEmpty()) {
/* 31 */       int srScaledHeight = sr.getScaledHeight();
/* 32 */       int scaledWidth = sr.getScaledWidth() + 10;
/* 33 */       int y = srScaledHeight - 60;
/* 34 */       for (Notification notification : notifications) {
/* 35 */         ScreenHelper screenHelper = notification.getTranslate();
/* 36 */         int width = notification.getWidth() + 40 + mc.fontRendererObj.getStringWidth(notification.getContent()) / 2;
/* 37 */         if (!notification.getTimer().hasReached((notification.getTime() - 100))) {
/* 38 */           screenHelper.calculateCompensation((scaledWidth - width), y, 0.12F, 5.0F);
/*    */         } else {
/* 40 */           screenHelper.calculateCompensation(scaledWidth, notification.getTranslate().getY(), 0.12F, 5.0F);
/* 41 */           if (mc.player != null && mc.world != null && 
/* 42 */             notification.getTimer().getTime() > (notification.getTime() + 500)) {
/* 43 */             notifications.remove(notification);
/*    */           }
/*    */         } 
/*    */         
/* 47 */         float translateX = screenHelper.getX();
/* 48 */         float translateY = screenHelper.getY();
/* 49 */         GlStateManager.pushMatrix();
/* 50 */         GlStateManager.disableBlend();
/* 51 */         RectHelper.drawRect(translateX, translateY, (translateX - 2.0F), (translateY + 28.0F), ClientHelper.getClientColor().getRGB());
/* 52 */         RectHelper.drawRect(translateX, translateY, scaledWidth, (translateY + 28.0F), (new Color(35, 34, 34)).getRGB());
/*    */ 
/*    */         
/* 55 */         if (!HUD.font.currentMode.equals("Minecraft")) {
/* 56 */           mc.latoBig.drawStringWithShadow(TextFormatting.BOLD + notification.getTitle(), (translateX + 5.0F), (translateY + 4.0F), -1);
/* 57 */           mc.latoFontRender.drawStringWithShadow(notification.getContent(), (translateX + 5.0F), (translateY + 17.0F), (new Color(245, 245, 245)).getRGB());
/*    */         } else {
/* 59 */           mc.fontRendererObj.drawStringWithShadow(TextFormatting.BOLD + notification.getTitle(), translateX + 5.0F, translateY + 4.0F, -1);
/* 60 */           mc.fontRendererObj.drawStringWithShadow(notification.getContent(), translateX + 5.0F, translateY + 15.0F, (new Color(245, 245, 245)).getRGB());
/*    */         } 
/*    */         
/* 63 */         GlStateManager.popMatrix();
/*    */         
/* 65 */         if (notifications.size() > 1)
/* 66 */           y -= 35; 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\notification\NotificationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */