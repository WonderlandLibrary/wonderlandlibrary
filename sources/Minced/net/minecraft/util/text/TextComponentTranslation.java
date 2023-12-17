// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import java.util.Arrays;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.IllegalFormatException;
import net.minecraft.util.text.translation.I18n;
import com.google.common.collect.Lists;
import java.util.regex.Pattern;
import com.google.common.annotations.VisibleForTesting;
import java.util.List;

public class TextComponentTranslation extends TextComponentBase
{
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock;
    private long lastTranslationUpdateTimeInMilliseconds;
    @VisibleForTesting
    List<ITextComponent> children;
    public static final Pattern STRING_VARIABLE_PATTERN;
    
    public TextComponentTranslation(final String translationKey, final Object... args) {
        this.syncLock = new Object();
        this.lastTranslationUpdateTimeInMilliseconds = -1L;
        this.children = (List<ITextComponent>)Lists.newArrayList();
        this.key = translationKey;
        this.formatArgs = args;
        for (final Object object : args) {
            if (object instanceof ITextComponent) {
                ((ITextComponent)object).getStyle().setParentStyle(this.getStyle());
            }
        }
    }
    
    @VisibleForTesting
    synchronized void ensureInitialized() {
        synchronized (this.syncLock) {
            final long i = I18n.getLastTranslationUpdateTimeInMilliseconds();
            if (i == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }
            this.lastTranslationUpdateTimeInMilliseconds = i;
            this.children.clear();
        }
        try {
            this.initializeFromFormat(I18n.translateToLocal(this.key));
        }
        catch (TextComponentTranslationFormatException textcomponenttranslationformatexception) {
            this.children.clear();
            try {
                this.initializeFromFormat(I18n.translateToFallback(this.key));
            }
            catch (TextComponentTranslationFormatException var5) {
                throw textcomponenttranslationformatexception;
            }
        }
    }
    
    protected void initializeFromFormat(final String format) {
        final boolean flag = false;
        final Matcher matcher = TextComponentTranslation.STRING_VARIABLE_PATTERN.matcher(format);
        int i = 0;
        int j = 0;
        try {
            while (matcher.find(j)) {
                final int k = matcher.start();
                final int l = matcher.end();
                if (k > j) {
                    final TextComponentString textcomponentstring = new TextComponentString(String.format(format.substring(j, k), new Object[0]));
                    textcomponentstring.getStyle().setParentStyle(this.getStyle());
                    this.children.add(textcomponentstring);
                }
                final String s2 = matcher.group(2);
                final String s3 = format.substring(k, l);
                if ("%".equals(s2) && "%%".equals(s3)) {
                    final TextComponentString textcomponentstring2 = new TextComponentString("%");
                    textcomponentstring2.getStyle().setParentStyle(this.getStyle());
                    this.children.add(textcomponentstring2);
                }
                else {
                    if (!"s".equals(s2)) {
                        throw new TextComponentTranslationFormatException(this, "Unsupported format: '" + s3 + "'");
                    }
                    final String s4 = matcher.group(1);
                    final int i2 = (s4 != null) ? (Integer.parseInt(s4) - 1) : i++;
                    if (i2 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i2));
                    }
                }
                j = l;
            }
            if (j < format.length()) {
                final TextComponentString textcomponentstring3 = new TextComponentString(String.format(format.substring(j), new Object[0]));
                textcomponentstring3.getStyle().setParentStyle(this.getStyle());
                this.children.add(textcomponentstring3);
            }
        }
        catch (IllegalFormatException illegalformatexception) {
            throw new TextComponentTranslationFormatException(this, illegalformatexception);
        }
    }
    
    private ITextComponent getFormatArgumentAsComponent(final int index) {
        if (index >= this.formatArgs.length) {
            throw new TextComponentTranslationFormatException(this, index);
        }
        final Object object = this.formatArgs[index];
        ITextComponent itextcomponent;
        if (object instanceof ITextComponent) {
            itextcomponent = (ITextComponent)object;
        }
        else {
            itextcomponent = new TextComponentString((object == null) ? "null" : object.toString());
            itextcomponent.getStyle().setParentStyle(this.getStyle());
        }
        return itextcomponent;
    }
    
    @Override
    public ITextComponent setStyle(final Style style) {
        super.setStyle(style);
        for (final Object object : this.formatArgs) {
            if (object instanceof ITextComponent) {
                ((ITextComponent)object).getStyle().setParentStyle(this.getStyle());
            }
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (final ITextComponent itextcomponent : this.children) {
                itextcomponent.getStyle().setParentStyle(style);
            }
        }
        return this;
    }
    
    @Override
    public Iterator<ITextComponent> iterator() {
        this.ensureInitialized();
        return (Iterator<ITextComponent>)Iterators.concat((Iterator)TextComponentBase.createDeepCopyIterator(this.children), (Iterator)TextComponentBase.createDeepCopyIterator(this.siblings));
    }
    
    @Override
    public String getUnformattedComponentText() {
        this.ensureInitialized();
        final StringBuilder stringbuilder = new StringBuilder();
        for (final ITextComponent itextcomponent : this.children) {
            stringbuilder.append(itextcomponent.getUnformattedComponentText());
        }
        return stringbuilder.toString();
    }
    
    @Override
    public TextComponentTranslation createCopy() {
        final Object[] aobject = new Object[this.formatArgs.length];
        for (int i = 0; i < this.formatArgs.length; ++i) {
            if (this.formatArgs[i] instanceof ITextComponent) {
                aobject[i] = ((ITextComponent)this.formatArgs[i]).createCopy();
            }
            else {
                aobject[i] = this.formatArgs[i];
            }
        }
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(this.key, aobject);
        textcomponenttranslation.setStyle(this.getStyle().createShallowCopy());
        for (final ITextComponent itextcomponent : this.getSiblings()) {
            textcomponenttranslation.appendSibling(itextcomponent.createCopy());
        }
        return textcomponenttranslation;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentTranslation)) {
            return false;
        }
        final TextComponentTranslation textcomponenttranslation = (TextComponentTranslation)p_equals_1_;
        return Arrays.equals(this.formatArgs, textcomponenttranslation.formatArgs) && this.key.equals(textcomponenttranslation.key) && super.equals(p_equals_1_);
    }
    
    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.formatArgs);
        return i;
    }
    
    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
    
    static {
        STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    }
}
