package net.minecraft.client.gui;

static enum GuiLockIconButton.Icon {
    LOCKED(0, 146),
    LOCKED_HOVER(0, 166),
    LOCKED_DISABLED(0, 186),
    UNLOCKED(20, 146),
    UNLOCKED_HOVER(20, 166),
    UNLOCKED_DISABLED(20, 186);

    private final int field_178914_g;
    private final int field_178920_h;

    private GuiLockIconButton.Icon(int p_i45537_3_, int p_i45537_4_) {
        this.field_178914_g = p_i45537_3_;
        this.field_178920_h = p_i45537_4_;
    }

    public int func_178910_a() {
        return this.field_178914_g;
    }

    public int func_178912_b() {
        return this.field_178920_h;
    }
}
