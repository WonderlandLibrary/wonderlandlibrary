/*    */ package org.neverhook.client.ui.notification;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ import org.neverhook.client.helpers.misc.TimerHelper;
/*    */ import org.neverhook.client.helpers.render.ScreenHelper;
/*    */ 
/*    */ public class Notification
/*    */   implements Helper {
/*    */   private final ScreenHelper screenHelper;
/*    */   private final FontRenderer fontRenderer;
/*    */   private final String title;
/*    */   private final String content;
/*    */   private final int time;
/*    */   private final NotificationType type;
/*    */   private final TimerHelper timer;
/*    */   
/*    */   public Notification(String title, String content, NotificationType type, int second, FontRenderer fontRenderer) {
/* 20 */     this.title = title;
/* 21 */     this.content = content;
/* 22 */     this.time = second;
/* 23 */     this.type = type;
/* 24 */     this.timer = new TimerHelper();
/* 25 */     this.fontRenderer = fontRenderer;
/* 26 */     ScaledResolution sr = new ScaledResolution(mc);
/* 27 */     this.screenHelper = new ScreenHelper((sr.getScaledWidth() - getWidth() + getWidth()), (sr.getScaledHeight() - 60));
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 31 */     return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 40);
/*    */   }
/*    */   
/*    */   public String getTitle() {
/* 35 */     return this.title;
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 39 */     return this.content;
/*    */   }
/*    */   
/*    */   public int getTime() {
/* 43 */     return this.time;
/*    */   }
/*    */   
/*    */   public NotificationType getType() {
/* 47 */     return this.type;
/*    */   }
/*    */   
/*    */   public TimerHelper getTimer() {
/* 51 */     return this.timer;
/*    */   }
/*    */   
/*    */   public ScreenHelper getTranslate() {
/* 55 */     return this.screenHelper;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\notification\Notification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */