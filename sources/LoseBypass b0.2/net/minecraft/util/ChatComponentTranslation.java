/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslationFormatException;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ChatComponentTranslation
extends ChatComponentStyle {
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    List<IChatComponent> children = Lists.newArrayList();
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public ChatComponentTranslation(String translationKey, Object ... args) {
        this.key = translationKey;
        this.formatArgs = args;
        Object[] objectArray = args;
        int n = objectArray.length;
        int n2 = 0;
        while (n2 < n) {
            Object object = objectArray[n2];
            if (object instanceof IChatComponent) {
                ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
            }
            ++n2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    synchronized void ensureInitialized() {
        Object object = this.syncLock;
        synchronized (object) {
            long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            if (i == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = i;
            this.children.clear();
        }
        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
            return;
        }
        catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception) {
            this.children.clear();
            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
                return;
            }
            catch (ChatComponentTranslationFormatException var5) {
                throw chatcomponenttranslationformatexception;
            }
        }
    }

    protected void initializeFromFormat(String format) {
        boolean flag = false;
        Matcher matcher = stringVariablePattern.matcher(format);
        int i = 0;
        int j = 0;
        try {
            while (true) {
                if (!matcher.find(j)) {
                    if (j >= format.length()) return;
                    ChatComponentText chatcomponenttext1 = new ChatComponentText(String.format(format.substring(j), new Object[0]));
                    chatcomponenttext1.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext1);
                    return;
                }
                int k = matcher.start();
                int l = matcher.end();
                if (k > j) {
                    ChatComponentText chatcomponenttext = new ChatComponentText(String.format(format.substring(j, k), new Object[0]));
                    chatcomponenttext.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext);
                }
                String s2 = matcher.group(2);
                String s = format.substring(k, l);
                if ("%".equals(s2) && "%%".equals(s)) {
                    ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
                    chatcomponenttext2.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext2);
                } else {
                    int i1;
                    if (!"s".equals(s2)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + s + "'");
                    }
                    String s1 = matcher.group(1);
                    int n = i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;
                    if (i1 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i1));
                    }
                }
                j = l;
            }
        }
        catch (IllegalFormatException illegalformatexception) {
            throw new ChatComponentTranslationFormatException(this, (Throwable)illegalformatexception);
        }
    }

    private IChatComponent getFormatArgumentAsComponent(int index) {
        if (index >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, index);
        }
        Object object = this.formatArgs[index];
        if (object instanceof IChatComponent) {
            return (IChatComponent)object;
        }
        IChatComponent ichatcomponent = new ChatComponentText(object == null ? "null" : object.toString());
        ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        return ichatcomponent;
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        super.setChatStyle(style);
        for (Object object : this.formatArgs) {
            if (!(object instanceof IChatComponent)) continue;
            ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
        }
        if (this.lastTranslationUpdateTimeInMilliseconds <= -1L) return this;
        Iterator<IChatComponent> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)iterator.next();
            ichatcomponent.getChatStyle().setParentStyle(style);
        }
        return this;
    }

    @Override
    public Iterator<IChatComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat(ChatComponentTranslation.createDeepCopyIterator(this.children), ChatComponentTranslation.createDeepCopyIterator(this.siblings));
    }

    @Override
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<IChatComponent> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }
        return stringbuilder.toString();
    }

    @Override
    public ChatComponentTranslation createCopy() {
        Object[] aobject = new Object[this.formatArgs.length];
        for (int i = 0; i < this.formatArgs.length; ++i) {
            aobject[i] = this.formatArgs[i] instanceof IChatComponent ? ((IChatComponent)this.formatArgs[i]).createCopy() : this.formatArgs[i];
        }
        ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.key, aobject);
        chatcomponenttranslation.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator<IChatComponent> iterator = this.getSiblings().iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponenttranslation;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        }
        ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_equals_1_;
        if (!Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs)) return false;
        if (!this.key.equals(chatcomponenttranslation.key)) return false;
        if (!super.equals(p_equals_1_)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        return 31 * i + Arrays.hashCode(this.formatArgs);
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
}

