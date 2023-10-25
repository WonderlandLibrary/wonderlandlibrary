/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackRepository;

public class ResourcePackListEntryFound
extends ResourcePackListEntry {
    private final ResourcePackRepository.Entry field_148319_c;

    public ResourcePackRepository.Entry func_148318_i() {
        return this.field_148319_c;
    }

    @Override
    protected String func_148311_a() {
        return this.field_148319_c.getTexturePackDescription();
    }

    @Override
    protected String func_148312_b() {
        return this.field_148319_c.getResourcePackName();
    }

    public ResourcePackListEntryFound(GuiScreenResourcePacks guiScreenResourcePacks, ResourcePackRepository.Entry entry) {
        super(guiScreenResourcePacks);
        this.field_148319_c = entry;
    }

    @Override
    protected int func_183019_a() {
        return this.field_148319_c.func_183027_f();
    }

    @Override
    protected void func_148313_c() {
        this.field_148319_c.bindTexturePackIcon(this.mc.getTextureManager());
    }
}

