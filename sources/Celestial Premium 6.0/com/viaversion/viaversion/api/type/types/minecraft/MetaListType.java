/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.ModernMetaListType;

public final class MetaListType
extends ModernMetaListType {
    private final Type<Metadata> type;

    public MetaListType(Type<Metadata> type) {
        Preconditions.checkNotNull(type);
        this.type = type;
    }

    @Override
    protected Type<Metadata> getType() {
        return this.type;
    }
}

