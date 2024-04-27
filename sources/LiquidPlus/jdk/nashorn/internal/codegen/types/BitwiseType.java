/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.nashorn.internal.codegen.types.BytecodeBitwiseOps;
import jdk.nashorn.internal.codegen.types.NumericType;

public abstract class BitwiseType
extends NumericType
implements BytecodeBitwiseOps {
    private static final long serialVersionUID = 1L;

    protected BitwiseType(String name, Class<?> clazz, int weight, int slots) {
        super(name, clazz, weight, slots);
    }
}

