/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.injection.backend.ScaledResolutionImpl;
import net.minecraft.client.gui.ScaledResolution;

public final class ScaledResolutionImplKt {
    public static final ScaledResolution unwrap(IScaledResolution $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ScaledResolutionImpl)$this$unwrap).getWrapped();
    }

    public static final IScaledResolution wrap(ScaledResolution $this$wrap) {
        int $i$f$wrap = 0;
        return new ScaledResolutionImpl($this$wrap);
    }
}

