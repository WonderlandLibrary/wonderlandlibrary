package net.minecraft.util;

import java.util.Iterator;
import java.util.List;

public class ChatComponentText
  extends ChatComponentStyle
{
  private final String text;
  private static final String __OBFID = "CL_00001269";
  
  public ChatComponentText(String msg)
  {
    this.text = msg;
  }
  
  public String getChatComponentText_TextValue()
  {
    return this.text;
  }
  
  public String getUnformattedTextForChat()
  {
    return this.text;
  }
  
  public ChatComponentText createCopy()
  {
    ChatComponentText var1 = new ChatComponentText(this.text);
    var1.setChatStyle(getChatStyle().createShallowCopy());
    Iterator var2 = getSiblings().iterator();
    while (var2.hasNext())
    {
      IChatComponent var3 = (IChatComponent)var2.next();
      var1.appendSibling(var3.createCopy());
    }
    return var1;
  }
  
  public boolean equals(Object p_equals_1_)
  {
    if (this == p_equals_1_) {
      return true;
    }
    if (!(p_equals_1_ instanceof ChatComponentText)) {
      return false;
    }
    ChatComponentText var2 = (ChatComponentText)p_equals_1_;
    return (this.text.equals(var2.getChatComponentText_TextValue())) && (super.equals(p_equals_1_));
  }
  
  public String toString()
  {
    return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
  }
}
