/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.Lemon.clickgui.component.components;

import ClickGUIs.Lemon.clickgui.component.Component;
import ClickGUIs.Lemon.clickgui.component.Frame;
import ClickGUIs.Lemon.clickgui.component.components.sub.Checkbox;
import ClickGUIs.Lemon.clickgui.component.components.sub.Keybind;
import ClickGUIs.Lemon.clickgui.component.components.sub.ModeButton;
import ClickGUIs.Lemon.clickgui.component.components.sub.Slider;
import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.Random;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Button
extends Component {
    public Frame parent;
    private ArrayList<Component> subcomponents;
    public Module mod;
    public boolean open;
    private int height;
    public int offset;
    private boolean isHovered;
    Random random = new Random();

    @Override
    public void keyTyped(char c, int n) {
        for (Component component : this.subcomponents) {
            component.keyTyped(c, n);
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        for (Component component : this.subcomponents) {
            component.mouseReleased(n, n2, n3);
        }
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.parent.getX() && n < this.parent.getX() + this.parent.getWidth() && n2 > this.parent.getY() + this.offset && n2 < this.parent.getY() + 12 + this.offset;
    }

    public Button(Module module, Frame frame, int n) {
        this.mod = module;
        this.parent = frame;
        this.offset = n;
        this.subcomponents = new ArrayList();
        this.open = false;
        this.height = 12;
        int n2 = n + 12;
        if (Exodus.INSTANCE.settingsManager.getSettingsByMod(module) != null) {
            for (Setting setting : Exodus.INSTANCE.settingsManager.getSettingsByMod(module)) {
                if (setting.isCombo()) {
                    this.subcomponents.add(new ModeButton(setting, this, module, n2));
                    n2 += 12;
                }
                if (setting.isSlider()) {
                    this.subcomponents.add(new Slider(setting, this, n2));
                    n2 += 12;
                }
                if (!setting.isCheck()) continue;
                this.subcomponents.add(new Checkbox(setting, this, n2));
                n2 += 12;
            }
        }
        this.subcomponents.add(new Keybind(this, n2));
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return 12 * (this.subcomponents.size() + 1);
        }
        return 12;
    }

    @Override
    public void renderComponent() {
        float f = this.random.nextFloat();
        float f2 = this.random.nextFloat();
        float f3 = this.random.nextFloat();
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isToggled() ? CustomIngameGui.getColorInt(this.offset / 8) : -14540254) : (this.mod.isToggled() ? CustomIngameGui.getColorInt(this.offset / 8) : -15658735));
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        FontUtil.normal.drawStringWithShadow(this.mod.getName(), (this.parent.getX() + 2) * 2, (this.parent.getY() + this.offset + 2) * 2 + 4, this.mod.isToggled() ? -1 : 0x999999);
        GL11.glPopMatrix();
        if (this.open && !this.subcomponents.isEmpty()) {
            for (Component component : this.subcomponents) {
                component.renderComponent();
            }
            Gui.drawRect(this.parent.getX() + 2, this.parent.getY() + this.offset + 12, this.parent.getX() + 3, this.parent.getY() + this.offset + (this.subcomponents.size() + 1) * 12, CustomIngameGui.getColorInt(this.offset / 8));
        }
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
        int n2 = this.offset + 12;
        for (Component component : this.subcomponents) {
            component.setOff(n2);
            n2 += 12;
        }
    }

    @Override
    public void updateComponent(int n, int n2) {
        this.isHovered = this.isMouseOnButton(n, n2);
        if (!this.subcomponents.isEmpty()) {
            for (Component component : this.subcomponents) {
                component.updateComponent(n, n2);
            }
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(n, n2) && n3 == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Component component : this.subcomponents) {
            component.mouseClicked(n, n2, n3);
        }
    }
}

