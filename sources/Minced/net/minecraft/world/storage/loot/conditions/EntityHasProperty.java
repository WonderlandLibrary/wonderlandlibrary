// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import java.util.Random;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.properties.EntityProperty;

public class EntityHasProperty implements LootCondition
{
    private final EntityProperty[] properties;
    private final LootContext.EntityTarget target;
    
    public EntityHasProperty(final EntityProperty[] propertiesIn, final LootContext.EntityTarget targetIn) {
        this.properties = propertiesIn;
        this.target = targetIn;
    }
    
    @Override
    public boolean testCondition(final Random rand, final LootContext context) {
        final Entity entity = context.getEntity(this.target);
        if (entity == null) {
            return false;
        }
        for (final EntityProperty entityproperty : this.properties) {
            if (!entityproperty.testProperty(rand, entity)) {
                return false;
            }
        }
        return true;
    }
    
    public static class Serializer extends LootCondition.Serializer<EntityHasProperty>
    {
        protected Serializer() {
            super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
        }
        
        @Override
        public void serialize(final JsonObject json, final EntityHasProperty value, final JsonSerializationContext context) {
            final JsonObject jsonobject = new JsonObject();
            for (final EntityProperty entityproperty : value.properties) {
                final EntityProperty.Serializer<EntityProperty> serializer = EntityPropertyManager.getSerializerFor(entityproperty);
                jsonobject.add(serializer.getName().toString(), serializer.serialize(entityproperty, context));
            }
            json.add("properties", (JsonElement)jsonobject);
            json.add("entity", context.serialize((Object)value.target));
        }
        
        @Override
        public EntityHasProperty deserialize(final JsonObject json, final JsonDeserializationContext context) {
            final Set<Map.Entry<String, JsonElement>> set = (Set<Map.Entry<String, JsonElement>>)JsonUtils.getJsonObject(json, "properties").entrySet();
            final EntityProperty[] aentityproperty = new EntityProperty[set.size()];
            int i = 0;
            for (final Map.Entry<String, JsonElement> entry : set) {
                aentityproperty[i++] = (EntityProperty)EntityPropertyManager.getSerializerForName(new ResourceLocation(entry.getKey())).deserialize(entry.getValue(), context);
            }
            return new EntityHasProperty(aentityproperty, JsonUtils.deserializeClass(json, "entity", context, (Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class));
        }
    }
}
