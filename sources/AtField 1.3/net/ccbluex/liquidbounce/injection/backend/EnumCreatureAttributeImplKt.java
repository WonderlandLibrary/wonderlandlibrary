/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EnumCreatureAttribute
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute;
import net.ccbluex.liquidbounce.injection.backend.EnumCreatureAttributeImpl;
import net.minecraft.entity.EnumCreatureAttribute;

public final class EnumCreatureAttributeImplKt {
    public static final IEnumCreatureAttribute wrap(EnumCreatureAttribute enumCreatureAttribute) {
        boolean bl = false;
        return new EnumCreatureAttributeImpl(enumCreatureAttribute);
    }

    public static final EnumCreatureAttribute unwrap(IEnumCreatureAttribute iEnumCreatureAttribute) {
        boolean bl = false;
        return ((EnumCreatureAttributeImpl)iEnumCreatureAttribute).getWrapped();
    }
}

