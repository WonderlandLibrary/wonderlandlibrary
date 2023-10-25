/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;

public abstract class BaseAttribute
implements IAttribute {
    private final IAttribute field_180373_a;
    private final String unlocalizedName;
    private boolean shouldWatch;
    private final double defaultValue;

    @Override
    public IAttribute func_180372_d() {
        return this.field_180373_a;
    }

    @Override
    public double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean getShouldWatch() {
        return this.shouldWatch;
    }

    @Override
    public String getAttributeUnlocalizedName() {
        return this.unlocalizedName;
    }

    public boolean equals(Object object) {
        return object instanceof IAttribute && this.unlocalizedName.equals(((IAttribute)object).getAttributeUnlocalizedName());
    }

    protected BaseAttribute(IAttribute iAttribute, String string, double d) {
        this.field_180373_a = iAttribute;
        this.unlocalizedName = string;
        this.defaultValue = d;
        if (string == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }

    public int hashCode() {
        return this.unlocalizedName.hashCode();
    }

    public BaseAttribute setShouldWatch(boolean bl) {
        this.shouldWatch = bl;
        return this;
    }
}

