// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import ru.tuskevich.util.animations.AnimationMath;
import org.lwjgl.input.Keyboard;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import java.util.Iterator;
import ru.tuskevich.ui.dropui.setting.imp.RangeSetting;
import ru.tuskevich.ui.dropui.setting.imp.TextSetting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import ru.tuskevich.ui.newui.UIPanel;
import ru.tuskevich.modules.Module;

public class ElementModule extends Element
{
    private Module module;
    private UIPanel panel;
    public boolean extended;
    public boolean binding;
    public List<Element> elements;
    public float rotated;
    
    public ElementModule(final Module module, final UIPanel panel) {
        this.elements = new ArrayList<Element>();
        this.panel = panel;
        this.module = module;
        this.setHeight(16.0);
        for (final Setting s : module.get()) {
            if (s instanceof BooleanSetting) {
                this.elements.add(new ElementBoolean(this, (BooleanSetting)s));
            }
            if (s instanceof SliderSetting) {
                this.elements.add(new ElementSlider(this, (SliderSetting)s));
            }
            if (s instanceof ModeSetting) {
                this.elements.add(new ElementMode(this, (ModeSetting)s));
            }
            if (s instanceof ColorSetting) {
                this.elements.add(new ElementColor(this, (ColorSetting)s));
            }
            if (s instanceof MultiBoxSetting) {
                this.elements.add(new ElementList(this, (MultiBoxSetting)s));
            }
            if (s instanceof TextSetting) {
                this.elements.add(new ElementText(this, (TextSetting)s));
            }
            if (s instanceof RangeSetting) {
                this.elements.add(new ElementRange(this, (RangeSetting)s));
            }
        }
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        RenderUtility.drawRound((float)this.x + (float)this.width / 15.0f, (float)this.y + 1.0f, 86.0f, 13.0f, 3.0f, new Color(25, 25, 25));
        if (this.module.state) {
            RenderUtility.drawGradientRound((float)this.x + (float)this.width / 15.0f, (float)this.y + 1.0f, 86.0f, 13.0f, 3.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
            Fonts.Nunito14.drawCenteredString(this.binding ? ("Bind -> [" + Keyboard.getKeyName(this.module.bind) + "]") : this.module.name, this.x + this.width / 2.0, this.y + 6.0, this.module.state ? new Color(116, 195, 224).getRGB() : -1);
        }
        this.rotated = AnimationMath.fast(this.rotated, this.extended ? 90.0f : 0.0f, 15.0f);
        Fonts.Nunito14.drawCenteredString(this.binding ? ("Bind -> [" + Keyboard.getKeyName(this.module.bind) + "]") : this.module.name, this.x + this.width / 2.0, this.y + 6.0, this.module.state ? new Color(255, 255, 255, 255).getRGB() : new Color(255, 255, 255, 255).getRGB());
        int offset = 0;
        if (this.extended && this.elements.size() > 0) {
            for (final Element e : this.elements) {
                if (e.isShown()) {
                    e.x = this.x;
                    e.y = this.y + 15.5 + offset;
                    e.width = this.width;
                    e.height = 16.0;
                    e.draw(mouseX, mouseY);
                    offset += (int)e.getHeight();
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 1) {
            this.extended = !this.extended;
        }
        if (this.isHovered(mouseX, mouseY) && (mouseButton == 2 || (Keyboard.isKeyDown(29) && mouseButton == 0))) {
            this.binding = true;
        }
        else if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            this.module.toggle();
        }
        if (this.extended) {
            for (final Element e : this.elements) {
                if (!e.isShown()) {
                    continue;
                }
                e.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int button) {
        super.mouseReleased(x, y, button);
        if (this.extended) {
            for (final Element e : this.elements) {
                if (!e.isShown()) {
                    continue;
                }
                e.mouseReleased(x, y, button);
            }
        }
    }
    
    @Override
    public void keypressed(final char c, final int key) {
        super.keypressed(c, key);
        this.elements.forEach(e -> e.keypressed(c, key));
        if (this.binding) {
            if (key == 211) {
                this.module.bind = 0;
                this.binding = false;
                return;
            }
            this.module.bind = key;
            this.binding = false;
        }
    }
}
