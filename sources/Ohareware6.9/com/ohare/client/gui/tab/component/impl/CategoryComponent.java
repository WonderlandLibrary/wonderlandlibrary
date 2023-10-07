package com.ohare.client.gui.tab.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.ohare.client.Client;
import com.ohare.client.gui.tab.TabGUI;
import com.ohare.client.gui.tab.component.Component;
import com.ohare.client.gui.tab.component.subcomponents.SubComponent;
import com.ohare.client.gui.tab.component.subcomponents.impl.ModuleComponent;
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
public class CategoryComponent extends Component {
    private TabGUI parent;
    private Module.Category category;
    private float incY;
    private ArrayList<SubComponent> subComponents = new ArrayList();
    private ArrayList<Module> modules;
    private Module selectedModule;
    private float largestString;

    public CategoryComponent(TabGUI parent, Module.Category category, float x, float incY, float width, float height) {
        super(StringUtils.capitalize(category.name().toLowerCase()), x, parent.getY(), width, height);
        this.parent = parent;
        this.category = category;
        this.incY = incY;
    }

    @Override
    public void init() {
        super.init();
        modules = new ArrayList(Client.INSTANCE.getModuleManager().getCategoryCheats(category));
        modules.sort(Comparator.comparing(module -> module.getLabel()));
        selectedModule = modules.get(0);
        float moduleY = 0;
        largestString = Fonts.hudfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(0).getLabel());
        for (int i = 0; i < Client.INSTANCE.getModuleManager().getCategoryCheats(category).size(); i++) {
            if (Fonts.hudfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel()) > largestString) {
                largestString = Fonts.hudfont.getStringWidth(Client.INSTANCE.getModuleManager().getCategoryCheats(category).get(i).getLabel());
            }
        }
        for (Module module : modules) {
            subComponents.add(new ModuleComponent(this, module, getX() + getWidth() + 2, moduleY, largestString + 10, 12));
            moduleY += 12;
        }
    }

    @Override
    public void updatePosition() {
        subComponents.forEach(component -> {
            component.setX(getX() + getWidth() + 2);
            component.updatePosition();
        });
    }

    @Override
    public void draw(ScaledResolution sr) {
        super.draw(sr);
        TabGUIComp tabGUIComp = (TabGUIComp) Client.INSTANCE.getHudCompManager().getHudComp("TabGUIComp");
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("HUD");
        if (tabGUIComp.roundedTab.isEnabled()) {
            Fonts.iconfont.drawString(getFontChar(category), parent.isHalfway() ? (getX() + getWidth() - 20) : getX() +10, parent.getY() + incY + 3, -1);
            Fonts.clickfont.drawStringWithShadow(getLabel(), parent.isHalfway() ? (getX() + getWidth() - Fonts.clickfont.getStringWidth(getLabel()) - 23) : getX() +23, parent.getY() + incY + 4.5,-1);
            RenderUtil.drawRoundedRect(!parent.isHalfway() ? (getX() + getWidth() - 22) : getX() +10,parent.getY() + incY + 3,12,6.5f,8,new Color(0, 107, 214, 255).getRGB());
            Fonts.sliderfont.drawString(String.valueOf(modules.size()),!parent.isHalfway() ? (getX() + getWidth() - 16.5f - Fonts.sliderfont.getStringWidth(String.valueOf(modules.size())) / 2) : getX() +15.5f- Fonts.sliderfont.getStringWidth(String.valueOf(modules.size())) / 2,parent.getY() + incY + 5.5f,-1);
            if (parent.getSelectedCategory() == category) RenderUtil.drawRect(parent.isHalfway() ? (getX() + getWidth() - 6): getX() + 6,parent.getY() + incY + 3,2,6.5f,new Color(0, 107, 214, 255).getRGB());
            if (parent.isExtended() && parent.getSelectedCategory() == category) {
                RenderUtil.drawRoundedRectWithShadow(getX() + getWidth() + 4 - (getParent().isHalfway() ? getWidth() + 6 + getLargestString() + 12 : 0), parent.getY() + incY,largestString + 10,subComponents.size() * 12,15,new Color(38, 46, 52, 255).getRGB());
                subComponents.forEach(subComponent -> subComponent.draw(sr));
            }
        } else {
            RenderUtil.drawRect(getX(), parent.getY() + incY, getWidth(), getHeight(),parent.getSelectedCategory() == category ? (hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0x616161).getRGB()) : new Color(0, 0, 0, 80).getRGB());

            Fonts.hudfont.drawStringWithShadow(getLabel(), parent.isHalfway() ? (getX() + getWidth() - Fonts.hudfont.getStringWidth(getLabel()) - (parent.getSelectedCategory() == category ? 6 : 2)) : (getX() + (parent.getSelectedCategory() == category ? 6 : 2)), parent.getY() + incY + 3, parent.getSelectedCategory() == category ? -1 : new Color(0x9D9798).getRGB());
            if (parent.isExtended() && parent.getSelectedCategory() == category) subComponents.forEach(subComponent -> subComponent.draw(sr));
        }
    }

    @Override
    public void keypress(int key) {
        super.keypress(key);
        if (parent.isExtended() && parent.getSelectedCategory() == category) {
            subComponents.forEach(subComponent -> subComponent.keypress(key));
            switch (key) {
                case Keyboard.KEY_DOWN:
                    if (modules.indexOf(selectedModule) + 1 >= modules.size()) {
                        setSelectedModule(modules.get(0));
                        return;
                    }
                    setSelectedModule(modules.get(modules.indexOf(selectedModule) + 1));
                    break;
                case Keyboard.KEY_UP:
                    if (modules.indexOf(selectedModule) <= 0) {
                        setSelectedModule(modules.get(modules.size() - 1));
                        return;
                    }
                    setSelectedModule(modules.get(modules.indexOf(selectedModule) - 1));
                    break;
                case Keyboard.KEY_RETURN:
                    getSelectedModule().setEnabled(!getSelectedModule().isEnabled());
                    break;
            }
        }
    }

    public Module.Category getCategory() {
        return category;
    }

    public TabGUI getParent() {
        return parent;
    }

    public ArrayList<SubComponent> getSubComponents() {
        return subComponents;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public float getIncY() {
        return incY;
    }

    public float getLargestString() {
        return largestString;
    }

    public String getFontChar(Module.Category cat) {
        switch (cat) {
            case COMBAT: {
                return "a";
            }
            case VISUALS: {
                return "f";
            }
            case MOVEMENT: {
                return "c";
            }
            case OTHER: {
                return "d";
            }
            case PLAYER: {
                return "e";
            }
            case EXPLOITS: {
                return "b";
            }
        }
        return "NONE";
    }
}
