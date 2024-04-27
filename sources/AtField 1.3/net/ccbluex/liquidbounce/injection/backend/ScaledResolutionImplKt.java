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
    public static final ScaledResolution unwrap(IScaledResolution iScaledResolution) {
        boolean bl = false;
        return ((ScaledResolutionImpl)iScaledResolution).getWrapped();
    }

    public static final IScaledResolution wrap(ScaledResolution scaledResolution) {
        boolean bl = false;
        return new ScaledResolutionImpl(scaledResolution);
    }
}

