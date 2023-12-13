/*
 * Decompiled with CFR 0.150.
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
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    List children = Lists.newArrayList();
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    private static final String __OBFID = "CL_00001270";

    public ChatComponentTranslation(String translationKey, Object ... args) {
        this.key = translationKey;
        this.formatArgs = args;
        Object[] var3 = args;
        int var4 = args.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            if (!(var6 instanceof IChatComponent)) continue;
            ((IChatComponent)var6).getChatStyle().setParentStyle(this.getChatStyle());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    synchronized void ensureInitialized() {
        Object var1 = this.syncLock;
        Object object = this.syncLock;
        synchronized (object) {
            long var2 = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            if (var2 == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = var2;
            this.children.clear();
        }
        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
        }
        catch (ChatComponentTranslationFormatException var6) {
            this.children.clear();
            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            }
            catch (ChatComponentTranslationFormatException var5) {
                throw var6;
            }
        }
    }

    protected void initializeFromFormat(String format) {
        boolean var2 = false;
        Matcher var3 = stringVariablePattern.matcher(format);
        int var4 = 0;
        int var5 = 0;
        try {
            while (var3.find(var5)) {
                int var6 = var3.start();
                int var7 = var3.end();
                if (var6 > var5) {
                    ChatComponentText var8 = new ChatComponentText(String.format(format.substring(var5, var6), new Object[0]));
                    var8.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(var8);
                }
                String var14 = var3.group(2);
                String var9 = format.substring(var6, var7);
                if ("%".equals(var14) && "%%".equals(var9)) {
                    ChatComponentText var15 = new ChatComponentText("%");
                    var15.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(var15);
                } else {
                    int var11;
                    if (!"s".equals(var14)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + var9 + "'");
                    }
                    String var10 = var3.group(1);
                    int n = var11 = var10 != null ? Integer.parseInt(var10) - 1 : var4++;
                    if (var11 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(var11));
                    }
                }
                var5 = var7;
            }
            if (var5 < format.length()) {
                ChatComponentText var13 = new ChatComponentText(String.format(format.substring(var5), new Object[0]));
                var13.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(var13);
            }
        }
        catch (IllegalFormatException var12) {
            throw new ChatComponentTranslationFormatException(this, (Throwable)var12);
        }
    }

    private IChatComponent getFormatArgumentAsComponent(int index) {
        IChatComponent var3;
        if (index >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, index);
        }
        Object var2 = this.formatArgs[index];
        if (var2 instanceof IChatComponent) {
            var3 = (IChatComponent)var2;
        } else {
            var3 = new ChatComponentText(var2 == null ? "null" : var2.toString());
            var3.getChatStyle().setParentStyle(this.getChatStyle());
        }
        return var3;
    }

    @Override
    public IChatComponent setChatStyle(ChatStyle style) {
        super.setChatStyle(style);
        for (Object var5 : this.formatArgs) {
            if (!(var5 instanceof IChatComponent)) continue;
            ((IChatComponent)var5).getChatStyle().setParentStyle(this.getChatStyle());
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (IChatComponent var7 : this.children) {
                var7.getChatStyle().setParentStyle(style);
            }
        }
        return this;
    }

    @Override
    public Iterator iterator() {
        this.ensureInitialized();
        return Iterators.concat((Iterator)ChatComponentTranslation.createDeepCopyIterator(this.children), (Iterator)ChatComponentTranslation.createDeepCopyIterator(this.siblings));
    }

    @Override
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        StringBuilder var1 = new StringBuilder();
        for (IChatComponent var3 : this.children) {
            var1.append(var3.getUnformattedTextForChat());
        }
        return var1.toString();
    }

    @Override
    public ChatComponentTranslation createCopy() {
        Object[] var1 = new Object[this.formatArgs.length];
        for (int var2 = 0; var2 < this.formatArgs.length; ++var2) {
            var1[var2] = this.formatArgs[var2] instanceof IChatComponent ? ((IChatComponent)this.formatArgs[var2]).createCopy() : this.formatArgs[var2];
        }
        ChatComponentTranslation var5 = new ChatComponentTranslation(this.key, var1);
        var5.setChatStyle(this.getChatStyle().createShallowCopy());
        for (IChatComponent var4 : this.getSiblings()) {
            var5.appendSibling(var4.createCopy());
        }
        return var5;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        }
        ChatComponentTranslation var2 = (ChatComponentTranslation)p_equals_1_;
        return Arrays.equals(this.formatArgs, var2.formatArgs) && this.key.equals(var2.key) && super.equals(p_equals_1_);
    }

    @Override
    public int hashCode() {
        int var1 = super.hashCode();
        var1 = 31 * var1 + this.key.hashCode();
        var1 = 31 * var1 + Arrays.hashCode(this.formatArgs);
        return var1;
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

