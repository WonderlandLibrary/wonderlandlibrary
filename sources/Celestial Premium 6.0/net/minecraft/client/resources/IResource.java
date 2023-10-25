/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.InputStream;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public interface IResource
extends Closeable {
    public ResourceLocation getResourceLocation();

    public InputStream getInputStream();

    public boolean hasMetadata();

    @Nullable
    public <T extends IMetadataSection> T getMetadata(String var1);

    public String getResourcePackName();
}

