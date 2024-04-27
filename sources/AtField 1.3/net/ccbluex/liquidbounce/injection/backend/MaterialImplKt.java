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
    public static final Material unwrap(IMaterial iMaterial) {
        boolean bl = false;
        return ((MaterialImpl)iMaterial).getWrapped();
    }

    public static final IMaterial wrap(Material material) {
        boolean bl = false;
        return new MaterialImpl(material);
    }
}

