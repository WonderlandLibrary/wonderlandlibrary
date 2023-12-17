// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.registry.RegistrySimple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.registry.IRegistry;

public class MetadataSerializer
{
    private final IRegistry<String, Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry;
    private final GsonBuilder gsonBuilder;
    private Gson gson;
    
    public MetadataSerializer() {
        this.metadataSectionSerializerRegistry = new RegistrySimple<String, Registration<? extends IMetadataSection>>();
        (this.gsonBuilder = new GsonBuilder()).registerTypeHierarchyAdapter((Class)ITextComponent.class, (Object)new ITextComponent.Serializer());
        this.gsonBuilder.registerTypeHierarchyAdapter((Class)Style.class, (Object)new Style.Serializer());
        this.gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
    }
    
    public <T extends IMetadataSection> void registerMetadataSectionType(final IMetadataSectionSerializer<T> metadataSectionSerializer, final Class<T> clazz) {
        this.metadataSectionSerializerRegistry.putObject(metadataSectionSerializer.getSectionName(), new Registration<IMetadataSection>((IMetadataSectionSerializer)metadataSectionSerializer, (Class)clazz));
        this.gsonBuilder.registerTypeAdapter((Type)clazz, (Object)metadataSectionSerializer);
        this.gson = null;
    }
    
    public <T extends IMetadataSection> T parseMetadataSection(final String sectionName, final JsonObject json) {
        if (sectionName == null) {
            throw new IllegalArgumentException("Metadata section name cannot be null");
        }
        if (!json.has(sectionName)) {
            return null;
        }
        if (!json.get(sectionName).isJsonObject()) {
            throw new IllegalArgumentException("Invalid metadata for '" + sectionName + "' - expected object, found " + json.get(sectionName));
        }
        final Registration<?> registration = this.metadataSectionSerializerRegistry.getObject(sectionName);
        if (registration == null) {
            throw new IllegalArgumentException("Don't know how to handle metadata section '" + sectionName + "'");
        }
        return (T)this.getGson().fromJson((JsonElement)json.getAsJsonObject(sectionName), (Class)registration.clazz);
    }
    
    private Gson getGson() {
        if (this.gson == null) {
            this.gson = this.gsonBuilder.create();
        }
        return this.gson;
    }
    
    class Registration<T extends IMetadataSection>
    {
        final IMetadataSectionSerializer<T> section;
        final Class<T> clazz;
        
        private Registration(final IMetadataSectionSerializer<T> metadataSectionSerializer, final Class<T> clazzToRegister) {
            this.section = metadataSectionSerializer;
            this.clazz = clazzToRegister;
        }
    }
}
