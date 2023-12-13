/*    */ package org.neverhook.client.feature.impl.misc;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiNewChat;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.SPacketChat;
/*    */ import net.minecraft.util.text.ChatType;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import org.neverhook.client.event.EventTarget;
/*    */ import org.neverhook.client.event.events.impl.packet.EventReceivePacket;
/*    */ import org.neverhook.client.feature.Feature;
/*    */ import org.neverhook.client.feature.impl.Type;
/*    */ 
/*    */ 
/*    */ public class BetterChat
/*    */   extends Feature
/*    */ {
/* 18 */   private String lastMessage = "";
/*    */   private int amount;
/*    */   private int line;
/*    */   
/*    */   public BetterChat() {
/* 23 */     super("BetterChat", "Убирает спам", Type.Misc);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 29 */     Packet<?> packet = event.getPacket();
/* 30 */     if (packet instanceof net.minecraft.network.play.server.SPacketCloseWindow) {
/* 31 */       if (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)
/* 32 */         event.setCancelled(true); 
/*    */     } else {
/* 34 */       SPacketChat sPacketChat; if (packet instanceof SPacketChat && (sPacketChat = (SPacketChat)packet).getChatType() == ChatType.CHAT) {
/* 35 */         ITextComponent message = sPacketChat.getChatComponent();
/* 36 */         String rawMessage = message.getFormattedText();
/* 37 */         GuiNewChat chatGui = mc.ingameGUI.getChatGUI();
/* 38 */         if (this.lastMessage.equals(rawMessage)) {
/* 39 */           chatGui.deleteChatLine(this.line);
/* 40 */           this.amount++;
/* 41 */           sPacketChat.getChatComponent().appendText(TextFormatting.GRAY + " [x" + this.amount + "]");
/*    */         } else {
/* 43 */           this.amount = 1;
/*    */         } 
/* 45 */         this.line++;
/* 46 */         this.lastMessage = rawMessage;
/* 47 */         chatGui.printChatMessageWithOptionalDeletion(message, this.line);
/* 48 */         if (this.line > 256) {
/* 49 */           this.line = 0;
/*    */         }
/* 51 */         event.setCancelled(true);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\feature\impl\misc\BetterChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */