/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import tk.rektsky.gui.ClickGuiOLD;
import tk.rektsky.gui.GuiKeybind;

public class GuiManager {
    public static final Class<? extends GuiScreen>[] BLUR_GUI = new Class[]{GuiKeybind.class, GuiContainer.class, GuiIngameMenu.class, GuiOptions.class, ClickGuiOLD.class};
}

