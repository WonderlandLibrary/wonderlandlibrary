// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;

public class EntityPredicate
{
    public static final EntityPredicate ANY;
    private final ResourceLocation type;
    private final DistancePredicate distance;
    private final LocationPredicate location;
    private final MobEffectsPredicate effects;
    private final NBTPredicate nbt;
    
    public EntityPredicate(@Nullable final ResourceLocation type, final DistancePredicate distance, final LocationPredicate location, final MobEffectsPredicate effects, final NBTPredicate nbt) {
        this.type = type;
        this.distance = distance;
        this.location = location;
        this.effects = effects;
        this.nbt = nbt;
    }
    
    public boolean test(final EntityPlayerMP player, @Nullable final Entity entity) {
        return this == EntityPredicate.ANY || (entity != null && (this.type == null || EntityList.isMatchingName(entity, this.type)) && this.distance.test(player.posX, player.posY, player.posZ, entity.posX, entity.posY, entity.posZ) && this.location.test(player.getServerWorld(), entity.posX, entity.posY, entity.posZ) && this.effects.test(entity) && this.nbt.test(entity));
    }
    
    public static EntityPredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "entity");
            ResourceLocation resourcelocation = null;
            if (jsonobject.has("type")) {
                resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "type"));
                if (!EntityList.isRegistered(resourcelocation)) {
                    throw new JsonSyntaxException("Unknown entity type '" + resourcelocation + "', valid types are: " + EntityList.getValidTypeNames());
                }
            }
            final DistancePredicate distancepredicate = DistancePredicate.deserialize(jsonobject.get("distance"));
            final LocationPredicate locationpredicate = LocationPredicate.deserialize(jsonobject.get("location"));
            final MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.deserialize(jsonobject.get("effects"));
            final NBTPredicate nbtpredicate = NBTPredicate.deserialize(jsonobject.get("nbt"));
            return new EntityPredicate(resourcelocation, distancepredicate, locationpredicate, mobeffectspredicate, nbtpredicate);
        }
        return EntityPredicate.ANY;
    }
    
    static {
        ANY = new EntityPredicate(null, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NBTPredicate.ANY);
    }
}
