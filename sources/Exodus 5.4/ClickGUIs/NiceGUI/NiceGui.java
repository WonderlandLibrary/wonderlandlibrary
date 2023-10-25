/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.Button;
import ClickGUIs.NiceGUI.CategoryButton;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class NiceGui
extends GuiScreen {
    static FontUtil ufr;
    Category currentCategory = Category.COMBAT;
    int offset = 0;
    ArrayList<Button> mods;
    ArrayList<CategoryButton> buttons = new ArrayList();
    int lastOffset = 0;

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        int n4 = width / 6;
        int n5 = height / 7;
        for (Button object : this.mods) {
            if (object.settingsWindow == null) continue;
            object.settingsWindow.mouseClicked(n, n2);
            return;
        }
        for (CategoryButton categoryButton : this.buttons) {
            categoryButton.mouseClicked(n, n2);
            if (!categoryButton.isWithinButton(n, n2)) continue;
            this.mods.clear();
            int n6 = 0;
            for (Module module : Exodus.INSTANCE.getModuleManager().getModulesByCategory(this.currentCategory)) {
                Button button = new Button(this.currentCategory, n4 + 86, n5 + 5 + n6 * 30, module);
                button.setStart(n5 + 5 + n6 * 30);
                this.mods.add(button);
                ++n6;
            }
            this.offset = 0;
        }
        for (Button button : this.mods) {
            button.mouseClicked(n, n2, n3);
        }
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        int n = width / 6;
        int n2 = height / 7;
        int n3 = 0;
        Category[] categoryArray = Category.values();
        int n4 = categoryArray.length;
        int n5 = 0;
        while (n5 < n4) {
            Category category = categoryArray[n5];
            this.buttons.add(new CategoryButton(n + 15, n2 + 5 + 44 * n3, category, this));
            ++n3;
            ++n5;
        }
        super.initGui();
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        for (Button button : this.mods) {
            if (button.settingsWindow == null) continue;
            button.settingsWindow.mouseReleased();
        }
        super.mouseReleased(n, n2, n3);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        for (Button button : this.mods) {
            if (button.settingsWindow == null) continue;
            if (n == 1) {
                button.settingsWindow = null;
            }
            return;
        }
        super.keyTyped(c, n);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        int n3;
        if (Mouse.hasWheel()) {
            n3 = Mouse.getDWheel();
            if (n3 < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (n3 > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        if (this.lastOffset != this.offset) {
            n3 = this.offset - this.lastOffset;
            this.lastOffset += n3 / 4;
        }
        n3 = width / 6;
        int n4 = height / 7;
        int n5 = width - n3;
        int n6 = height - n4 - 90;
        GlStateManager.pushMatrix();
        this.prepareScissorBox(n3, n4, n5, n6);
        GL11.glEnable((int)3089);
        Gui.drawRect(n3, n4, n5, n6, new Color(230, 230, 230, 255).darker().getRGB());
        Gui.drawRect(n3, n4, n3 + 80, n6, -1);
        for (CategoryButton object : this.buttons) {
            object.draw(n, n2);
        }
        if (this.currentCategory != null) {
            for (Button button : this.mods) {
                button.setY(button.start - this.lastOffset);
                button.draw(n, n2);
            }
            for (Button button : this.mods) {
                button.setY(button.start - this.lastOffset);
                button.drawString(ufr);
            }
        }
        for (Button button : this.mods) {
            if (button.settingsWindow == null) continue;
            button.settingsWindow.draw(ufr, n, n2);
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        super.drawScreen(n, n2, f);
    }

    public NiceGui() {
        this.mods = new ArrayList();
    }

    public void prepareScissorBox(float f, float f2, float f3, float f4) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(f * (float)n)), (int)((int)(((float)scaledResolution.getScaledHeight() - f4) * (float)n)), (int)((int)((f3 - f) * (float)n)), (int)((int)((f4 - f2) * (float)n)));
    }
}

