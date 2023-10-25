/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.properties.EntityProperty;

public class EntityOnFire
implements EntityProperty {
    private final boolean onFire;

    public EntityOnFire(boolean onFireIn) {
        this.onFire = onFireIn;
    }

    @Override
    public boolean testProperty(Random random, Entity entityIn) {
        return entityIn.isBurning() == this.onFire;
    }

    public static class Serializer
    extends EntityProperty.Serializer<EntityOnFire> {
        protected Serializer() {
            super(new ResourceLocation("on_fire"), EntityOnFire.class);
        }

        @Override
        public JsonElement serialize(EntityOnFire property, JsonSerializationContext serializationContext) {
            return new JsonPrimitive(property.onFire);
        }

        @Override
        public EntityOnFire deserialize(JsonElement element, JsonDeserializationContext deserializationContext) {
            return new EntityOnFire(JsonUtils.getBoolean(element, "on_fire"));
        }
    }
}

