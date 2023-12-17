// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import java.util.List;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.ui.newui.SmartScissor;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import java.util.ArrayList;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;

public class ElementList extends Element
{
    public ElementModule module;
    public MultiBoxSetting setting;
    public boolean expended;
    
    public ElementList(final ElementModule e, final MultiBoxSetting setting) {
        this.module = e;
        this.setting = setting;
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        if (this.expended) {
            this.setHeight(16 + this.setting.values.length * 16);
        }
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < this.setting.values.length; ++i) {
            if (this.setting.selectedValues.get(i)) {
                list.add(this.setting.values[i].substring(0, 2).toLowerCase());
            }
        }
        String sA = "Empty";
        if (!list.isEmpty()) {
            sA = String.join(", ", list);
        }
        RenderUtility.drawRect(this.x + 2.0, this.y, this.module.width - 4.0, this.height - 4.0, new Color(0, 0, 0, 50).getRGB());
        Fonts.MONTSERRAT12.drawCenteredString(this.setting.getName() + ": ", this.x + this.width / 2.0, this.y + 5.0, -1);
        SmartScissor.push();
        SmartScissor.unset();
        SmartScissor.pop();
        if (this.expended) {
            RenderUtility.verticalGradient(this.x + 2.0, this.y + 14.0, this.module.width - 4.0, 2.0, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 0).getRGB());
            int offset = 0;
            for (final String s : this.setting.values) {
                Fonts.MONTSERRAT12.drawString(s, this.x + 5.0, this.y + 21.0 + offset, ((boolean)this.setting.selectedValues.get(offset / 16)) ? Hud.getColor(280).getRGB() : new Color(255, 255, 255, 255).getRGB());
                offset += 16;
            }
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int button) {
        super.mouseClicked(x, y, button);
        if (this.collided(x, y, this.getX(), this.getY(), (float)this.getWidth(), 16.0f) && button == 1) {
            this.expended = !this.expended;
        }
        if (this.expended) {
            int offset = 0;
            for (final String s : this.setting.values) {
                if (this.collided(x, y, this.getX(), this.getY() + 16.0 + offset, (float)this.getWidth(), 16.0f) && button == 0) {
                    this.setting.selectedValues.set(offset / 16, !this.setting.selectedValues.get(offset / 16));
                }
                offset += 16;
            }
        }
    }
    
    @Override
    public boolean isShown() {
        return this.setting.isVisible();
    }
}
