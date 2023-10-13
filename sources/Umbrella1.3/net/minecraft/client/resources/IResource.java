/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

public interface IResource {
    public ResourceLocation func_177241_a();

    public InputStream getInputStream();

    public boolean hasMetadata();

    public IMetadataSection getMetadata(String var1);

    public String func_177240_d();
}

