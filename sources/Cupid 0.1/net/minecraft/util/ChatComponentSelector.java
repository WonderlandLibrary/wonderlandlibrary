package net.minecraft.util;

public class ChatComponentSelector extends ChatComponentStyle {
  private final String selector;
  
  public ChatComponentSelector(String selectorIn) {
    this.selector = selectorIn;
  }
  
  public String getSelector() {
    return this.selector;
  }
  
  public String getUnformattedTextForChat() {
    return this.selector;
  }
  
  public ChatComponentSelector createCopy() {
    ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
    chatcomponentselector.setChatStyle(getChatStyle().createShallowCopy());
    for (IChatComponent ichatcomponent : getSiblings())
      chatcomponentselector.appendSibling(ichatcomponent.createCopy()); 
    return chatcomponentselector;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof ChatComponentSelector))
      return false; 
    ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
    return (this.selector.equals(chatcomponentselector.selector) && super.equals(p_equals_1_));
  }
  
  public String toString() {
    return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\ChatComponentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */