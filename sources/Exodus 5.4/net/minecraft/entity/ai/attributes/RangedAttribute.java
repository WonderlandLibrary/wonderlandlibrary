/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai.attributes;

import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.MathHelper;

public class RangedAttribute
extends BaseAttribute {
    private final double maximumValue;
    private final double minimumValue;
    private String description;

    public RangedAttribute(IAttribute iAttribute, String string, double d, double d2, double d3) {
        super(iAttribute, string, d);
        this.minimumValue = d2;
        this.maximumValue = d3;
        if (d2 > d3) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        }
        if (d < d2) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        }
        if (d > d3) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public double clampValue(double d) {
        d = MathHelper.clamp_double(d, this.minimumValue, this.maximumValue);
        return d;
    }

    public RangedAttribute setDescription(String string) {
        this.description = string;
        return this;
    }
}

