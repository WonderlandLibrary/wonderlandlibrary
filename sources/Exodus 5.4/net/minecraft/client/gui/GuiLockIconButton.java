/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton
extends GuiButton {
    private boolean field_175231_o = false;

    public boolean func_175230_c() {
        return this.field_175231_o;
    }

    @Override
    public void drawButton(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            boolean bl;
            minecraft.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            boolean bl2 = bl = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
            Icon icon = this.field_175231_o ? (!this.enabled ? Icon.LOCKED_DISABLED : (bl ? Icon.LOCKED_HOVER : Icon.LOCKED)) : (!this.enabled ? Icon.UNLOCKED_DISABLED : (bl ? Icon.UNLOCKED_HOVER : Icon.UNLOCKED));
            this.drawTexturedModalRect(this.xPosition, this.yPosition, icon.func_178910_a(), icon.func_178912_b(), this.width, this.height);
        }
    }

    public GuiLockIconButton(int n, int n2, int n3) {
        super(n, n2, n3, 20, 20, "");
    }

    public void func_175229_b(boolean bl) {
        this.field_175231_o = bl;
    }

    static enum Icon {
        LOCKED(0, 146),
        LOCKED_HOVER(0, 166),
        LOCKED_DISABLED(0, 186),
        UNLOCKED(20, 146),
        UNLOCKED_HOVER(20, 166),
        UNLOCKED_DISABLED(20, 186);

        private final int field_178920_h;
        private final int field_178914_g;

        public int func_178912_b() {
            return this.field_178920_h;
        }

        private Icon(int n2, int n3) {
            this.field_178914_g = n2;
            this.field_178920_h = n3;
        }

        public int func_178910_a() {
            return this.field_178914_g;
        }
    }
}

