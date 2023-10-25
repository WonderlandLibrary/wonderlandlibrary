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

public class ComboBox
extends Component {
    public boolean isWithinComponent(int n, int n2) {
        return n > this.x && n < this.x + 80 && n2 > this.y && n2 < this.y + 20;
    }

    public ComboBox(int n, int n2, Setting setting, SettingsWindow settingsWindow) {
        super(n, n2, setting, settingsWindow);
    }

    @Override
    public void mouseClicked(int n, int n2) {
        if (this.isWithinComponent(n, n2)) {
            int n3 = this.set.getOptions().indexOf(this.set.getValString());
            if (n3 + 1 >= this.set.getOptions().size()) {
                this.set.setValString(this.set.getOptions().get(0));
            } else {
                this.set.setValString(this.set.getOptions().get(n3 + 1));
            }
        }
        super.mouseClicked(n, n2);
    }

    @Override
    public void draw(FontUtil fontUtil, int n, int n2) {
        RenderingUtil.drawRoundedRect(this.x, this.y, this.x + 150, this.y + 16, 3, new Color(230, 230, 230, 255).darker());
        FontUtil.normal.drawString(String.valueOf(this.set.getName()) + " : " + this.set.getValString(), this.x + 2, this.y + 1, -16777216);
        super.draw(fontUtil, n, n2);
    }
}

