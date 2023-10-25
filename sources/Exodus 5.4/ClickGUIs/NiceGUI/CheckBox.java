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
import net.minecraft.client.renderer.GlStateManager;

public class CheckBox
extends Component {
    @Override
    public void draw(FontUtil fontUtil, int n, int n2) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        FontUtil.normal.drawString(this.set.getName(), this.x + 15, this.y + 2, -16777216);
        if (!this.set.getValBoolean()) {
            RenderingUtil.drawCircle(this.x + 4, this.y + 8, 6.0f, 100, -11513776);
        } else {
            RenderingUtil.drawCircle(this.x + 4, this.y + 8, 6.0f, 100, -11513776);
            RenderingUtil.drawFilledCircle(this.x + 4, this.y + 8, 4.0f, new Color(50, 50, 50, 255));
        }
        super.draw(fontUtil, n, n2);
    }

    public boolean isWithinComponent(int n, int n2) {
        return n > this.x && n < this.x + 15 && n2 > this.y && n2 < this.y + 12;
    }

    public CheckBox(int n, int n2, Setting setting, SettingsWindow settingsWindow) {
        super(n, n2, setting, settingsWindow);
    }

    @Override
    public void mouseClicked(int n, int n2) {
        if (this.isWithinComponent(n, n2)) {
            this.set.setValBoolean(!this.set.getValBoolean());
        }
        super.mouseClicked(n, n2);
    }
}

