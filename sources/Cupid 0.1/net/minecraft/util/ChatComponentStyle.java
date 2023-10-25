package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent {
  protected List<IChatComponent> siblings = Lists.newArrayList();
  
  private ChatStyle style;
  
  public IChatComponent appendSibling(IChatComponent component) {
    component.getChatStyle().setParentStyle(getChatStyle());
    this.siblings.add(component);
    return this;
  }
  
  public List<IChatComponent> getSiblings() {
    return this.siblings;
  }
  
  public IChatComponent appendText(String text) {
    return appendSibling(new ChatComponentText(text));
  }
  
  public IChatComponent setChatStyle(ChatStyle style) {
    this.style = style;
    for (IChatComponent ichatcomponent : this.siblings)
      ichatcomponent.getChatStyle().setParentStyle(getChatStyle()); 
    return this;
  }
  
  public ChatStyle getChatStyle() {
    if (this.style == null) {
      this.style = new ChatStyle();
      for (IChatComponent ichatcomponent : this.siblings)
        ichatcomponent.getChatStyle().setParentStyle(this.style); 
    } 
    return this.style;
  }
  
  public Iterator<IChatComponent> iterator() {
    return Iterators.concat((Iterator)Iterators.forArray((Object[])new ChatComponentStyle[] { this }, ), createDeepCopyIterator(this.siblings));
  }
  
  public final String getUnformattedText() {
    StringBuilder stringbuilder = new StringBuilder();
    for (IChatComponent ichatcomponent : this)
      stringbuilder.append(ichatcomponent.getUnformattedTextForChat()); 
    return stringbuilder.toString();
  }
  
  public final String getFormattedText() {
    StringBuilder stringbuilder = new StringBuilder();
    for (IChatComponent ichatcomponent : this) {
      stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
      stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
      stringbuilder.append(EnumChatFormatting.RESET);
    } 
    return stringbuilder.toString();
  }
  
  public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components) {
    Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function<IChatComponent, Iterator<IChatComponent>>() {
            public Iterator<IChatComponent> apply(IChatComponent p_apply_1_) {
              return p_apply_1_.iterator();
            }
          }));
    iterator = Iterators.transform(iterator, new Function<IChatComponent, IChatComponent>() {
          public IChatComponent apply(IChatComponent p_apply_1_) {
            IChatComponent ichatcomponent = p_apply_1_.createCopy();
            ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
            return ichatcomponent;
          }
        });
    return iterator;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof ChatComponentStyle))
      return false; 
    ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
    return (this.siblings.equals(chatcomponentstyle.siblings) && getChatStyle().equals(chatcomponentstyle.getChatStyle()));
  }
  
  public int hashCode() {
    return 31 * this.style.hashCode() + this.siblings.hashCode();
  }
  
  public String toString() {
    return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\ChatComponentStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */