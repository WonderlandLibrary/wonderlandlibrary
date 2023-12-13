// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Arrays;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.IllegalFormatException;
import com.google.common.collect.Lists;
import java.util.regex.Pattern;
import java.util.List;

public class ChatComponentTranslation extends ChatComponentStyle
{
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock;
    private long lastTranslationUpdateTimeInMilliseconds;
    List children;
    public static final Pattern stringVariablePattern;
    private static final String __OBFID = "CL_00001270";
    
    public ChatComponentTranslation(final String translationKey, final Object... args) {
        this.syncLock = new Object();
        this.lastTranslationUpdateTimeInMilliseconds = -1L;
        this.children = Lists.newArrayList();
        this.key = translationKey;
        this.formatArgs = args;
        for (final Object var6 : args) {
            if (var6 instanceof IChatComponent) {
                ((IChatComponent)var6).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
    }
    
    synchronized void ensureInitialized() {
        final Object var1 = this.syncLock;
        synchronized (this.syncLock) {
            final long var2 = StatCollector.getLastTranslationUpdateTimeInMilliseconds();
            if (var2 == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = var2;
            this.children.clear();
        }
        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
        }
        catch (ChatComponentTranslationFormatException var3) {
            this.children.clear();
            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            }
            catch (ChatComponentTranslationFormatException var4) {
                throw var3;
            }
        }
    }
    
    protected void initializeFromFormat(final String format) {
        final boolean var2 = false;
        final Matcher var3 = ChatComponentTranslation.stringVariablePattern.matcher(format);
        int var4 = 0;
        int var5 = 0;
        try {
            while (var3.find(var5)) {
                final int var6 = var3.start();
                final int var7 = var3.end();
                if (var6 > var5) {
                    final ChatComponentText var8 = new ChatComponentText(String.format(format.substring(var5, var6), new Object[0]));
                    var8.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(var8);
                }
                final String var9 = var3.group(2);
                final String var10 = format.substring(var6, var7);
                if ("%".equals(var9) && "%%".equals(var10)) {
                    final ChatComponentText var11 = new ChatComponentText("%");
                    var11.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(var11);
                }
                else {
                    if (!"s".equals(var9)) {
                        throw new ChatComponentTranslationFormatException(this, "Unsupported format: '" + var10 + "'");
                    }
                    final String var12 = var3.group(1);
                    final int var13 = (var12 != null) ? (Integer.parseInt(var12) - 1) : var4++;
                    if (var13 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(var13));
                    }
                }
                var5 = var7;
            }
            if (var5 < format.length()) {
                final ChatComponentText var14 = new ChatComponentText(String.format(format.substring(var5), new Object[0]));
                var14.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(var14);
            }
        }
        catch (IllegalFormatException var15) {
            throw new ChatComponentTranslationFormatException(this, var15);
        }
    }
    
    private IChatComponent getFormatArgumentAsComponent(final int index) {
        if (index >= this.formatArgs.length) {
            throw new ChatComponentTranslationFormatException(this, index);
        }
        final Object var2 = this.formatArgs[index];
        Object var3;
        if (var2 instanceof IChatComponent) {
            var3 = var2;
        }
        else {
            var3 = new ChatComponentText((var2 == null) ? "null" : var2.toString());
            ((IChatComponent)var3).getChatStyle().setParentStyle(this.getChatStyle());
        }
        return (IChatComponent)var3;
    }
    
    @Override
    public IChatComponent setChatStyle(final ChatStyle style) {
        super.setChatStyle(style);
        for (final Object var5 : this.formatArgs) {
            if (var5 instanceof IChatComponent) {
                ((IChatComponent)var5).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (final IChatComponent var7 : this.children) {
                var7.getChatStyle().setParentStyle(style);
            }
        }
        return this;
    }
    
    @Override
    public Iterator iterator() {
        this.ensureInitialized();
        return Iterators.concat(ChatComponentStyle.createDeepCopyIterator(this.children), ChatComponentStyle.createDeepCopyIterator(this.siblings));
    }
    
    @Override
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        final StringBuilder var1 = new StringBuilder();
        for (final IChatComponent var3 : this.children) {
            var1.append(var3.getUnformattedTextForChat());
        }
        return var1.toString();
    }
    
    @Override
    public ChatComponentTranslation createCopy() {
        final Object[] var1 = new Object[this.formatArgs.length];
        for (int var2 = 0; var2 < this.formatArgs.length; ++var2) {
            if (this.formatArgs[var2] instanceof IChatComponent) {
                var1[var2] = ((IChatComponent)this.formatArgs[var2]).createCopy();
            }
            else {
                var1[var2] = this.formatArgs[var2];
            }
        }
        final ChatComponentTranslation var3 = new ChatComponentTranslation(this.key, var1);
        var3.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent var5 : this.getSiblings()) {
            var3.appendSibling(var5.createCopy());
        }
        return var3;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentTranslation)) {
            return false;
        }
        final ChatComponentTranslation var2 = (ChatComponentTranslation)p_equals_1_;
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
    
    static {
        stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    }
}
