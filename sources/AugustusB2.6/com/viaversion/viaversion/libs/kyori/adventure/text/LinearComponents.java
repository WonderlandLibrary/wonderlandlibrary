// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jetbrains.annotations.NotNull;

public final class LinearComponents
{
    private LinearComponents() {
    }
    
    @NotNull
    public static Component linear(@NotNull final ComponentBuilderApplicable... applicables) {
        final int length = applicables.length;
        if (length == 0) {
            return Component.empty();
        }
        if (length == 1) {
            final ComponentBuilderApplicable ap0 = applicables[0];
            if (ap0 instanceof ComponentLike) {
                return ((ComponentLike)ap0).asComponent();
            }
            throw nothingComponentLike();
        }
        else {
            final TextComponentImpl.BuilderImpl builder = new TextComponentImpl.BuilderImpl();
            Style.Builder style = null;
            for (final ComponentBuilderApplicable applicable : applicables) {
                if (applicable instanceof StyleBuilderApplicable) {
                    if (style == null) {
                        style = Style.style();
                    }
                    style.apply((StyleBuilderApplicable)applicable);
                }
                else if (style != null && applicable instanceof ComponentLike) {
                    builder.applicableApply(((ComponentLike)applicable).asComponent().style(style));
                }
                else {
                    builder.applicableApply(applicable);
                }
            }
            final int size = builder.children.size();
            if (size == 0) {
                throw nothingComponentLike();
            }
            if (size == 1) {
                return builder.children.get(0);
            }
            return builder.build();
        }
    }
    
    private static IllegalStateException nothingComponentLike() {
        return new IllegalStateException("Cannot build component linearly - nothing component-like was given");
    }
}
