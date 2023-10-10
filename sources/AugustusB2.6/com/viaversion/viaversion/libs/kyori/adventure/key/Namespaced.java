// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

public interface Namespaced
{
    @NotNull
    @Pattern("[a-z0-9_\\-.]+")
    String namespace();
}
