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
    public static final EnumCreatureAttribute unwrap(IEnumCreatureAttribute $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((EnumCreatureAttributeImpl)$this$unwrap).getWrapped();
    }

    public static final IEnumCreatureAttribute wrap(EnumCreatureAttribute $this$wrap) {
        int $i$f$wrap = 0;
        return new EnumCreatureAttributeImpl($this$wrap);
    }
}

