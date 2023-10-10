// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.util.Nag;

final class LegacyFormattingDetected extends Nag
{
    LegacyFormattingDetected(final Component component) {
        super("Legacy formatting codes have been detected in a component - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.adventure.kyori.net) for more information. Component: " + component);
    }
}
