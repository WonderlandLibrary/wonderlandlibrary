// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.DimensionType;
import javax.annotation.Nullable;
import net.minecraft.world.biome.Biome;

public class LocationPredicate
{
    public static LocationPredicate ANY;
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    @Nullable
    final Biome biome;
    @Nullable
    private final String feature;
    @Nullable
    private final DimensionType dimension;
    
    public LocationPredicate(final MinMaxBounds x, final MinMaxBounds y, final MinMaxBounds z, @Nullable final Biome biome, @Nullable final String feature, @Nullable final DimensionType dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.biome = biome;
        this.feature = feature;
        this.dimension = dimension;
    }
    
    public boolean test(final WorldServer world, final double x, final double y, final double z) {
        return this.test(world, (float)x, (float)y, (float)z);
    }
    
    public boolean test(final WorldServer world, final float x, final float y, final float z) {
        if (!this.x.test(x)) {
            return false;
        }
        if (!this.y.test(y)) {
            return false;
        }
        if (!this.z.test(z)) {
            return false;
        }
        if (this.dimension != null && this.dimension != world.provider.getDimensionType()) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(x, y, z);
        return (this.biome == null || this.biome == world.getBiome(blockpos)) && (this.feature == null || world.getChunkProvider().isInsideStructure(world, this.feature, blockpos));
    }
    
    public static LocationPredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "location");
            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "position", new JsonObject());
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject2.get("x"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(jsonobject2.get("y"));
            final MinMaxBounds minmaxbounds3 = MinMaxBounds.deserialize(jsonobject2.get("z"));
            final DimensionType dimensiontype = jsonobject.has("dimension") ? DimensionType.byName(JsonUtils.getString(jsonobject, "dimension")) : null;
            final String s = jsonobject.has("feature") ? JsonUtils.getString(jsonobject, "feature") : null;
            Biome biome = null;
            if (jsonobject.has("biome")) {
                final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "biome"));
                biome = Biome.REGISTRY.getObject(resourcelocation);
                if (biome == null) {
                    throw new JsonSyntaxException("Unknown biome '" + resourcelocation + "'");
                }
            }
            return new LocationPredicate(minmaxbounds, minmaxbounds2, minmaxbounds3, biome, s, dimensiontype);
        }
        return LocationPredicate.ANY;
    }
    
    static {
        LocationPredicate.ANY = new LocationPredicate(MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, null, null, null);
    }
}
