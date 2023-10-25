/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public class TextComponentSelector
extends TextComponentBase {
    private final String selector;

    public TextComponentSelector(String selectorIn) {
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
        TextComponentSelector textcomponentselector = new TextComponentSelector(this.selector);
        textcomponentselector.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentselector.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentselector;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentSelector)) {
            return false;
        }
        TextComponentSelector textcomponentselector = (TextComponentSelector)p_equals_1_;
        return this.selector.equals(textcomponentselector.selector) && super.equals(p_equals_1_);
    }

    @Override
    public String toString() {
        return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}

