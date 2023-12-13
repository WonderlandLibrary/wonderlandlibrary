package my.NewSnake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatMessage {
   private final ChatComponentText message;

   static ChatComponentText access$0(ChatMessage var0) {
      return var0.getChatComponent();
   }

   private ChatMessage(ChatComponentText var1) {
      this.message = var1;
   }

   private ChatComponentText getChatComponent() {
      return this.message;
   }

   public void displayClientSided() {
      Minecraft.getMinecraft();
      Minecraft.thePlayer.addChatMessage(this.message);
   }

   ChatMessage(ChatComponentText var1, ChatMessage var2) {
      this(var1);
   }

   public static class ChatMessageBuilder {
      private boolean useDefaultMessageColor = false;
      private static final EnumChatFormatting defaultMessageColor;
      private ChatComponentText workerMessage = new ChatComponentText("");
      private static final ChatComponentText defaultPrefix;
      private ChatStyle workingStyle = new ChatStyle();
      private ChatComponentText theMessage = new ChatComponentText("");

      public ChatMessage.ChatMessageBuilder appendText(String var1) {
         this.appendSibling();
         this.workerMessage = new ChatComponentText(var1);
         this.workingStyle = new ChatStyle();
         if (this.useDefaultMessageColor) {
            this.setColor(defaultMessageColor);
         }

         return this;
      }

      static {
         defaultMessageColor = EnumChatFormatting.WHITE;
         defaultPrefix = ChatMessage.access$0((new ChatMessage.ChatMessageBuilder(false, false)).appendText(" ").setColor(EnumChatFormatting.DARK_GRAY).build());
      }

      public ChatMessage.ChatMessageBuilder underline() {
         this.workingStyle.setUnderlined(true);
         return this;
      }

      public ChatMessage.ChatMessageBuilder bold() {
         this.workingStyle.setBold(true);
         return this;
      }

      public ChatMessageBuilder(boolean var1, boolean var2) {
         if (var1) {
            this.theMessage.appendSibling(defaultPrefix);
         }

         this.useDefaultMessageColor = var2;
      }

      public ChatMessage.ChatMessageBuilder setColor(EnumChatFormatting var1) {
         this.workingStyle.setColor(var1);
         return this;
      }

      public ChatMessage.ChatMessageBuilder strikethrough() {
         this.workingStyle.setStrikethrough(true);
         return this;
      }

      public ChatMessage build() {
         this.appendSibling();
         return new ChatMessage(this.theMessage, (ChatMessage)null);
      }

      private void appendSibling() {
         this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
      }

      public ChatMessageBuilder() {
      }

      public ChatMessage.ChatMessageBuilder italic() {
         this.workingStyle.setItalic(true);
         return this;
      }
   }
}
