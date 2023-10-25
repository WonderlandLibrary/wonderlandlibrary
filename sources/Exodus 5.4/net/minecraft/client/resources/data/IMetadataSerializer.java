/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.TypeAdapterFactory
 */
package net.minecraft.client.resources.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;

public class IMetadataSerializer {
    private final GsonBuilder gsonBuilder;
    private final IRegistry<String, Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = new RegistrySimple<String, Registration<? extends IMetadataSection>>();
    private Gson gson;

    public <T extends IMetadataSection> T parseMetadataSection(String string, JsonObject jsonObject) {
        if (string == null) {
            throw new IllegalArgumentException("Metadata section name cannot be null");
        }
        if (!jsonObject.has(string)) {
            return null;
        }
        if (!jsonObject.get(string).isJsonObject()) {
            throw new IllegalArgumentException("Invalid metadata for '" + string + "' - expected object, found " + jsonObject.get(string));
        }
        Registration<? extends IMetadataSection> registration = this.metadataSectionSerializerRegistry.getObject(string);
        if (registration == null) {
            throw new IllegalArgumentException("Don't know how to handle metadata section '" + string + "'");
        }
        return (T)((IMetadataSection)this.getGson().fromJson((JsonElement)jsonObject.getAsJsonObject(string), registration.field_110500_b));
    }

    public IMetadataSerializer() {
        this.gsonBuilder = new GsonBuilder();
        this.gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, (Object)new IChatComponent.Serializer());
        this.gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, (Object)new ChatStyle.Serializer());
        this.gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
    }

    private Gson getGson() {
        if (this.gson == null) {
            this.gson = this.gsonBuilder.create();
        }
        return this.gson;
    }

    public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> iMetadataSectionSerializer, Class<T> clazz) {
        this.metadataSectionSerializerRegistry.putObject(iMetadataSectionSerializer.getSectionName(), new Registration(iMetadataSectionSerializer, clazz));
        this.gsonBuilder.registerTypeAdapter(clazz, iMetadataSectionSerializer);
        this.gson = null;
    }

    class Registration<T extends IMetadataSection> {
        final IMetadataSectionSerializer<T> field_110502_a;
        final Class<T> field_110500_b;

        private Registration(IMetadataSectionSerializer<T> iMetadataSectionSerializer, Class<T> clazz) {
            this.field_110502_a = iMetadataSectionSerializer;
            this.field_110500_b = clazz;
        }
    }
}

