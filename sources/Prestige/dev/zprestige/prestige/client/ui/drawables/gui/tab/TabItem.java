package dev.zprestige.prestige.client.ui.drawables.gui.tab;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.BindButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.ColorButton;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.ModeButton;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderUtil;

import java.awt.Color;
import java.util.Locale;
import net.minecraft.util.Identifier;

public class TabItem extends Drawable {
    public Identifier itemTexture;
    public ColorButton color;
    public BindButton bindButton;
    public ModeButton mode;
    public Category category;
    public float x;
    public float y;
    public boolean isDragged;

    public TabItem(Category category, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, null);
        this.category = category;
        itemTexture = new Identifier("prestige", "icons/other/" + category.name().toLowerCase(Locale.ROOT) + ".png");
        color = new ColorButton(Prestige.Companion.getModuleManager().getMenu().getColor(), f + 5, f2, f3 - 10, 15);
        bindButton = new BindButton(Prestige.Companion.getModuleManager().getMenu().getBind(), f + 5, f2, f3 - 10, 15);
        mode = new ModeButton(Prestige.Companion.getModuleManager().getMenu().getMode(), f + 5, f2, f3 - 10, 15);
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
        RenderUtil.renderTexturedRect(getX() + 5.5f, getY() + 3, 15, 15, itemTexture, RenderUtil.getColor(0.4f, f2));
        RenderUtil.renderTexturedRect(getX() + 5, getY() + 2.5f, 15, 15, itemTexture, RenderUtil.getColor(Color.WHITE, f2));
        font.drawString(category.name(), getX() + getWidth() / 2 - font.getStringWidth(category.name()) / 2, getY() + 10 - font.getStringHeight() / 2, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderColoredQuad(getX(), getY() + 20, getX() + getWidth(), getY() + 25, true, true, false, false, f2 * 0.4f);
        bindButton.setX(getX() + 5);
        bindButton.setY(getY() + 25.1f);
        bindButton.renderButton(n, n2, f, f2, getX(), getY(), getX() + getWidth(), getY() + getHeight());
        mode.setX(getX() + 5);
        mode.setY(getY() + 45.1f);
        mode.setSwap(false);
        mode.renderButton(n, n2, f, f2, getX(), getY(), getX() + getWidth(), getY() + getHeight());
        color.method1137(true);
        color.setX(getX() + 5);
        color.setY(getY() + 65.1f);
        //color.applyAnimation(new Animation(100, true, Easing.BACK_OUT));
        color.renderButton(n, n2, f, f2, getX(), getY(), getX() + getWidth(), getY() + getHeight());
        setHeight((getY() + 70 + color.getHeight()) - getY());
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        color.mouseClicked(d, d2, n);
        bindButton.mouseClicked(d, d2, n);
        mode.mouseClicked(d, d2, n);
        if (n == 0 && isInsideCross((int)d, (int)d2)) {
            x = (float)(getX() - d);
            y = (float)(getY() - d2);
            isDragged = true;
        }
    }

    @Override
    public void charTyped(char c, int n) {
        color.charTyped(c, n);
        bindButton.charTyped(c, n);
        mode.charTyped(c, n);
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        color.keyPressed(n, n2, n3);
        bindButton.keyPressed(n, n2, n3);
        mode.keyPressed(n, n2, n3);
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        color.mouseReleased(d, d2, n);
        bindButton.mouseReleased(d, d2, n);
        mode.mouseReleased(d, d2, n);
        if (n == 0) {
            isDragged = false;
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 20;
    }
}
