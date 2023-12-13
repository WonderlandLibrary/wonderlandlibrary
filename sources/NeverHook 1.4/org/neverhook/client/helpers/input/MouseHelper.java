/*   */ package org.neverhook.client.helpers.input;
/*   */ 
/*   */ import org.neverhook.client.helpers.Helper;
/*   */ 
/*   */ public class MouseHelper
/*   */   implements Helper {
/*   */   public static boolean isHovered(double x, double y, double mouseX, double mouseY, int width, int height) {
/* 8 */     return (width > x && height > y && width < mouseX && height < mouseY);
/*   */   }
/*   */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\input\MouseHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */