// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import net.minecraft.util.math.MathHelper;
import javax.annotation.Nullable;

public class RangedAttribute extends BaseAttribute
{
    private final double minimumValue;
    private final double maximumValue;
    private String description;
    
    public RangedAttribute(@Nullable final IAttribute parentIn, final String unlocalizedNameIn, final double defaultValue, final double minimumValueIn, final double maximumValueIn) {
        super(parentIn, unlocalizedNameIn, defaultValue);
        this.minimumValue = minimumValueIn;
        this.maximumValue = maximumValueIn;
        if (minimumValueIn > maximumValueIn) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        }
        if (defaultValue < minimumValueIn) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        }
        if (defaultValue > maximumValueIn) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }
    
    public RangedAttribute setDescription(final String descriptionIn) {
        this.description = descriptionIn;
        return this;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public double clampValue(double value) {
        value = MathHelper.clamp(value, this.minimumValue, this.maximumValue);
        return value;
    }
}
