package net.minecraft.client.gui.spectator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/*
 * Exception performing whole class analysis ignored.
 */
static class SpectatorMenu.MoveMenuObject
implements ISpectatorMenuObject {
    private final int field_178666_a;
    private final boolean field_178665_b;

    public SpectatorMenu.MoveMenuObject(int p_i45495_1_, boolean p_i45495_2_) {
        this.field_178666_a = p_i45495_1_;
        this.field_178665_b = p_i45495_2_;
    }

    public void func_178661_a(SpectatorMenu menu) {
        SpectatorMenu.access$102((SpectatorMenu)menu, (int)this.field_178666_a);
    }

    public IChatComponent getSpectatorName() {
        return this.field_178666_a < 0 ? new ChatComponentText("Previous Page") : new ChatComponentText("Next Page");
    }

    public void func_178663_a(float p_178663_1_, int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        if (this.field_178666_a < 0) {
            Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)144.0f, (float)0.0f, (int)16, (int)16, (float)256.0f, (float)256.0f);
        } else {
            Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)160.0f, (float)0.0f, (int)16, (int)16, (float)256.0f, (float)256.0f);
        }
    }

    public boolean func_178662_A_() {
        return this.field_178665_b;
    }
}
