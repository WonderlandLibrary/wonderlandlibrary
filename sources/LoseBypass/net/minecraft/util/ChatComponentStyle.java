/*
 * Decompiled with CFR 0.152.
 */
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

    @Override
    public IChatComponent appendSibling(IChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(component);
        return this;
    }

    @Override
    public List<IChatComponent> getSiblings() {
        return this.siblings;
    }

    @Override
    public IChatComponent appendText(String text) {
        return this.appendSibling(new ChatComponentText(text));
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        this.style = style;
        Iterator<IChatComponent> iterator = this.siblings.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return this;
    }

    @Override
    public ChatStyle getChatStyle() {
        if (this.style != null) return this.style;
        this.style = new ChatStyle();
        Iterator<IChatComponent> iterator = this.siblings.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            ichatcomponent.getChatStyle().setParentStyle(this.style);
        }
        return this.style;
    }

    @Override
    public Iterator<IChatComponent> iterator() {
        return Iterators.concat(Iterators.forArray(this), ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }

    @Override
    public final String getUnformattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<IChatComponent> iterator = this.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }
        return stringbuilder.toString();
    }

    @Override
    public final String getFormattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<IChatComponent> iterator = this.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append((Object)EnumChatFormatting.RESET);
        }
        return stringbuilder.toString();
    }

    public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> components) {
        Iterator<IChatComponent> iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function<IChatComponent, Iterator<IChatComponent>>(){

            @Override
            public Iterator<IChatComponent> apply(IChatComponent p_apply_1_) {
                return p_apply_1_.iterator();
            }
        }));
        return Iterators.transform(iterator, new Function<IChatComponent, IChatComponent>(){

            @Override
            public IChatComponent apply(IChatComponent p_apply_1_) {
                IChatComponent ichatcomponent = p_apply_1_.createCopy();
                ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
                return ichatcomponent;
            }
        });
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentStyle)) {
            return false;
        }
        ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
        if (!this.siblings.equals(chatcomponentstyle.siblings)) return false;
        if (!this.getChatStyle().equals(chatcomponentstyle.getChatStyle())) return false;
        return true;
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}

