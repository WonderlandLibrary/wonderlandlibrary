package net.minecraft.client.gui.spectator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

static class SpectatorMenu.EndSpectatorObject
implements ISpectatorMenuObject {
    private SpectatorMenu.EndSpectatorObject() {
    }

    public void func_178661_a(SpectatorMenu menu) {
        menu.func_178641_d();
    }

    public IChatComponent getSpectatorName() {
        return new ChatComponentText("Close menu");
    }

    public void func_178663_a(float p_178663_1_, int alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)128.0f, (float)0.0f, (int)16, (int)16, (float)256.0f, (float)256.0f);
    }

    public boolean func_178662_A_() {
        return true;
    }
}
