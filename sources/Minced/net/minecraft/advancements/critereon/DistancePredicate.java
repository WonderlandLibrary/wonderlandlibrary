// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate
{
    public static final DistancePredicate ANY;
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    private final MinMaxBounds horizontal;
    private final MinMaxBounds absolute;
    
    public DistancePredicate(final MinMaxBounds x, final MinMaxBounds y, final MinMaxBounds z, final MinMaxBounds horizontal, final MinMaxBounds absolute) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.horizontal = horizontal;
        this.absolute = absolute;
    }
    
    public boolean test(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final float f = (float)(x1 - x2);
        final float f2 = (float)(y1 - y2);
        final float f3 = (float)(z1 - z2);
        return this.x.test(MathHelper.abs(f)) && this.y.test(MathHelper.abs(f2)) && this.z.test(MathHelper.abs(f3)) && this.horizontal.testSquare(f * f + f3 * f3) && this.absolute.testSquare(f * f + f2 * f2 + f3 * f3);
    }
    
    public static DistancePredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "distance");
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject.get("x"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(jsonobject.get("y"));
            final MinMaxBounds minmaxbounds3 = MinMaxBounds.deserialize(jsonobject.get("z"));
            final MinMaxBounds minmaxbounds4 = MinMaxBounds.deserialize(jsonobject.get("horizontal"));
            final MinMaxBounds minmaxbounds5 = MinMaxBounds.deserialize(jsonobject.get("absolute"));
            return new DistancePredicate(minmaxbounds, minmaxbounds2, minmaxbounds3, minmaxbounds4, minmaxbounds5);
        }
        return DistancePredicate.ANY;
    }
    
    static {
        ANY = new DistancePredicate(MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED);
    }
}
