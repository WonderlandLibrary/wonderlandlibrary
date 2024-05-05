package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton
extends GuiButton {
    private boolean field_175231_o = false;

    public GuiLockIconButton(int p_i45538_1_, int p_i45538_2_, int p_i45538_3_) {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
    }

    public boolean func_175230_c() {
        return this.field_175231_o;
    }

    public void func_175229_b(boolean p_175229_1_) {
        this.field_175231_o = p_175229_1_;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean flag;
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            boolean bl = flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Icon guilockiconbutton$icon = this.field_175231_o ? (!this.enabled ? Icon.LOCKED_DISABLED : (flag ? Icon.LOCKED_HOVER : Icon.LOCKED)) : (!this.enabled ? Icon.UNLOCKED_DISABLED : (flag ? Icon.UNLOCKED_HOVER : Icon.UNLOCKED));
            this.drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.func_178910_a(), guilockiconbutton$icon.func_178912_b(), this.width, this.height);
        }
    }
}
