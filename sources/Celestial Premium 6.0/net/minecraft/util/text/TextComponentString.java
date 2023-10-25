/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public class TextComponentString
extends TextComponentBase {
    private final String text;

    public TextComponentString(String msg) {
        this.text = msg;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String getUnformattedComponentText() {
        return this.text;
    }

    @Override
    public TextComponentString createCopy() {
        TextComponentString textcomponentstring = new TextComponentString(this.text);
        textcomponentstring.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentstring.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentstring;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentString)) {
            return false;
        }
        TextComponentString textcomponentstring = (TextComponentString)p_equals_1_;
        return this.text.equals(textcomponentstring.getText()) && super.equals(p_equals_1_);
    }

    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}

