package net.minecraft.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkProviderSettings;

static class GuiScreenCustomizePresets.Info {
    public String field_178955_a;
    public ResourceLocation field_178953_b;
    public ChunkProviderSettings.Factory field_178954_c;

    public GuiScreenCustomizePresets.Info(String p_i45523_1_, ResourceLocation p_i45523_2_, ChunkProviderSettings.Factory p_i45523_3_) {
        this.field_178955_a = p_i45523_1_;
        this.field_178953_b = p_i45523_2_;
        this.field_178954_c = p_i45523_3_;
    }
}
