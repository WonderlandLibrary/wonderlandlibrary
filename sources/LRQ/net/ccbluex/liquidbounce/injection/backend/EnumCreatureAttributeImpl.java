/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EnumCreatureAttribute
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureAttribute;
import org.jetbrains.annotations.Nullable;

public final class EnumCreatureAttributeImpl
implements IEnumCreatureAttribute {
    private final EnumCreatureAttribute wrapped;

    public boolean equals(@Nullable Object other) {
        return other instanceof EnumCreatureAttributeImpl && ((EnumCreatureAttributeImpl)other).wrapped == this.wrapped;
    }

    public final EnumCreatureAttribute getWrapped() {
        return this.wrapped;
    }

    public EnumCreatureAttributeImpl(EnumCreatureAttribute wrapped) {
        this.wrapped = wrapped;
    }
}

