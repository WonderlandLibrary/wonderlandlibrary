/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.injection.backend.MaterialImpl;
import net.minecraft.block.material.Material;

public final class MaterialImplKt {
    public static final Material unwrap(IMaterial $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((MaterialImpl)$this$unwrap).getWrapped();
    }

    public static final IMaterial wrap(Material $this$wrap) {
        int $i$f$wrap = 0;
        return new MaterialImpl($this$wrap);
    }
}

