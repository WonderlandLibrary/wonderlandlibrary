/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.obf.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tk.rektsky.utils.obf.wrapper.MinecraftWrapper;

public class DisplayGuiScreen {
    private static final Minecraft mc = MinecraftWrapper.getMinecraft(Minecraft.class);

    public static void displayGuiScreen(GuiScreen guiScreenIn) {
        mc.displayGuiScreen(guiScreenIn);
    }
}

