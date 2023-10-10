// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface TextDecorationAndState extends Examinable, StyleBuilderApplicable
{
    @NotNull
    TextDecoration decoration();
    
    TextDecoration.State state();
    
    default void styleApply(final Style.Builder style) {
        style.decoration(this.decoration(), this.state());
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("decoration", this.decoration()), ExaminableProperty.of("state", this.state()) });
    }
}
