/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.RegistrySimple;

public class RegistryDefaulted
extends RegistrySimple {
    private final Object defaultObject;
    private static final String __OBFID = "CL_00001198";

    public RegistryDefaulted(Object p_i1366_1_) {
        this.defaultObject = p_i1366_1_;
    }

    @Override
    public Object getObject(Object p_82594_1_) {
        Object var2 = super.getObject(p_82594_1_);
        return var2 == null ? this.defaultObject : var2;
    }
}

