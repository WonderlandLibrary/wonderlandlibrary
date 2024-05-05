/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.injection.backend.AttributeModifierImpl;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public final class AttributeModifierImplKt {
    public static final AttributeModifier unwrap(IAttributeModifier $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((AttributeModifierImpl)$this$unwrap).getWrapped();
    }

    public static final IAttributeModifier wrap(AttributeModifier $this$wrap) {
        int $i$f$wrap = 0;
        return new AttributeModifierImpl($this$wrap);
    }
}

