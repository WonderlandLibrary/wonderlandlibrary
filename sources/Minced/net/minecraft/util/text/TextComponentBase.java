// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;

public abstract class TextComponentBase implements ITextComponent
{
    protected List<ITextComponent> siblings;
    private Style style;
    
    public TextComponentBase() {
        this.siblings = (List<ITextComponent>)Lists.newArrayList();
    }
    
    @Override
    public ITextComponent appendSibling(final ITextComponent component) {
        component.getStyle().setParentStyle(this.getStyle());
        this.siblings.add(component);
        return this;
    }
    
    @Override
    public List<ITextComponent> getSiblings() {
        return this.siblings;
    }
    
    @Override
    public ITextComponent appendText(final String text) {
        return this.appendSibling(new TextComponentString(text));
    }
    
    @Override
    public ITextComponent setStyle(final Style style) {
        this.style = style;
        for (final ITextComponent itextcomponent : this.siblings) {
            itextcomponent.getStyle().setParentStyle(this.getStyle());
        }
        return this;
    }
    
    @Override
    public Style getStyle() {
        if (this.style == null) {
            this.style = new Style();
            for (final ITextComponent itextcomponent : this.siblings) {
                itextcomponent.getStyle().setParentStyle(this.style);
            }
        }
        return this.style;
    }
    
    @Override
    public Iterator<ITextComponent> iterator() {
        return (Iterator<ITextComponent>)Iterators.concat((Iterator)Iterators.forArray((Object[])new TextComponentBase[] { this }), (Iterator)createDeepCopyIterator(this.siblings));
    }
    
    @Override
    public final String getUnformattedText() {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final ITextComponent itextcomponent : this) {
            stringbuilder.append(itextcomponent.getUnformattedComponentText());
        }
        return stringbuilder.toString();
    }
    
    @Override
    public final String getFormattedText() {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final ITextComponent itextcomponent : this) {
            final String s = itextcomponent.getUnformattedComponentText();
            if (!s.isEmpty()) {
                stringbuilder.append(itextcomponent.getStyle().getFormattingCode());
                stringbuilder.append(s);
                stringbuilder.append(TextFormatting.RESET);
            }
        }
        return stringbuilder.toString();
    }
    
    public static Iterator<ITextComponent> createDeepCopyIterator(final Iterable<ITextComponent> components) {
        Iterator<ITextComponent> iterator = (Iterator<ITextComponent>)Iterators.concat(Iterators.transform((Iterator)components.iterator(), (Function)new Function<ITextComponent, Iterator<ITextComponent>>() {
            public Iterator<ITextComponent> apply(@Nullable final ITextComponent p_apply_1_) {
                return p_apply_1_.iterator();
            }
        }));
        iterator = (Iterator<ITextComponent>)Iterators.transform((Iterator)iterator, (Function)new Function<ITextComponent, ITextComponent>() {
            public ITextComponent apply(@Nullable final ITextComponent p_apply_1_) {
                final ITextComponent itextcomponent = p_apply_1_.createCopy();
                itextcomponent.setStyle(itextcomponent.getStyle().createDeepCopy());
                return itextcomponent;
            }
        });
        return iterator;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentBase)) {
            return false;
        }
        final TextComponentBase textcomponentbase = (TextComponentBase)p_equals_1_;
        return this.siblings.equals(textcomponentbase.siblings) && this.getStyle().equals(textcomponentbase.getStyle());
    }
    
    @Override
    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }
    
    @Override
    public String toString() {
        return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
