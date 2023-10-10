/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Gui;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoBindButton;
import tk.rektsky.gui.clickgui.components.AstolfoBooleanButton;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.gui.clickgui.components.AstolfoDoubleButton;
import tk.rektsky.gui.clickgui.components.AstolfoModeButton;
import tk.rektsky.gui.clickgui.components.AstolfoNumberButton;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.module.settings.Setting;

public class AstolfoModuleButton
extends AstolfoButton {
    public Module module;
    public Color color;
    public boolean extended;
    public float finalHeight;
    public ArrayList<AstolfoButton> astolfoButtons = new ArrayList();

    public AstolfoModuleButton(float x2, float y2, float width, float height, Module mod, Color col) {
        super(x2, y2, width, height);
        this.module = mod;
        this.color = col;
        float startY = y2 + height;
        int count = 0;
        ArrayList<Setting> settings = this.module.settings;
        for (Setting set : settings.stream().sorted((o1, o2) -> {
            int first = 0;
            int second = 0;
            if (o1 instanceof ListSetting) {
                first = 4;
            }
            if (o2 instanceof ListSetting) {
                second = 4;
            }
            if (first > second) {
                return -1;
            }
            if (first < second) {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList())) {
            int cat_height = AstolfoClickGui.otherFont.FONT_HEIGHT + 10;
            if (set instanceof BooleanSetting) {
                this.astolfoButtons.add(new AstolfoBooleanButton(x2, startY + (float)(18 * count), width, cat_height, (BooleanSetting)set, this.color));
            }
            if (set instanceof ListSetting) {
                this.astolfoButtons.add(new AstolfoModeButton(x2, startY + (float)(18 * count), width, cat_height, (ListSetting)set, this.color));
            }
            if (set instanceof IntSetting) {
                this.astolfoButtons.add(new AstolfoNumberButton(x2, startY + (float)(18 * count), width, cat_height, (IntSetting)set, this.color));
            }
            if (set instanceof DoubleSetting) {
                this.astolfoButtons.add(new AstolfoDoubleButton(x2, startY + (float)(18 * count), width, cat_height, (DoubleSetting)set, this.color));
            }
            ++count;
        }
        int cat_height = AstolfoClickGui.otherFont.FONT_HEIGHT + 10;
        this.astolfoButtons.add(new AstolfoBindButton(x2, startY + (float)(18 * count), width, cat_height, this.module));
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        if (!this.extended) {
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), this.module.isToggled() ? this.color.getRGB() : -14473693);
        } else {
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -15197673);
        }
        AstolfoClickGui.otherFont.drawString(this.module.name, this.x + 4.0f, this.y + this.height / 2.0f - 4.0f, this.extended ? (this.module.isToggled() ? this.color.getRGB() : -1) : -1);
        int count = 0;
        float hehe = 0.0f;
        if (this.extended) {
            float startY = this.y + this.height;
            for (AstolfoButton pan : this.astolfoButtons) {
                pan.x = this.x;
                pan.y = startY + pan.height * (float)count;
                pan.drawPanel(mouseX, mouseY);
                ++count;
                hehe = pan.height;
            }
        }
        this.finalHeight = hehe * (float)count + this.height;
        if (!this.module.bypassing) {
            Gui.drawRect((int)(this.x + this.width - 10.0f), (int)(this.y + this.height / 2.0f - 2.0f), (int)(this.x + this.width - 14.0f), (int)(this.y + this.height / 2.0f + 2.0f), -256);
        }
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY) && click) {
            if (button == 0) {
                this.module.toggle();
            } else if (this.module.settings.size() > 0) {
                this.extended = !this.extended;
            }
        }
    }
}

