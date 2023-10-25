/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslationFormatException;
import net.minecraft.util.text.translation.I18n;

public class TextComponentTranslation
extends TextComponentBase {
    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    @VisibleForTesting
    List<ITextComponent> children = Lists.newArrayList();
    public static final Pattern STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TextComponentTranslation(String translationKey, Object ... args) {
        this.key = translationKey;
        this.formatArgs = args;
        for (Object object : args) {
            if (!(object instanceof ITextComponent)) continue;
            ((ITextComponent)object).getStyle().setParentStyle(this.getStyle());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @VisibleForTesting
    synchronized void ensureInitialized() {
        Object object = this.syncLock;
        synchronized (object) {
            long i = I18n.getLastTranslationUpdateTimeInMilliseconds();
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

    protected void initializeFromFormat(String format) {
        boolean flag = false;
        Matcher matcher = STRING_VARIABLE_PATTERN.matcher(format);
        int i = 0;
        int j = 0;
        try {
            while (matcher.find(j)) {
                int k = matcher.start();
                int l = matcher.end();
                if (k > j) {
                    TextComponentString textcomponentstring = new TextComponentString(String.format(format.substring(j, k), new Object[0]));
                    textcomponentstring.getStyle().setParentStyle(this.getStyle());
                    this.children.add(textcomponentstring);
                }
                String s2 = matcher.group(2);
                String s = format.substring(k, l);
                if ("%".equals(s2) && "%%".equals(s)) {
                    TextComponentString textcomponentstring2 = new TextComponentString("%");
                    textcomponentstring2.getStyle().setParentStyle(this.getStyle());
                    this.children.add(textcomponentstring2);
                } else {
                    int i1;
                    if (!"s".equals(s2)) {
                        throw new TextComponentTranslationFormatException(this, "Unsupported format: '" + s + "'");
                    }
                    String s1 = matcher.group(1);
                    int n = i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;
                    if (i1 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i1));
                    }
                }
                j = l;
            }
            if (j < format.length()) {
                TextComponentString textcomponentstring1 = new TextComponentString(String.format(format.substring(j), new Object[0]));
                textcomponentstring1.getStyle().setParentStyle(this.getStyle());
                this.children.add(textcomponentstring1);
            }
        }
        catch (IllegalFormatException illegalformatexception) {
            throw new TextComponentTranslationFormatException(this, (Throwable)illegalformatexception);
        }
    }

    private ITextComponent getFormatArgumentAsComponent(int index) {
        ITextComponent itextcomponent;
        if (index >= this.formatArgs.length) {
            throw new TextComponentTranslationFormatException(this, index);
        }
        Object object = this.formatArgs[index];
        if (object instanceof ITextComponent) {
            itextcomponent = (ITextComponent)object;
        } else {
            itextcomponent = new TextComponentString(object == null ? "null" : object.toString());
            itextcomponent.getStyle().setParentStyle(this.getStyle());
        }
        return itextcomponent;
    }

    @Override
    public ITextComponent setStyle(Style style) {
        super.setStyle(style);
        for (Object object : this.formatArgs) {
            if (!(object instanceof ITextComponent)) continue;
            ((ITextComponent)object).getStyle().setParentStyle(this.getStyle());
        }
        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (ITextComponent itextcomponent : this.children) {
                itextcomponent.getStyle().setParentStyle(style);
            }
        }
        return this;
    }

    @Override
    public Iterator<ITextComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat(TextComponentTranslation.createDeepCopyIterator(this.children), TextComponentTranslation.createDeepCopyIterator(this.siblings));
    }

    @Override
    public String getUnformattedComponentText() {
        this.ensureInitialized();
        StringBuilder stringbuilder = new StringBuilder();
        for (ITextComponent itextcomponent : this.children) {
            stringbuilder.append(itextcomponent.getUnformattedComponentText());
        }
        return stringbuilder.toString();
    }

    @Override
    public TextComponentTranslation createCopy() {
        Object[] aobject = new Object[this.formatArgs.length];
        for (int i = 0; i < this.formatArgs.length; ++i) {
            aobject[i] = this.formatArgs[i] instanceof ITextComponent ? ((ITextComponent)this.formatArgs[i]).createCopy() : this.formatArgs[i];
        }
        TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(this.key, aobject);
        textcomponenttranslation.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent itextcomponent : this.getSiblings()) {
            textcomponenttranslation.appendSibling(itextcomponent.createCopy());
        }
        return textcomponenttranslation;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentTranslation)) {
            return false;
        }
        TextComponentTranslation textcomponenttranslation = (TextComponentTranslation)p_equals_1_;
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
}

