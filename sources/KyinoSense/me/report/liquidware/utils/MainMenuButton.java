/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import me.report.liquidware.gui.GuiMainMenu;
import net.ccbluex.liquidbounce.utils.RenderUtils;

public class MainMenuButton {
    public GuiMainMenu parent;
    public String icon;
    public String text;
    public Executor action;
    public int buttonID;
    public float x;
    public float y;
    public float textOffset;
    public float yAnimation = 0.0f;

    public MainMenuButton(GuiMainMenu parent, int id, String icon, String text, Executor action) {
        this.parent = parent;
        this.buttonID = id;
        this.icon = icon;
        this.text = text;
        this.action = action;
        this.textOffset = 0.0f;
    }

    public MainMenuButton(GuiMainMenu parent, int id, String icon, String text, Executor action, float yOffset) {
        this.parent = parent;
        this.buttonID = id;
        this.icon = icon;
        this.text = text;
        this.action = action;
        this.textOffset = yOffset;
    }

    public void draw(float x, float y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.yAnimation = RenderUtils.smoothAnimation(this.yAnimation, RenderUtils.isHovering(mouseX, mouseY, x, y, x + 50.0f, y + 30.0f) ? 2.0f : 0.0f, 50.0f, 0.3f);
        RenderUtils.drawGradientRect(x, y + 30.0f - this.yAnimation * 3.0f, x + 50.0f, y + 30.0f, 3453695, 2016719615);
        RenderUtils.drawRect(x, y + 30.0f - this.yAnimation, x + 50.0f, y + 30.0f, -13323521);
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovering(mouseX, mouseY, this.x, this.y, this.x + 50.0f, this.y + 30.0f) && this.action != null && mouseButton == 0) {
            this.action.execute();
        }
    }

    public static interface Executor {
        public void execute();
    }
}

