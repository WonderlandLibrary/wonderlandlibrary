/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import net.ccbluex.liquidbounce.injection.backend.ArmorMaterialImpl;
import net.minecraft.item.ItemArmor;

public final class ArmorMaterialImplKt {
    public static final ItemArmor.ArmorMaterial unwrap(IArmorMaterial $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ArmorMaterialImpl)$this$unwrap).getWrapped();
    }

    public static final IArmorMaterial wrap(ItemArmor.ArmorMaterial $this$wrap) {
        int $i$f$wrap = 0;
        return new ArmorMaterialImpl($this$wrap);
    }
}

