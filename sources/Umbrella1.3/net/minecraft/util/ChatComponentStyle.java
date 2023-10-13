/*
 * Decompiled with CFR 0.150.
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
    protected List siblings = Lists.newArrayList();
    private ChatStyle style;
    private static final String __OBFID = "CL_00001257";

    @Override
    public IChatComponent appendSibling(IChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(component);
        return this;
    }

    @Override
    public List getSiblings() {
        return this.siblings;
    }

    @Override
    public IChatComponent appendText(String text) {
        return this.appendSibling(new ChatComponentText(text));
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        this.style = style;
        for (IChatComponent var3 : this.siblings) {
            var3.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return this;
    }

    @Override
    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            for (IChatComponent var2 : this.siblings) {
                var2.getChatStyle().setParentStyle(this.style);
            }
        }
        return this.style;
    }

    public Iterator iterator() {
        return Iterators.concat((Iterator)Iterators.forArray((Object[])new ChatComponentStyle[]{this}), (Iterator)ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }

    @Override
    public final String getUnformattedText() {
        StringBuilder var1 = new StringBuilder();
        for (IChatComponent var3 : this) {
            var1.append(var3.getUnformattedTextForChat());
        }
        return var1.toString();
    }

    @Override
    public final String getFormattedText() {
        StringBuilder var1 = new StringBuilder();
        for (IChatComponent var3 : this) {
            var1.append(var3.getChatStyle().getFormattingCode());
            var1.append(var3.getUnformattedTextForChat());
            var1.append((Object)EnumChatFormatting.RESET);
        }
        return var1.toString();
    }

    public static Iterator createDeepCopyIterator(Iterable components) {
        Iterator var1 = Iterators.concat((Iterator)Iterators.transform(components.iterator(), (Function)new Function(){
            private static final String __OBFID = "CL_00001258";

            public Iterator apply(IChatComponent p_apply_1_) {
                return p_apply_1_.iterator();
            }

            public Object apply(Object p_apply_1_) {
                return this.apply((IChatComponent)p_apply_1_);
            }
        }));
        var1 = Iterators.transform((Iterator)var1, (Function)new Function(){
            private static final String __OBFID = "CL_00001259";

            public IChatComponent apply(IChatComponent p_apply_1_) {
                IChatComponent var2 = p_apply_1_.createCopy();
                var2.setChatStyle(var2.getChatStyle().createDeepCopy());
                return var2;
            }

            public Object apply(Object p_apply_1_) {
                return this.apply((IChatComponent)p_apply_1_);
            }
        });
        return var1;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentStyle)) {
            return false;
        }
        ChatComponentStyle var2 = (ChatComponentStyle)p_equals_1_;
        return this.siblings.equals(var2.siblings) && this.getChatStyle().equals(var2.getChatStyle());
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}

