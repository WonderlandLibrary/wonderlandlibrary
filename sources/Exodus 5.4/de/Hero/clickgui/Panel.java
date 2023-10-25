/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui;

import de.Hero.clickgui.ClickGUI;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import java.awt.Color;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.ui.Draw;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Panel {
    public double y;
    public boolean visible;
    public double width;
    public ClickGUI clickgui;
    public boolean extended;
    public double height;
    public String title;
    private double y2;
    public boolean dragging;
    public double x;
    public ArrayList<ModuleButton> Elements = new ArrayList();
    private double x2;

    public void mouseReleased(int n, int n2, int n3) {
        if (!this.visible) {
            return;
        }
        if (n3 == 0) {
            this.dragging = false;
        }
    }

    public boolean mouseClicked(int n, int n2, int n3) {
        if (!this.visible) {
            return false;
        }
        if (n3 == 0 && this.isHovered(n, n2)) {
            this.x2 = this.x - (double)n;
            this.y2 = this.y - (double)n2;
            this.dragging = true;
            return true;
        }
        if (n3 == 1 && this.isHovered(n, n2)) {
            this.extended = !this.extended;
            return true;
        }
        if (this.extended) {
            for (ModuleButton moduleButton : this.Elements) {
                if (!moduleButton.mouseClicked(n, n2, n3)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= this.y && (double)n2 <= this.y + this.height;
    }

    public void drawScreen(int n, int n2, float f) {
        if (!this.visible) {
            return;
        }
        if (this.dragging) {
            this.x = this.x2 + (double)n;
            this.y = this.y2 + (double)n2;
        }
        Color color = ColorUtil.getClickGUIColor().darker();
        int n3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 170).getRGB();
        Gui.drawRect(this.x, this.y - 3.0, this.x + this.width, this.y + this.height, CustomIngameGui.getColorInt((int)(this.y / 8.0)));
        Gui.drawRect(this.x, this.y - 3.0, this.x + this.width - 64.0, this.y + this.height, 0x55111111);
        if (Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
            Gui.drawRect(this.x - 2.0, this.y, this.x, this.y + this.height, n3);
            FontUtil.normal.drawStringWithShadow(this.title, this.x + 2.0, (float)(this.y + this.height / 2.0 - (double)(FontUtil.normal.getHeight() / 2)), -1052689);
        } else if (Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Clean")) {
            FontUtil.normal.drawStringWithShadow(this.title, this.x + this.width / 3.0, (float)(this.y + this.height / 4.5), -1052689);
            if (this.title.equalsIgnoreCase("Combat")) {
                Draw.drawImg(new ResourceLocation("Terror/combat.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("Movement")) {
                Draw.drawImg(new ResourceLocation("Terror/movement.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("Render")) {
                Draw.drawImg(new ResourceLocation("Terror/render.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("Misc")) {
                Draw.drawImg(new ResourceLocation("Terror/misc.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("Player")) {
                Draw.drawImg(new ResourceLocation("Terror/player.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("SkyBlock")) {
                Draw.drawImg(new ResourceLocation("Terror/skyblock.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
            if (this.title.equalsIgnoreCase("World")) {
                Draw.drawImg(new ResourceLocation("Terror/world.png"), this.x - 2.0, this.y - 2.0, this.width - 60.0, this.height + 2.0);
            }
        }
        if (this.extended && !this.Elements.isEmpty()) {
            double d = this.y + this.height;
            int n4 = Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New") ? -14474461 : (Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Clean") ? -1156246251 : 0);
            for (ModuleButton moduleButton : this.Elements) {
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
                    Gui.drawRect(this.x - 2.0, d, this.x + this.width, d + moduleButton.height + 1.0, n3);
                }
                Gui.drawRect(this.x, d, this.x + this.width, d + moduleButton.height + 1.0, n4);
                moduleButton.x = this.x + 2.0;
                moduleButton.y = d;
                moduleButton.width = this.width - 4.0;
                moduleButton.drawScreen(n, n2, f);
                d += moduleButton.height + 1.0;
            }
            Gui.drawRect(this.x, d, this.x + this.width, d + 1.0, n4);
        }
    }

    public void setup() {
    }

    public Panel(String string, double d, double d2, double d3, double d4, boolean bl, ClickGUI clickGUI) {
        this.title = string;
        this.x = d;
        this.y = d2;
        this.width = d3;
        this.height = d4;
        this.extended = bl;
        this.dragging = false;
        this.visible = true;
        this.clickgui = clickGUI;
        this.setup();
    }
}

