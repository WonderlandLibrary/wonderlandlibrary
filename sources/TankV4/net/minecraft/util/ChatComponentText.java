package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
   private final String text;

   public ChatComponentText(String var1) {
      this.text = var1;
   }

   public String getUnformattedTextForChat() {
      return this.text;
   }

   public String getChatComponentText_TextValue() {
      return this.text;
   }

   public IChatComponent createCopy() {
      return this.createCopy();
   }

   public String toString() {
      return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChatComponentText)) {
         return false;
      } else {
         ChatComponentText var2 = (ChatComponentText)var1;
         return this.text.equals(var2.getChatComponentText_TextValue()) && super.equals(var1);
      }
   }

   public ChatComponentText createCopy() {
      ChatComponentText var1 = new ChatComponentText(this.text);
      var1.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var3 = this.getSiblings().iterator();

      while(var3.hasNext()) {
         IChatComponent var2 = (IChatComponent)var3.next();
         var1.appendSibling(var2.createCopy());
      }

      return var1;
   }
}
