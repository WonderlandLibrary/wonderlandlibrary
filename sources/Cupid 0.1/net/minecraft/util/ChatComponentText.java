package net.minecraft.util;

public class ChatComponentText extends ChatComponentStyle {
  private final String text;
  
  public ChatComponentText(String msg) {
    this.text = msg;
  }
  
  public String getChatComponentText_TextValue() {
    return this.text;
  }
  
  public String getUnformattedTextForChat() {
    return this.text;
  }
  
  public ChatComponentText createCopy() {
    ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
    chatcomponenttext.setChatStyle(getChatStyle().createShallowCopy());
    for (IChatComponent ichatcomponent : getSiblings())
      chatcomponenttext.appendSibling(ichatcomponent.createCopy()); 
    return chatcomponenttext;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof ChatComponentText))
      return false; 
    ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
    return (this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_));
  }
  
  public String toString() {
    return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\ChatComponentText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */