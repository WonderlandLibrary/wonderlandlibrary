/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.Component;
import ClickGUIs.NiceGUI.SettingsWindow;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.util.RenderingUtil;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;

public class Slider
extends Component {
    boolean isSliding = false;

    @Override
    public void draw(FontUtil fontUtil, int n, int n2) {
        double d;
        int n3 = 20;
        int n4 = 100;
        if (this.isSliding) {
            d = n;
            double d2 = (d - (double)this.x) / (double)n4;
            this.set.setValDouble((this.set.getMax() - this.set.getMin()) * d2);
        }
        if (this.set.getValDouble() <= this.set.getMin()) {
            this.set.setValDouble(this.set.getMin());
        }
        if (this.set.getValDouble() >= this.set.getMax()) {
            this.set.setValDouble(this.set.getMax());
        }
        d = this.set.getValDouble() / this.set.getMax();
        Gui.drawRect(this.x, this.y + n3 / 2 - 1, this.x + n4, this.y + n3 / 2 + 1, new Color(150, 150, 150, 255).getRGB());
        RenderingUtil.drawFilledCircle((int)((double)this.x + (double)n4 * d), this.y + n3 / 2, 3.0f, new Color(200, 200, 200, 255));
        FontUtil.normal.drawString(String.valueOf(this.set.getName()) + " : " + (this.set.onlyInt() ? (double)((int)this.set.getValDouble()) : (double)Math.round(this.set.getValDouble() * 1000.0) / 1000.0), this.x + n4 + 5, this.y + 1, -16777216);
        super.draw(fontUtil, n, n2);
    }

    public Slider(int n, int n2, Setting setting, SettingsWindow settingsWindow) {
        super(n, n2, setting, settingsWindow);
    }

    @Override
    public void mouseClicked(int n, int n2) {
        if (this.isWithinComponent(n, n2)) {
            this.isSliding = true;
        }
        super.mouseClicked(n, n2);
    }

    @Override
    public void mouseRelease() {
        this.isSliding = false;
        super.mouseRelease();
    }

    public boolean isWithinComponent(int n, int n2) {
        int n3 = 20;
        int n4 = 100;
        return n > this.x && this.x < this.x + n4 && n2 > this.y && n2 < this.y + n3;
    }
}

