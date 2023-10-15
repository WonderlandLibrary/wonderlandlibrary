/*    */ package org.neverhook.client.ui.font;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ public class FontUtil
/*    */ {
/*    */   public static Font getFontFromTTF(ResourceLocation loc, float fontSize, int fontType) {
/*    */     try {
/* 12 */       Font output = Font.createFont(fontType, Minecraft.getInstance().getResourceManager().getResource(loc).getInputStream());
/* 13 */       output = output.deriveFont(fontSize);
/* 14 */       return output;
/* 15 */     } catch (Exception e) {
/* 16 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\clien\\ui\font\FontUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */