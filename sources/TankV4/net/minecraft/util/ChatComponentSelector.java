package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle {
   private final String selector;

   public String getUnformattedTextForChat() {
      return this.selector;
   }

   public ChatComponentSelector(String var1) {
      this.selector = var1;
   }

   public String toString() {
      return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }

   public IChatComponent createCopy() {
      return this.createCopy();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChatComponentSelector)) {
         return false;
      } else {
         ChatComponentSelector var2 = (ChatComponentSelector)var1;
         return this.selector.equals(var2.selector) && super.equals(var1);
      }
   }

   public ChatComponentSelector createCopy() {
      ChatComponentSelector var1 = new ChatComponentSelector(this.selector);
      var1.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var3 = this.getSiblings().iterator();

      while(var3.hasNext()) {
         IChatComponent var2 = (IChatComponent)var3.next();
         var1.appendSibling(var2.createCopy());
      }

      return var1;
   }

   public String getSelector() {
      return this.selector;
   }
}
