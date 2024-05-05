/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.Window;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class ClickyUI
extends GuiScreen {
    public static ArrayList<Window> windows = Lists.newArrayList();
    public int scrollVelocity;
    public static boolean binding;

    public ClickyUI() {
        if (windows.isEmpty()) {
            int x = 25;
            for (ModuleCategory c : ModuleCategory.values()) {
                windows.add(new Window(c, 20, x));
                x += 30;
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        Gui.func_73734_a((int)0, (int)0, (int)Display.getWidth(), (int)Display.getHeight(), (int)new Color(0, 0, 0, 100).getRGB());
        GlStateManager.func_179094_E();
        windows.forEach(w2 -> w2.render(mouseX, mouseY));
        GlStateManager.func_179121_F();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
        }
        windows.forEach(w2 -> w2.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (keyCode == 1 && !binding) {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        windows.forEach(w2 -> w2.key(typedChar, keyCode));
    }
}

