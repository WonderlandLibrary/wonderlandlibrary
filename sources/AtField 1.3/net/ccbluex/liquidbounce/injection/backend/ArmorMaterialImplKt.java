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
    public static final ItemArmor.ArmorMaterial unwrap(IArmorMaterial iArmorMaterial) {
        boolean bl = false;
        return ((ArmorMaterialImpl)iArmorMaterial).getWrapped();
    }

    public static final IArmorMaterial wrap(ItemArmor.ArmorMaterial armorMaterial) {
        boolean bl = false;
        return new ArmorMaterialImpl(armorMaterial);
    }
}

