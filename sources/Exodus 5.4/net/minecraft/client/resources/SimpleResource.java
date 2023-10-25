/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 */
package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class SimpleResource
implements IResource {
    private final IMetadataSerializer srMetadataSerializer;
    private final String resourcePackName;
    private boolean mcmetaJsonChecked;
    private final ResourceLocation srResourceLocation;
    private final InputStream mcmetaInputStream;
    private final InputStream resourceInputStream;
    private JsonObject mcmetaJson;
    private final Map<String, IMetadataSection> mapMetadataSections = Maps.newHashMap();

    @Override
    public InputStream getInputStream() {
        return this.resourceInputStream;
    }

    public int hashCode() {
        int n = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
        n = 31 * n + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
        return n;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleResource)) {
            return false;
        }
        SimpleResource simpleResource = (SimpleResource)object;
        if (this.srResourceLocation != null ? !this.srResourceLocation.equals(simpleResource.srResourceLocation) : simpleResource.srResourceLocation != null) {
            return false;
        }
        return !(this.resourcePackName != null ? !this.resourcePackName.equals(simpleResource.resourcePackName) : simpleResource.resourcePackName != null);
    }

    @Override
    public boolean hasMetadata() {
        return this.mcmetaInputStream != null;
    }

    public SimpleResource(String string, ResourceLocation resourceLocation, InputStream inputStream, InputStream inputStream2, IMetadataSerializer iMetadataSerializer) {
        this.resourcePackName = string;
        this.srResourceLocation = resourceLocation;
        this.resourceInputStream = inputStream;
        this.mcmetaInputStream = inputStream2;
        this.srMetadataSerializer = iMetadataSerializer;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.srResourceLocation;
    }

    @Override
    public String getResourcePackName() {
        return this.resourcePackName;
    }

    @Override
    public <T extends IMetadataSection> T getMetadata(String string) {
        Object object;
        if (!this.hasMetadata()) {
            return null;
        }
        if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
            this.mcmetaJsonChecked = true;
            object = null;
            object = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
            this.mcmetaJson = new JsonParser().parse((Reader)object).getAsJsonObject();
            IOUtils.closeQuietly((Reader)object);
        }
        if ((object = this.mapMetadataSections.get(string)) == null) {
            object = this.srMetadataSerializer.parseMetadataSection(string, this.mcmetaJson);
        }
        return (T)object;
    }
}

