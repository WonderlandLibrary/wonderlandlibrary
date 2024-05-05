package net.minecraft.client.gui;

public static class GuiPageButtonList.GuiListEntry {
    private final int field_178939_a;
    private final String field_178937_b;
    private final boolean field_178938_c;

    public GuiPageButtonList.GuiListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_) {
        this.field_178939_a = p_i45531_1_;
        this.field_178937_b = p_i45531_2_;
        this.field_178938_c = p_i45531_3_;
    }

    public int func_178935_b() {
        return this.field_178939_a;
    }

    public String func_178936_c() {
        return this.field_178937_b;
    }

    public boolean func_178934_d() {
        return this.field_178938_c;
    }
}
