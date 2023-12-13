// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.minecraft.client.resources.data.IMetadataSection;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class SimpleResource implements IResource
{
    private final Map mapMetadataSections;
    private final String field_177242_b;
    private final ResourceLocation srResourceLocation;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;
    private static final String __OBFID = "CL_00001093";
    
    public SimpleResource(final String p_i46090_1_, final ResourceLocation p_i46090_2_, final InputStream p_i46090_3_, final InputStream p_i46090_4_, final IMetadataSerializer p_i46090_5_) {
        this.mapMetadataSections = Maps.newHashMap();
        this.field_177242_b = p_i46090_1_;
        this.srResourceLocation = p_i46090_2_;
        this.resourceInputStream = p_i46090_3_;
        this.mcmetaInputStream = p_i46090_4_;
        this.srMetadataSerializer = p_i46090_5_;
    }
    
    @Override
    public ResourceLocation func_177241_a() {
        return this.srResourceLocation;
    }
    
    @Override
    public InputStream getInputStream() {
        return this.resourceInputStream;
    }
    
    @Override
    public boolean hasMetadata() {
        return this.mcmetaInputStream != null;
    }
    
    @Override
    public IMetadataSection getMetadata(final String p_110526_1_) {
        if (!this.hasMetadata()) {
            return null;
        }
        if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
            this.mcmetaJsonChecked = true;
            BufferedReader var2 = null;
            try {
                var2 = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                this.mcmetaJson = new JsonParser().parse((Reader)var2).getAsJsonObject();
            }
            finally {
                IOUtils.closeQuietly((Reader)var2);
            }
        }
        IMetadataSection var3 = this.mapMetadataSections.get(p_110526_1_);
        if (var3 == null) {
            var3 = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
        }
        return var3;
    }
    
    @Override
    public String func_177240_d() {
        return this.field_177242_b;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof SimpleResource)) {
            return false;
        }
        final SimpleResource var2 = (SimpleResource)p_equals_1_;
        if (this.srResourceLocation != null) {
            if (!this.srResourceLocation.equals(var2.srResourceLocation)) {
                return false;
            }
        }
        else if (var2.srResourceLocation != null) {
            return false;
        }
        if (this.field_177242_b != null) {
            if (!this.field_177242_b.equals(var2.field_177242_b)) {
                return false;
            }
        }
        else if (var2.field_177242_b != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int var1 = (this.field_177242_b != null) ? this.field_177242_b.hashCode() : 0;
        var1 = 31 * var1 + ((this.srResourceLocation != null) ? this.srResourceLocation.hashCode() : 0);
        return var1;
    }
}
