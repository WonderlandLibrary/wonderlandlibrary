package dev.zprestige.prestige.client.ui.drawables.gui.tab;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.ConfigScreen;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.Color;
import java.util.Locale;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TabConfig extends Drawable {

    public Category category;
    public Identifier tabTexture;
    public float x;
    public float y;
    public boolean isDragged;

    public TabConfig(Category category, float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, null);
        this.category = category;
        tabTexture = new Identifier("prestige", "icons/other/" + category.name().toLowerCase(Locale.ROOT) + ".png");
    }

    @Override
    public void render(int n, int n2, float f, float f2) {
        if (isDragged) {
            float f3 = (float)n + x;
            float f4 = (float)n2 + y;
            setX(MathUtil.interpolate(getX(), f3, Interface.getDeltaTime() * 0.005f * 4));
            setY(MathUtil.interpolate(getY(), f4, Interface.getDeltaTime() * 0.005f * 4));
        }
        RenderUtil.renderColoredQuad(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(0, f2));
        RenderUtil.renderRoundedRectOutline(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(-3, f2), 0);
        Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        RenderUtil.renderColoredRoundedRect(getX() + 5, getY() + 2.5f, getX() + 20, getY() + 17.5f, 4, RenderUtil.getColor(themeColor, f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker().darker(), f2));
        RenderUtil.renderTexturedRect(getX() + 5.5f, getY() + 3, 15, 15, tabTexture, RenderUtil.getColor(0.4f, f2));
        RenderUtil.renderTexturedRect(getX() + 5, getY() + 2.5f, 15, 15, tabTexture, RenderUtil.getColor(Color.WHITE, f2));
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        font.drawString(category.name(), getX() + getWidth() / 2 - font.getStringWidth(category.name()) / 2, getY() + 10 - font.getStringHeight() / 2, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderColoredQuad(getX(), getY() + 20, getX() + getWidth(), getY() + 25, true, true, false, false, f2 * 0.4f);
        RenderUtil.renderColoredQuad(getX() + 5, getY() + 25, getX() + getWidth() - 5, getY() + 40, RenderUtil.getColor(-1, f2));
        RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 25, getX() + getWidth() - 5, getY() + 40, RenderUtil.getColor(-5, f2), 0);
        if (isInsideButton(n, n2)) {
            RenderUtil.renderColoredQuad(getX() + 5, getY() + 25, getX() + getWidth() - 5, getY() + 40, RenderUtil.getColor(Color.BLACK, f2 * 0.2f));
        }
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        font.drawString("Browse Configs", (getX() + getWidth() / 2 - Prestige.Companion.getFontManager().getFontRenderer().getStringWidth("Browse Configs") / 2 * 0.8f) / 0.8f, (getY() + 35 - Prestige.Companion.getFontManager().getFontRenderer().getStringHeight() * 0.8f) / 0.8f, RenderUtil.getColor(Color.WHITE, f2));
        matrixStack.pop();
        setHeight(45);
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0) {
            if (isInsideCross((int)d, (int)d2)) {
                x = (float)(getX() - d);
                y = (float)(getY() - d2);
                isDragged = true;
            }
            if (isInsideButton((int)d, (int)d2)) {
                Prestige.Companion.setConfigScreen(new ConfigScreen());
                MinecraftClient.getInstance().setScreen(Prestige.Companion.getConfigScreen());
            }
        }
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        if (n == 0) {
            isDragged = false;
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 20;
    }

    boolean isInsideButton(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() + 25 && n2 < getY() + 40;
    }
}
