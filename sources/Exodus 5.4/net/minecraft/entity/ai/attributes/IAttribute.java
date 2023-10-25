/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai.attributes;

public interface IAttribute {
    public IAttribute func_180372_d();

    public boolean getShouldWatch();

    public double getDefaultValue();

    public double clampValue(double var1);

    public String getAttributeUnlocalizedName();
}

