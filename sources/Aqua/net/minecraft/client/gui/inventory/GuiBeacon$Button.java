package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/*
 * Exception performing whole class analysis ignored.
 */
static class GuiBeacon.Button
extends GuiButton {
    private final ResourceLocation field_146145_o;
    private final int field_146144_p;
    private final int field_146143_q;
    private boolean field_146142_r;

    protected GuiBeacon.Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_) {
        super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
        this.field_146145_o = p_i1077_4_;
        this.field_146144_p = p_i1077_5_;
        this.field_146143_q = p_i1077_6_;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiBeacon.access$000());
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = 219;
            int j = 0;
            if (!this.enabled) {
                j += this.width * 2;
            } else if (this.field_146142_r) {
                j += this.width * 1;
            } else if (this.hovered) {
                j += this.width * 3;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
            if (!GuiBeacon.access$000().equals((Object)this.field_146145_o)) {
                mc.getTextureManager().bindTexture(this.field_146145_o);
            }
            this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
        }
    }

    public boolean func_146141_c() {
        return this.field_146142_r;
    }

    public void func_146140_b(boolean p_146140_1_) {
        this.field_146142_r = p_146140_1_;
    }
}
