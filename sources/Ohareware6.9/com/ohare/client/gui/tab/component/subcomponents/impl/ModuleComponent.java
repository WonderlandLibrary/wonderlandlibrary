package com.ohare.client.gui.tab.component.subcomponents.impl;

import java.awt.Color;

import com.ohare.client.Client;
import com.ohare.client.gui.tab.component.impl.CategoryComponent;
import com.ohare.client.gui.tab.component.subcomponents.SubComponent;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.module.modules.visuals.hudcomps.comps.TabGUIComp;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;

import net.minecraft.client.gui.ScaledResolution;

/**
 * made by oHare for oHareWare
 *
 * @since 7/13/2019
 **/
public class ModuleComponent extends SubComponent {
    private Module module;
    private CategoryComponent parent;
    private float incY;
    public ModuleComponent(CategoryComponent parent, Module module, float x, float incY, float width, float height) {
        super(module.getLabel(), x, parent.getY() , width, height);
        this.module = module;
        this.parent = parent;
        this.incY = incY;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void draw(ScaledResolution sr) {
        super.draw(sr);
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        TabGUIComp tabGUIComp = (TabGUIComp) Client.INSTANCE.getHudCompManager().getHudComp("TabGUIComp");

        if (tabGUIComp.roundedTab.isEnabled()) {
            Fonts.clickfont.drawStringWithShadow(getLabel(), parent.getParent().isHalfway() ? getX() - parent.getWidth() - 14 - Fonts.clickfont.getStringWidth(getLabel()) : (getX() + 12), parent.getParent().getY() + parent.getIncY() + incY + 4.5, module.isEnabled() ? -1 : new Color(0x9D9798).getRGB());
            if (parent.getSelectedModule() == module) RenderUtil.drawRect(parent.getParent().isHalfway() ? getX() - parent.getWidth() - 12 : (getX() + 8),parent.getParent().getY() + parent.getIncY() + incY + 3,2,6.5f,new Color(0, 107, 214, 255).getRGB());
        } else {
            RenderUtil.drawRect(getX() - (parent.getParent().isHalfway() ? parent.getWidth() + 6 + parent.getLargestString() + 10 : 0), parent.getParent().getY() + parent.getIncY() + incY, getWidth(), getHeight(), parent.getSelectedModule() == module ? (hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0x616161).getRGB()) : new Color(0, 0, 0, 80).getRGB());
            Fonts.hudfont.drawStringWithShadow(getLabel(), parent.getParent().isHalfway() ? getX() - parent.getWidth() - (parent.getSelectedModule() == module ? 12 : 8) - Fonts.hudfont.getStringWidth(getLabel()) : (getX() + (parent.getSelectedModule() == module ? 6 : 2)), parent.getParent().getY() + parent.getIncY() + incY + 3, module.isEnabled() ? -1 : new Color(0x9D9798).getRGB());
        }
    }

    @Override
    public void keypress(int key) {
        super.keypress(key);
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
    }

    public Module getModule() {
        return module;
    }
}
