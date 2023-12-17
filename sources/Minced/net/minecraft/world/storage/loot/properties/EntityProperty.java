// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import java.util.Random;

public interface EntityProperty
{
    boolean testProperty(final Random p0, final Entity p1);
    
    public abstract static class Serializer<T extends EntityProperty>
    {
        private final ResourceLocation name;
        private final Class<T> propertyClass;
        
        protected Serializer(final ResourceLocation nameIn, final Class<T> propertyClassIn) {
            this.name = nameIn;
            this.propertyClass = propertyClassIn;
        }
        
        public ResourceLocation getName() {
            return this.name;
        }
        
        public Class<T> getPropertyClass() {
            return this.propertyClass;
        }
        
        public abstract JsonElement serialize(final T p0, final JsonSerializationContext p1);
        
        public abstract T deserialize(final JsonElement p0, final JsonDeserializationContext p1);
    }
}
