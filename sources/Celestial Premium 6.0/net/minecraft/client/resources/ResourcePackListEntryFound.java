/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackRepository;

public class ResourcePackListEntryFound
extends ResourcePackListEntry {
    private final ResourcePackRepository.Entry resourcePackEntry;

    public ResourcePackListEntryFound(GuiScreenResourcePacks resourcePacksGUIIn, ResourcePackRepository.Entry entry) {
        super(resourcePacksGUIIn);
        this.resourcePackEntry = entry;
    }

    @Override
    protected void bindResourcePackIcon() {
        this.resourcePackEntry.bindTexturePackIcon(this.mc.getTextureManager());
    }

    @Override
    protected int getResourcePackFormat() {
        return this.resourcePackEntry.getPackFormat();
    }

    @Override
    protected String getResourcePackDescription() {
        return this.resourcePackEntry.getTexturePackDescription();
    }

    @Override
    protected String getResourcePackName() {
        return this.resourcePackEntry.getResourcePackName();
    }

    public ResourcePackRepository.Entry getResourcePackEntry() {
        return this.resourcePackEntry;
    }
}

