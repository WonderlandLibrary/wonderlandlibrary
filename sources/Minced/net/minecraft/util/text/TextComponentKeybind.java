// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.function.Function;

public class TextComponentKeybind extends TextComponentBase
{
    public static Function<String, Supplier<String>> displaySupplierFunction;
    private final String keybind;
    private Supplier<String> displaySupplier;
    
    public TextComponentKeybind(final String keybind) {
        this.keybind = keybind;
    }
    
    @Override
    public String getUnformattedComponentText() {
        if (this.displaySupplier == null) {
            this.displaySupplier = TextComponentKeybind.displaySupplierFunction.apply(this.keybind);
        }
        return this.displaySupplier.get();
    }
    
    @Override
    public TextComponentKeybind createCopy() {
        final TextComponentKeybind textcomponentkeybind = new TextComponentKeybind(this.keybind);
        textcomponentkeybind.setStyle(this.getStyle().createShallowCopy());
        for (final ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentkeybind.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentkeybind;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentKeybind)) {
            return false;
        }
        final TextComponentKeybind textcomponentkeybind = (TextComponentKeybind)p_equals_1_;
        return this.keybind.equals(textcomponentkeybind.keybind) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "KeybindComponent{keybind='" + this.keybind + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    public String getKeybind() {
        return this.keybind;
    }
    
    static {
        TextComponentKeybind.displaySupplierFunction = (p_193635_0_ -> () -> p_193635_0_);
    }
}
