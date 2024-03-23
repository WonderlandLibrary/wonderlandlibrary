package dev.zprestige.prestige.client.ui.drawables.gui.tab;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.buttons.ModuleButton;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.util.Identifier;

public class TabCategory extends Drawable {
    public Category dispatched;
    public Identifier categoryTexture;
    public ArrayList<ModuleButton> modules= new ArrayList<>();
    public float x;
    public float y;
    public boolean isDragged;

    public TabCategory(Category category, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, null);
        dispatched = category;
        categoryTexture = new Identifier("prestige", "icons/categories/" + dispatched.name().toLowerCase(Locale.ROOT) + ".png");
        for (Module module : Prestige.Companion.getModuleManager().getModules()) {
            if (module.getCategory() == dispatched) {
                modules.add(new ModuleButton(module, f + 5, f2, f3 - 10, 15));
            }
        }
    }

    @Override
    public void render(int n, int n2, float f, float f2) {
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        if (isDragged) {
            setX(MathUtil.interpolate(getX(), n + x, Interface.getDeltaTime() * 0.005f * 4));
            setY(MathUtil.interpolate(getY(), n2 + y, Interface.getDeltaTime() * 0.005f * 4));
        }
        RenderUtil.renderColoredQuad(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(0, f2));
        RenderUtil.renderRoundedRectOutline(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(-3, f2), 0);
        Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        RenderUtil.renderColoredRoundedRect(getX() + 5, getY() + 2.5f, getX() + 20, getY() + 17.5f, 4, RenderUtil.getColor(themeColor, f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker().darker(), f2));
        RenderUtil.renderTexturedRect(getX() + 5.5f, getY() + 3, 15, 15, categoryTexture, RenderUtil.getColor(0.4f, f2));
        RenderUtil.renderTexturedRect(getX() + 5, getY() + 2.5f, 15, 15, categoryTexture, RenderUtil.getColor(Color.WHITE, f2));
        font.drawString(dispatched.name(), getX() + getWidth() / 2 - font.getStringWidth(dispatched.name()) / 2, getY() + 10 - font.getStringHeight() / 2, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderColoredQuad(getX(), getY() + 20, getX() + getWidth(), getY() + 25, true, true, false, false, f2 * 0.4f);
        float f13 = getY() + 25;
        for (ModuleButton moduleButton : modules) {
            if (Interface.getSearch().isEmpty() || moduleButton.hasModule(Interface.getSearch())) {
                moduleButton.setX(getX() + 5);
                moduleButton.setY(f13 + 0.01f);
                moduleButton.render(n, n2, f, f2);
                f13 += moduleButton.getHeight() + 5;
            }
        }
        setHeight(f13 - getY());
    }

    @Override
    public void charTyped(char c, int n) {
        for (ModuleButton moduleButton : this.modules) {
            if (Interface.getSearch().isEmpty() || moduleButton.hasModule(Interface.getSearch())) {
                moduleButton.charTyped(c, n);
            }
        }
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        for (ModuleButton moduleButton : this.modules) {
            if (Interface.getSearch().isEmpty() || moduleButton.hasModule(Interface.getSearch())) {
                moduleButton.keyPressed(n, n2, n3);
            }
        }
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        for (ModuleButton moduleButton : this.modules) {
            if (Interface.getSearch().isEmpty() || moduleButton.hasModule(Interface.getSearch())) {
                moduleButton.mouseClicked(d, d2, n);
            }
        }
        if (n == 0 && isInsideCross((int)d, (int)d2)) {
            x = (float)(getX() - d);
            y = (float)(getY() - d2);
            isDragged = true;
        }
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        for (ModuleButton moduleButton : this.modules) {
            if (Interface.getSearch().isEmpty() || moduleButton.hasModule(Interface.getSearch())) {
                moduleButton.mouseReleased(d, d2, n);
            }
        }
        if (n == 0) {
            isDragged = false;
        }
    }

    @Override
    public void clear() {
        super.clear();
        for (ModuleButton module : modules) {
            module.clear();
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 20;
    }
}