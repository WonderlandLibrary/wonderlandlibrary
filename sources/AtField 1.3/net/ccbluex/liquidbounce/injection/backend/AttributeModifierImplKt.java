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
    public static final AttributeModifier unwrap(IAttributeModifier iAttributeModifier) {
        boolean bl = false;
        return ((AttributeModifierImpl)iAttributeModifier).getWrapped();
    }

    public static final IAttributeModifier wrap(AttributeModifier attributeModifier) {
        boolean bl = false;
        return new AttributeModifierImpl(attributeModifier);
    }
}

