/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
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
    List<IChatComponent> children;
    private final String key;
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    private final Object[] formatArgs;
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    private final Object syncLock = new Object();

    public String getKey() {
        return this.key;
    }

    synchronized void ensureInitialized() {
        Object object = this.syncLock;
        synchronized (object) {
            long l = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            if (l == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = l;
            this.children.clear();
        }
        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
        }
        catch (ChatComponentTranslationFormatException chatComponentTranslationFormatException) {
            this.children.clear();
            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            }
            catch (ChatComponentTranslationFormatException chatComponentTranslationFormatException2) {
                throw chatComponentTranslationFormatException;
            }
        }
    }

    @Override
    public ChatComponentTranslation createCopy() {
        Object[] objectArray = new Object[this.formatArgs.length];
        int n = 0;
        while (n < this.formatArgs.length) {
            objectArray[n] = this.formatArgs[n] instanceof IChatComponent ? ((IChatComponent)this.formatArgs[n]).createCopy() : this.formatArgs[n];
            ++n;
        }
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(this.key, objectArray);
        chatComponentTranslation.setChatStyle(this.getChatStyle().createShallowCopy());
        for (IChatComponent iChatComponent : this.getSiblings()) {
            chatComponentTranslation.appendSibling(iChatComponent.createCopy());
        }
        return chatComponentTranslation;
    }

    private IChatComponent getFormatArgumentAsComponent(int n) {
        IChatComponent iChatComponent;
        if (n >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, n);
        }
        Object object = this.formatArgs[n];
        if (object instanceof IChatComponent) {
            iChatComponent = (IChatComponent)object;
        } else {
            iChatComponent = new ChatComponentText(object == null ? "null" : object.toString());
            iChatComponent.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return iChatComponent;
    }

    @Override
    public Iterator<IChatComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat(ChatComponentTranslation.createDeepCopyIterator(this.children), ChatComponentTranslation.createDeepCopyIterator(this.siblings));
    }

    public ChatComponentTranslation(String string, Object ... objectArray) {
        this.children = Lists.newArrayList();
        this.key = string;
        this.formatArgs = objectArray;
        Object[] objectArray2 = objectArray;
        int n = objectArray.length;
        int n2 = 0;
        while (n2 < n) {
            Object object = objectArray2[n2];
            if (object instanceof IChatComponent) {
                ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
            }
            ++n2;
        }
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.key.hashCode();
        n = 31 * n + Arrays.hashCode(this.formatArgs);
        return n;
    }

    @Override
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        StringBuilder stringBuilder = new StringBuilder();
        for (IChatComponent iChatComponent : this.children) {
            stringBuilder.append(iChatComponent.getUnformattedTextForChat());
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatComponentTranslation)) {
            return false;
        }
        ChatComponentTranslation chatComponentTranslation = (ChatComponentTranslation)object;
        return Arrays.equals(this.formatArgs, chatComponentTranslation.formatArgs) && this.key.equals(chatComponentTranslation.key) && super.equals(object);
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle chatStyle) {
        super.setChatStyle(chatStyle);
        Object[] objectArray = this.formatArgs;
        int n = this.formatArgs.length;
        int n2 = 0;
        while (n2 < n) {
            Object object = objectArray[n2];
            if (object instanceof IChatComponent) {
                ((IChatComponent)object).getChatStyle().setParentStyle(this.getChatStyle());
            }
            ++n2;
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (IChatComponent iChatComponent : this.children) {
                iChatComponent.getChatStyle().setParentStyle(chatStyle);
            }
        }
        return this;
    }

    public Object[] getFormatArgs() {
        return this.formatArgs;
    }

    protected void initializeFromFormat(String string) {
        boolean bl = false;
        Matcher matcher = stringVariablePattern.matcher(string);
        int n = 0;
        int n2 = 0;
        try {
            while (matcher.find(n2)) {
                Object object;
                Object object2;
                int n3 = matcher.start();
                int n4 = matcher.end();
                if (n3 > n2) {
                    object2 = new ChatComponentText(String.format(string.substring(n2, n3), new Object[0]));
                    ((ChatComponentStyle)object2).getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add((IChatComponent)object2);
                }
                object2 = matcher.group(2);
                String string2 = string.substring(n3, n4);
                if ("%".equals(object2) && "%%".equals(string2)) {
                    object = new ChatComponentText("%");
                    ((ChatComponentStyle)object).getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add((IChatComponent)object);
                } else {
                    int n5;
                    if (!"s".equals(object2)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + string2 + "'");
                    }
                    object = matcher.group(1);
                    int n6 = n5 = object != null ? Integer.parseInt((String)object) - 1 : n++;
                    if (n5 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(n5));
                    }
                }
                n2 = n4;
            }
            if (n2 < string.length()) {
                ChatComponentText chatComponentText = new ChatComponentText(String.format(string.substring(n2), new Object[0]));
                chatComponentText.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(chatComponentText);
            }
        }
        catch (IllegalFormatException illegalFormatException) {
            throw new ChatComponentTranslationFormatException(this, (Throwable)illegalFormatException);
        }
    }
}

