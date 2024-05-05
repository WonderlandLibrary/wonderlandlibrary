package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiPageButtonList;

public static class GuiPageButtonList.GuiButtonEntry
extends GuiPageButtonList.GuiListEntry {
    private final boolean field_178941_a;

    public GuiPageButtonList.GuiButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_) {
        super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
        this.field_178941_a = p_i45535_4_;
    }

    public boolean func_178940_a() {
        return this.field_178941_a;
    }
}
