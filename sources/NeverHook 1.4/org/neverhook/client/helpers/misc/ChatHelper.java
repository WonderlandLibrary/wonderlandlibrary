/*    */ package org.neverhook.client.helpers.misc;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import javafx.scene.effect.Glow;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class ChatHelper implements Helper {
/* 10 */   public static String chatPrefix = "§7[" + ChatFormatting.RED + "N" + ChatFormatting.WHITE + "ever" + ChatFormatting.RED + "H" + ChatFormatting.WHITE + "ook" + ChatFormatting.RESET + "§7] " + ChatFormatting.RESET;
/*    */   
/*    */   public static void addChatMessage(String message) {
/* 13 */     Glow glow = new Glow();
/* 14 */     mc.player.addChatMessage((ITextComponent)new TextComponentString(chatPrefix + message));
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\misc\ChatHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */