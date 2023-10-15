/*   */ package org.neverhook.client.helpers.input;
/*   */ 
/*   */ import java.util.regex.Pattern;
/*   */ 
/*   */ public class StringHelper
/*   */ {
/*   */   public static String format(String text) {
/* 8 */     return Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(text).replaceAll("");
/*   */   }
/*   */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\input\StringHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */