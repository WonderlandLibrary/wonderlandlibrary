package net.minecraft.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;

class IMetadataSerializer.Registration<T extends IMetadataSection> {
    final IMetadataSectionSerializer<T> section;
    final Class<T> clazz;

    private IMetadataSerializer.Registration(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazzToRegister) {
        this.section = metadataSectionSerializer;
        this.clazz = clazzToRegister;
    }
}
