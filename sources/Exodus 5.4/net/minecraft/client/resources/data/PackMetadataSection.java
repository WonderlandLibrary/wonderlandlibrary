/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.IChatComponent;

public class PackMetadataSection
implements IMetadataSection {
    private final IChatComponent packDescription;
    private final int packFormat;

    public int getPackFormat() {
        return this.packFormat;
    }

    public PackMetadataSection(IChatComponent iChatComponent, int n) {
        this.packDescription = iChatComponent;
        this.packFormat = n;
    }

    public IChatComponent getPackDescription() {
        return this.packDescription;
    }
}

