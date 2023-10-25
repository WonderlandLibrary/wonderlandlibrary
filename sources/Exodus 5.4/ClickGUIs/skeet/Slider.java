/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.skeet;

import ClickGUIs.skeet.Comp;
import ClickGUIs.skeet.Skeet;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Slider
extends Comp {
    private boolean dragging = false;
    private double renderWidth;
    private double renderWidth2;

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        this.dragging = false;
    }

    public Slider(double d, double d2, Skeet skeet, Module module, Setting setting) {
        this.x = d;
        this.y = d2;
        this.parent = skeet;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int n, int n2) {
        super.drawScreen(n, n2);
        double d = this.setting.getMin();
        double d2 = this.setting.getMax();
        double d3 = 90.0;
        this.renderWidth = d3 * (this.setting.getValDouble() - d) / (d2 - d);
        this.renderWidth2 = d3 * (this.setting.getMax() - d) / (d2 - d);
        double d4 = Math.min(d3, Math.max(0.0, (double)n - (this.parent.posX + this.x - 70.0)));
        if (this.dragging) {
            if (d4 == 0.0) {
                this.setting.setValDouble(this.setting.getMin());
            } else {
                double d5 = this.roundToPlace(d4 / d3 * (d2 - d) + d, 1);
                this.setting.setValDouble(d5);
            }
        }
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
        Gui.drawRect(this.parent.posX + this.x - 70.0, this.parent.posY + this.y + 10.0, this.parent.posX + this.x - 70.0 + this.renderWidth2, this.parent.posY + this.y + 20.0, new Color(230, 10, 230).darker().getRGB());
        Gui.drawRect(this.parent.posX + this.x - 70.0, this.parent.posY + this.y + 10.0, this.parent.posX + this.x - 70.0 + this.renderWidth, this.parent.posY + this.y + 20.0, new Color(230, 10, 230).getRGB());
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawString(String.valueOf(this.setting.getName()) + ": " + this.setting.getValDouble(), (int)(this.parent.posX + this.x - 70.0), (int)(this.parent.posY + this.y), -1);
    }

    private double roundToPlace(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isInside(n, n2, this.parent.posX + this.x - 70.0, this.parent.posY + this.y + 10.0, this.parent.posX + this.x - 70.0 + this.renderWidth2, this.parent.posY + this.y + 20.0) && n3 == 0) {
            this.dragging = true;
        }
    }
}

