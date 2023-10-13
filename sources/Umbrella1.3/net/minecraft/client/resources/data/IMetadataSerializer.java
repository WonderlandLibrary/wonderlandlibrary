/*
 * Decompiled with CFR 0.150.
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
import java.lang.reflect.Type;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;

public class IMetadataSerializer {
    private final IRegistry metadataSectionSerializerRegistry = new RegistrySimple();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;
    private static final String __OBFID = "CL_00001101";

    public IMetadataSerializer() {
        this.gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, (Object)new IChatComponent.Serializer());
        this.gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, (Object)new ChatStyle.Serializer());
        this.gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
    }

    public void registerMetadataSectionType(IMetadataSectionSerializer p_110504_1_, Class p_110504_2_) {
        this.metadataSectionSerializerRegistry.putObject(p_110504_1_.getSectionName(), new Registration(p_110504_1_, p_110504_2_, null));
        this.gsonBuilder.registerTypeAdapter((Type)p_110504_2_, (Object)p_110504_1_);
        this.gson = null;
    }

    public IMetadataSection parseMetadataSection(String p_110503_1_, JsonObject p_110503_2_) {
        if (p_110503_1_ == null) {
            throw new IllegalArgumentException("Metadata section name cannot be null");
        }
        if (!p_110503_2_.has(p_110503_1_)) {
            return null;
        }
        if (!p_110503_2_.get(p_110503_1_).isJsonObject()) {
            throw new IllegalArgumentException("Invalid metadata for '" + p_110503_1_ + "' - expected object, found " + (Object)p_110503_2_.get(p_110503_1_));
        }
        Registration var3 = (Registration)this.metadataSectionSerializerRegistry.getObject(p_110503_1_);
        if (var3 == null) {
            throw new IllegalArgumentException("Don't know how to handle metadata section '" + p_110503_1_ + "'");
        }
        return (IMetadataSection)this.getGson().fromJson((JsonElement)p_110503_2_.getAsJsonObject(p_110503_1_), var3.field_110500_b);
    }

    private Gson getGson() {
        if (this.gson == null) {
            this.gson = this.gsonBuilder.create();
        }
        return this.gson;
    }

    class Registration {
        final IMetadataSectionSerializer field_110502_a;
        final Class field_110500_b;
        private static final String __OBFID = "CL_00001103";

        private Registration(IMetadataSectionSerializer p_i1305_2_, Class p_i1305_3_) {
            this.field_110502_a = p_i1305_2_;
            this.field_110500_b = p_i1305_3_;
        }

        Registration(IMetadataSectionSerializer p_i1306_2_, Class p_i1306_3_, Object p_i1306_4_) {
            this(p_i1306_2_, p_i1306_3_);
        }
    }
}

