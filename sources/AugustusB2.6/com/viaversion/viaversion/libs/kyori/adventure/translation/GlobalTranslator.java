// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.translation;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.Locale;
import com.viaversion.viaversion.libs.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface GlobalTranslator extends Translator, Examinable
{
    @NotNull
    default GlobalTranslator get() {
        return GlobalTranslatorImpl.INSTANCE;
    }
    
    @NotNull
    default TranslatableComponentRenderer<Locale> renderer() {
        return GlobalTranslatorImpl.INSTANCE.renderer;
    }
    
    @NotNull
    default Component render(@NotNull final Component component, @NotNull final Locale locale) {
        return renderer().render(component, locale);
    }
    
    @NotNull
    Iterable<? extends Translator> sources();
    
    boolean addSource(@NotNull final Translator source);
    
    boolean removeSource(@NotNull final Translator source);
}
