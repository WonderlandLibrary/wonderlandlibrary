// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.properties;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import java.util.Random;

public class EntityOnFire implements EntityProperty
{
    private final boolean onFire;
    
    public EntityOnFire(final boolean onFireIn) {
        this.onFire = onFireIn;
    }
    
    @Override
    public boolean testProperty(final Random random, final Entity entityIn) {
        return entityIn.isBurning() == this.onFire;
    }
    
    public static class Serializer extends EntityProperty.Serializer<EntityOnFire>
    {
        protected Serializer() {
            super(new ResourceLocation("on_fire"), EntityOnFire.class);
        }
        
        @Override
        public JsonElement serialize(final EntityOnFire property, final JsonSerializationContext serializationContext) {
            return (JsonElement)new JsonPrimitive(Boolean.valueOf(property.onFire));
        }
        
        @Override
        public EntityOnFire deserialize(final JsonElement element, final JsonDeserializationContext deserializationContext) {
            return new EntityOnFire(JsonUtils.getBoolean(element, "on_fire"));
        }
    }
}
