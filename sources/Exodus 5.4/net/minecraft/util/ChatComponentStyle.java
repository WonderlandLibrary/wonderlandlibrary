/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
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
    private ChatStyle style;
    protected List<IChatComponent> siblings = Lists.newArrayList();

    @Override
    public final String getFormattedText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (IChatComponent iChatComponent : this) {
            stringBuilder.append(iChatComponent.getChatStyle().getFormattingCode());
            stringBuilder.append(iChatComponent.getUnformattedTextForChat());
            stringBuilder.append((Object)EnumChatFormatting.RESET);
        }
        return stringBuilder.toString();
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    @Override
    public final String getUnformattedText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (IChatComponent iChatComponent : this) {
            stringBuilder.append(iChatComponent.getUnformattedTextForChat());
        }
        return stringBuilder.toString();
    }

    public static Iterator<IChatComponent> createDeepCopyIterator(Iterable<IChatComponent> iterable) {
        Iterator iterator = Iterators.concat((Iterator)Iterators.transform(iterable.iterator(), (Function)new Function<IChatComponent, Iterator<IChatComponent>>(){

            public Iterator<IChatComponent> apply(IChatComponent iChatComponent) {
                return iChatComponent.iterator();
            }
        }));
        iterator = Iterators.transform((Iterator)iterator, (Function)new Function<IChatComponent, IChatComponent>(){

            public IChatComponent apply(IChatComponent iChatComponent) {
                IChatComponent iChatComponent2 = iChatComponent.createCopy();
                iChatComponent2.setChatStyle(iChatComponent2.getChatStyle().createDeepCopy());
                return iChatComponent2;
            }
        });
        return iterator;
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle chatStyle) {
        this.style = chatStyle;
        for (IChatComponent iChatComponent : this.siblings) {
            iChatComponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return this;
    }

    @Override
    public Iterator<IChatComponent> iterator() {
        return Iterators.concat((Iterator)Iterators.forArray((Object[])new ChatComponentStyle[]{this}), ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }

    @Override
    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            for (IChatComponent iChatComponent : this.siblings) {
                iChatComponent.getChatStyle().setParentStyle(this.style);
            }
        }
        return this.style;
    }

    @Override
    public IChatComponent appendSibling(IChatComponent iChatComponent) {
        iChatComponent.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(iChatComponent);
        return this;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatComponentStyle)) {
            return false;
        }
        ChatComponentStyle chatComponentStyle = (ChatComponentStyle)object;
        return this.siblings.equals(chatComponentStyle.siblings) && this.getChatStyle().equals(chatComponentStyle.getChatStyle());
    }

    @Override
    public List<IChatComponent> getSiblings() {
        return this.siblings;
    }

    @Override
    public IChatComponent appendText(String string) {
        return this.appendSibling(new ChatComponentText(string));
    }
}

