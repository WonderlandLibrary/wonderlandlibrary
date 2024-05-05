package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class ChatComponentStyle
implements IChatComponent {
    protected List<IChatComponent> siblings = Lists.newArrayList();
    private ChatStyle style;

    public IChatComponent appendSibling(IChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add((Object)component);
        return this;
    }

    public List<IChatComponent> getSiblings() {
        return this.siblings;
    }

    public IChatComponent appendText(String text) {
        return this.appendSibling((IChatComponent)new ChatComponentText(text));
    }

    public IChatComponent setChatStyle(ChatStyle style) {
        this.style = style;
        for (IChatComponent ichatcomponent : this.siblings) {
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return this;
    }

    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            for (IChatComponent ichatcomponent : this.siblings) {
                ichatcomponent.getChatStyle().setParentStyle(this.style);
            }
        }
        return this.style;
    }

    public Iterator<IChatComponent> iterator() {
        return Iterators.concat((Iterator)Iterators.forArray((Object[])new ChatComponentStyle[]{this}), ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }

    public final String getUnformattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<IChatComponent> iterator = this.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)iterator.next();
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }
        return stringbuilder.toString();
    }

    public final String getFormattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<IChatComponent> iterator = this.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)iterator.next();
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append((Object)EnumChatFormatting.RESET);
        }
        return stringbuilder.toString();
    }

    public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components) {
        Iterator iterator = Iterators.concat((Iterator)Iterators.transform((Iterator)components.iterator(), (Function)new /* Unavailable Anonymous Inner Class!! */));
        iterator = Iterators.transform((Iterator)iterator, (Function)new /* Unavailable Anonymous Inner Class!! */);
        return iterator;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentStyle)) {
            return false;
        }
        ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
        return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals((Object)chatcomponentstyle.getChatStyle());
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
