package dev.zprestige.prestige.client.ui;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.handler.impl.KeybindHanlder;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.DrawableScreen;
import dev.zprestige.prestige.client.ui.drawables.gui.tab.TabCategory;
import dev.zprestige.prestige.client.ui.drawables.gui.tab.TabConfig;
import dev.zprestige.prestige.client.ui.drawables.gui.tab.TabItem;
import dev.zprestige.prestige.client.ui.drawables.gui.tab.TabSocial;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.MC;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Interface extends DrawableScreen implements MC {

    public ArrayList<Drawable> tabs = new ArrayList<>();
    public Identifier socialImage = new Identifier("prestige", "icons/other/social.png");
    public Identifier searchImage = new Identifier("prestige", "icons/search.png");
    public TabSocial field842;
    public static String search = "";
    public boolean notLoaded = true;
    public boolean isSocial;
    public boolean inSearch;
    public static long time;
    public static long deltaTime;
    public long animation;
    public boolean setPos;
    public static Drawable drawable;
    public float scroll;
    public float y;
    public float idk;

    public Interface() {
        this.field842 = new TabSocial(0, 0, 150, 200);
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    public void render(DrawContext drawContext, int n, int n2, float f) {
        float f2 = (float)getMc().getWindow().getScaledWidth() / 2 - (float)Category.values().length * 65 + 5;
        if (notLoaded) {
            for (Category category : Category.values()) {
                switch (category) {
                    case Menu -> tabs.add(new TabItem(category, f2, 5, 120, 20));
                    case Configs -> tabs.add(new TabConfig(category, f2, 5, 120, 20));
                    default -> tabs.add(new TabCategory(category, f2, 5, 120, 20));
                }
                f2 += 130;
            }
            notLoaded = false;
        }
        if (setPos) {
            f2 = (float)getMc().getWindow().getScaledWidth() / 2.0f - (float)Category.values().length * 65.0f + 5.0f;
            for (Drawable drawable : tabs) {
                drawable.setX(f2);
                drawable.setY(5.0f);
                drawable.setWidth(120.0f);
                f2 += 130.0f;
            }
            setPos = false;
        }
        Prestige.Companion.getFontManager().setMatrixStack(drawContext.getMatrices());
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        RenderHelper.setMatrixStack(drawContext.getMatrices());
        deltaTime = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        if (Interface.getDeltaTime() * 0.005f > 1.0f) {
            return;
        }
        idk = MathUtil.interpolate(idk, 1, Interface.getDeltaTime() * 0.005f);
        y = MathUtil.interpolate(y, scroll, Interface.getDeltaTime() * 0.005f * 2);
        RenderUtil.renderRoundedRect((float)width / 2 - 100, (float)height - 30, (float)width / 2 + 100, (float)height - 10, RenderUtil.getColor(0, idk), 7);
        RenderUtil.renderRoundedRectOutline((float)width / 2 - 100, (float)height - 30, (float)width / 2 + 100, (float)height - 10, RenderUtil.getColor(-3, idk), 7);
        Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        RenderUtil.renderColoredRoundedRect((float)width / 2 + 80, (float)height - 27.5f, (float)width / 2 + 95, (float)height - 12.5f, 4, RenderUtil.getColor(themeColor, idk), RenderUtil.getColor(themeColor.darker(), idk), RenderUtil.getColor(themeColor.darker(), idk), RenderUtil.getColor(themeColor.darker().darker(), idk));
        RenderUtil.renderTexturedRect((float)width / 2 + 82.5f, (float)height - 25, 10, 10, searchImage, RenderUtil.getColor(Color.WHITE, idk));
        RenderUtil.setScissorRegion((float)width / 2 - 100, (float)height - 30, (float)width / 2 + 77.5f, (float)height - 10);
        String string;
        if (search.isEmpty()) {
            string = "Search";
        } else {
            string = search;
        }
        float f9 = 0.9f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f9, f9, f9);
        font.drawString(string + (inSearch ? typingIcon() : ""), ((float)width / 2 - 90) / f9, ((float)height - 27.6f) / f9, RenderUtil.getColor(search.isEmpty() ? 0.6f : 1.0f, idk));
        matrixStack.pop();
        GL11.glDisable(3089);
        renderSocialsButton();
        if (isSocial) {
            field842.setX(width / 2 + 130);
            field842.setY(height - 235);
            field842.render(n, n2, f, 1);
        }
        drawable = null;
        for (Drawable drawable : tabs) {
            drawable.setY(drawable.getY() + y);
            drawable.render(n, n2, f, idk);
            drawable.setY(drawable.getY() - y);
        }
        if (drawable != null && drawable.getText() != null) {
            if (Prestige.Companion.getModuleManager().getMenu().getMode().getObject().equals("Mouse")) {;
                font.drawString(drawable.getText(), (float) n + 5, (float) n2 + 5, RenderUtil.getColor(230, idk));
            } else {
                font.drawString(drawable.getText(), (float) width / 2 - font.getStringWidth(drawable.getText()) / 2, (float) height - 45.1f, RenderUtil.getColor(230, idk));
            }
        }
    }

    void renderSocialsButton() {
        RenderUtil.renderRoundedRect(width / 2 + 105, height - 30, width / 2 + 125f, height - 10, RenderUtil.getColor(0, idk), 7);
        RenderUtil.renderRoundedRectOutline(width / 2 + 105, height - 30, width / 2 + 125f, height - 10, RenderUtil.getColor(-3, idk), 7);
        Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        RenderUtil.renderColoredRoundedRect(width / 2 + 107.5f, height - 27.5f, width / 2 + 122.5f, height - 12.5f, 4.0f, RenderUtil.getColor(themeColor, idk), RenderUtil.getColor(themeColor.darker(), idk), RenderUtil.getColor(themeColor.darker(), idk), RenderUtil.getColor(themeColor.darker().darker(), idk));
        RenderUtil.renderTexturedRect(width / 2 + 110, height - 25, 10, 10, socialImage, RenderUtil.getColor(Color.WHITE, idk));
    }

    @Override
    public void close() {
        idk = 0.0f;
        getMc().getWindow().setScaleFactor(KeybindHanlder.getScale());
        super.close();
    }

    @Override
    public boolean charTyped(char c, int n) {
        for (Drawable drawable : tabs) {
            drawable.charTyped(c, n);
        }
        if (inSearch && c != '§' && c >= ' ' && c != '') {
            search = search + c;
        }
        if (isSocial) {
            field842.charTyped(c, n);
        }
        return super.charTyped(c, n);
    }

    @Override
    public boolean mouseClicked(double d, double d2, int n) {
        for (Drawable drawable : tabs) {
            drawable.mouseClicked(d, d2, n);
        }
        if (n == 0) {
            if (isInsideSearch(d, d2)) {
                inSearch = !inSearch;
            }
            if (isInsideSocials(d, d2)) {
                isSocial = !isSocial;
            }
        }
        if (isSocial) {
            this.field842.mouseClicked(d, d2, n);
        }
        return super.mouseClicked(d, d2, n);
    }

    @Override
    public boolean mouseReleased(double d, double d2, int n) {
        for (Drawable drawable : tabs) {
            drawable.mouseReleased(d, d2, n);
        }
        return super.mouseReleased(d, d2, n);
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        for (Drawable drawable : tabs) {
            drawable.keyPressed(n, n2, n3);
        }
        if (inSearch) {
            switch (n) {
                case 256, 257 -> inSearch = false;
            }
            if (n == 259 && !Interface.search.isEmpty()) {
                Interface.search = Interface.search.substring(0, Interface.search.length() - 1);
            }
        }
        if (this.isSocial) {
            this.field842.keyPressed(n, n2, n3);
        }
        return super.keyPressed(n, n2, n3);
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d3) {
        scroll += (float)d3 * 10;
        return super.mouseScrolled(d, d2, d3);
    }

    String typingIcon() {
        if (System.currentTimeMillis() - animation > 1000L) {
            animation = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - animation > 500L ? "|" : "";
    }

    boolean isInsideSearch(double n, double n2) {
        return n > (double) width / 2 - 100 && n < (double) width / 2 + 100 && n2 > height - 30 && n2 < height - 10;
    }

    boolean isInsideSocials(double n, double n2) {
        return n > (double) width / 2 + 105 && n < (double) width / 2 + 125 && n2 > height - 30 && n2 < height - 10;
    }

    public void onSelfDestruct() {
        for (Drawable drawable : tabs) {
            drawable.setText(null);
            drawable.clear();
        }
    }

    public static String getSearch() {
        return search;
    }

    public static void setHover(Drawable d) {
        drawable = d;
    }

    public static long getDeltaTime() {
        return deltaTime;
    }

    public void setInitPos(boolean bl) {
        setPos = bl;
    }
}