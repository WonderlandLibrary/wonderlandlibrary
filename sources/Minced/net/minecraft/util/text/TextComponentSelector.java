// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import java.util.Iterator;

public class TextComponentSelector extends TextComponentBase
{
    private final String selector;
    
    public TextComponentSelector(final String selectorIn) {
        this.selector = selectorIn;
    }
    
    public String getSelector() {
        return this.selector;
    }
    
    @Override
    public String getUnformattedComponentText() {
        return this.selector;
    }
    
    @Override
    public TextComponentSelector createCopy() {
        final TextComponentSelector textcomponentselector = new TextComponentSelector(this.selector);
        textcomponentselector.setStyle(this.getStyle().createShallowCopy());
        for (final ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentselector.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentselector;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentSelector)) {
            return false;
        }
        final TextComponentSelector textcomponentselector = (TextComponentSelector)p_equals_1_;
        return this.selector.equals(textcomponentselector.selector) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}
