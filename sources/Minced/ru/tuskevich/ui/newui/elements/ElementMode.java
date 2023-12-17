// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import java.util.Iterator;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;

public class ElementMode extends Element
{
    public ModeSetting setting;
    public ElementModule module;
    public boolean expended;
    
    public ElementMode(final ElementModule module, final ModeSetting setting) {
        this.setting = setting;
        this.module = module;
        this.setHeight(16.0);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        if (this.expended) {
            this.setHeight(16 + this.setting.modes.size() * 16);
        }
        RenderUtility.drawRect(this.x + 2.0, this.y, this.module.width - 4.0, this.height - 4.0, new Color(0, 0, 0, 50).getRGB());
        Fonts.MONTSERRAT12.drawCenteredString(this.setting.getName() + ": ", this.x + this.width / 2.0, this.y + 5.0, -1);
        if (this.expended) {
            RenderUtility.verticalGradient(this.x + 2.0, this.y + 14.0, this.module.width - 4.0, 2.0, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 0).getRGB());
            int offset = 0;
            for (final String s : this.setting.getModes()) {
                Fonts.MONTSERRAT12.drawString(s, this.x + 5.0, this.y + 21.0 + offset, this.setting.currentMode.equalsIgnoreCase(s) ? Hud.getColor(280).getRGB() : new Color(255, 255, 255, 255).getRGB());
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
            for (final String s : this.setting.getModes()) {
                if (this.collided(x, y, this.getX(), this.getY() + 16.0 + offset, (float)this.getWidth(), 16.0f) && button == 0) {
                    this.setting.setListMode(s);
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
