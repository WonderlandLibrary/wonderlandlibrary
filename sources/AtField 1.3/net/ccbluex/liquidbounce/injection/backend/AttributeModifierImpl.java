/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public final class AttributeModifierImpl
implements IAttributeModifier {
    private final AttributeModifier wrapped;

    @Override
    public double getAmount() {
        return this.wrapped.func_111164_d();
    }

    public final AttributeModifier getWrapped() {
        return this.wrapped;
    }

    public AttributeModifierImpl(AttributeModifier attributeModifier) {
        this.wrapped = attributeModifier;
    }
}

