package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;

public static class GuiPageButtonList.GuiSlideEntry
extends GuiPageButtonList.GuiListEntry {
    private final GuiSlider.FormatHelper field_178949_a;
    private final float field_178947_b;
    private final float field_178948_c;
    private final float field_178946_d;

    public GuiPageButtonList.GuiSlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, GuiSlider.FormatHelper p_i45530_4_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_) {
        super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
        this.field_178949_a = p_i45530_4_;
        this.field_178947_b = p_i45530_5_;
        this.field_178948_c = p_i45530_6_;
        this.field_178946_d = p_i45530_7_;
    }

    public GuiSlider.FormatHelper func_178945_a() {
        return this.field_178949_a;
    }

    public float func_178943_e() {
        return this.field_178947_b;
    }

    public float func_178944_f() {
        return this.field_178948_c;
    }

    public float func_178942_g() {
        return this.field_178946_d;
    }
}
