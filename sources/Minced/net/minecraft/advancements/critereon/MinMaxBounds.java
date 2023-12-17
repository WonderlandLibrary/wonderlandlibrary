// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;

public class MinMaxBounds
{
    public static final MinMaxBounds UNBOUNDED;
    private final Float min;
    private final Float max;
    
    public MinMaxBounds(@Nullable final Float min, @Nullable final Float max) {
        this.min = min;
        this.max = max;
    }
    
    public boolean test(final float value) {
        return (this.min == null || this.min <= value) && (this.max == null || this.max >= value);
    }
    
    public boolean testSquare(final double value) {
        return (this.min == null || this.min * this.min <= value) && (this.max == null || this.max * this.max >= value);
    }
    
    public static MinMaxBounds deserialize(@Nullable final JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return MinMaxBounds.UNBOUNDED;
        }
        if (JsonUtils.isNumber(element)) {
            final float f2 = JsonUtils.getFloat(element, "value");
            return new MinMaxBounds(f2, f2);
        }
        final JsonObject jsonobject = JsonUtils.getJsonObject(element, "value");
        final Float f3 = jsonobject.has("min") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "min")) : null;
        final Float f4 = jsonobject.has("max") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "max")) : null;
        return new MinMaxBounds(f3, f4);
    }
    
    static {
        UNBOUNDED = new MinMaxBounds(null, null);
    }
}
