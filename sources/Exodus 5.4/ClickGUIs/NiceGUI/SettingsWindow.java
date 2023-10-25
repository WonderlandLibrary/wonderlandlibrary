/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.Button;
import ClickGUIs.NiceGUI.CheckBox;
import ClickGUIs.NiceGUI.ComboBox;
import ClickGUIs.NiceGUI.Component;
import ClickGUIs.NiceGUI.Slider;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.util.RenderingUtil;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SettingsWindow {
    ArrayList<Component> components = new ArrayList();
    int y = 100;
    int yCount = 0;
    Button parent;
    int x = 200;

    public void mouseReleased() {
        for (Component component : this.components) {
            component.mouseRelease();
        }
    }

    public Button getParent() {
        return this.parent;
    }

    public void setParent(Button button) {
        this.parent = button;
    }

    public void draw(FontUtil fontUtil, int n, int n2) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        RenderingUtil.drawRoundedRect(this.x, this.y, scaledResolution.getScaledWidth() - this.x, scaledResolution.getScaledHeight() - this.y, 5, new Color(240, 240, 240, 255).darker());
        for (Component component : this.components) {
            component.draw(fontUtil, n, n2);
        }
    }

    public SettingsWindow(Button button) {
        this.parent = button;
        if (Exodus.INSTANCE.settingsManager.getSettingsByMod(button.mod) != null) {
            int n = 0;
            for (Setting setting : Exodus.INSTANCE.getSettingsManager().getSettingsByMod(button.mod)) {
                if (setting.isCheck()) {
                    this.components.add(new CheckBox(this.x + 5, this.y + 5 + n * 20, setting, this));
                } else if (setting.isCombo()) {
                    this.components.add(new ComboBox(this.x + 5, this.y + 5 + n * 20, setting, this));
                } else if (setting.isSlider()) {
                    this.components.add(new Slider(this.x + 5, this.y + 5 + n * 20, setting, this));
                }
                ++n;
            }
        }
    }

    public void mouseClicked(int n, int n2) {
        for (Component component : this.components) {
            component.mouseClicked(n, n2);
        }
    }
}

