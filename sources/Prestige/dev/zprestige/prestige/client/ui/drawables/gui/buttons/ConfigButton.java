package dev.zprestige.prestige.client.ui.drawables.gui.buttons;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.protection.auth.DeleteConfig;
import dev.zprestige.prestige.client.protection.auth.GetConfigData;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.ConfigScreen;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.Color;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class ConfigButton extends Drawable {
    public String name;
    public String description;
    public String owner;
    public boolean sessionCheck;
    public Identifier downloadTexture;
    public Identifier deleteTexture;

    public ConfigButton(String string, String string2, String string3, boolean bl) {
        super(0, 0, 180, 100, "");
        this.name = string;
        this.description = string2;
        this.owner = string3;
        this.sessionCheck = bl;
        this.downloadTexture = new Identifier("prestige", "icons/download.png");
        this.deleteTexture = new Identifier("prestige", "icons/delete.png");
    }

    @Override
    public void render(int n, int n2, float f, float f2) {
        RenderUtil.setScissorRegion(getX(), ConfigScreen.getMinY() + 5, getX() + getWidth() - 2.5f, ConfigScreen.getMaxY() - 5);
        RenderUtil.renderRoundedRect(getX() - 0.5f, getY() - 0.5f, getX() + getWidth() + 0.5f, getY() + getHeight() + 0.5f, RenderUtil.getColor(0, 0.2f), 10);
        RenderUtil.renderRoundedRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(3, 1), 10);
        RenderUtil.renderCircularGradient(getX(), getY(), getX() + getWidth(), getY() + 20, RenderUtil.getColor(5, 1), 10);
        RenderUtil.renderColoredQuad(getX(), getY() + 20, getX() + getWidth(), getY() + 25, true, true, false, false, 0.4f);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        font.drawString(name, getX() + 5, getY() + 10 - font.getStringHeight() / 2, Color.WHITE);
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        String[] stringArray = description.split(" ");
        float n8 = 0;
        float n9 = 0;
        int bruh = 0;
        for (String string : stringArray) {
            float f6 = font.getStringWidth(string) * 0.8f;
            if (n8 + f6 > 170) {
                n8 = 0;
                bruh = 0;
                n9++;
            } else if(bruh != 0) {
                n8 += 1.6f;
            }
            font.drawString(string, (getX() + 5 + n8) / 0.8f, (getY() + 25.1f + n9 * 10) / 0.8f, RenderUtil.getColor(0.7f, 1));
            n8 += f6;
            bruh++;
        }
        matrixStack.pop();
        Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
        matrixStack.push();
        matrixStack.scale(0.6f, 0.6f, 0.6f);;
        font.drawString(owner, (getX() + 2.5f + (5 + font.getStringWidth(name))) / 0.6f, (getY() + 14 - font.getStringHeight() / 2 * 0.6f) / 0.6f, themeColor);
        matrixStack.pop();
        RenderUtil.renderColoredRoundedRect(getX() + getWidth() - 25, getY() + getHeight() - 25, getX() + getWidth() - 5, getY() + getHeight() - 5, 4, RenderUtil.getColor(themeColor, f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker().darker(), f2));
        if (sessionCheck && Prestige.Companion.getSession().username.equals(owner)) {
            RenderUtil.renderColoredRoundedRect(getX() + getWidth() - 50, getY() + getHeight() - 25, getX() + getWidth() - 30, getY() + getHeight() - 5, 4, RenderUtil.getColor(themeColor, f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker(), f2), RenderUtil.getColor(themeColor.darker().darker(), f2));
            RenderUtil.renderTexturedRect(getX() + getWidth() - 47.5f, getY() + getHeight() - 22.5f, 15, 15, deleteTexture, RenderUtil.getColor(241, 0.7f));
            if (isInsideDelete(n, n2)) {
                RenderUtil.renderRoundedRect(getX() + getWidth() - 50, getY() + getHeight() - 25, getX() + getWidth() - 30, getY() + getHeight() - 5,RenderUtil.getColor(0, 0.3f), 4);
            }
        }
        RenderUtil.renderTexturedRect(getX() + getWidth() - 22.5f, getY() + getHeight() - 22.5f, 15, 15, downloadTexture, RenderUtil.getColor(241, 0.7f));
        if (isInsideCreate(n, n2)) {
            RenderUtil.renderRoundedRect(getX() + getWidth() - 25, getY() + getHeight() - 25, getX() + getWidth() - 5, getY() + getHeight() - 5, RenderUtil.getColor(0, 0.3f), 4);
        }
        GL11.glDisable(3089);
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0) {
            if (isInsideCreate((int)d, (int)d2)) {
                String string = GetConfigData.run(name);
                if (string.startsWith("Internal error") || string.startsWith("Invalid")) {
                    ConfigScreen.setResponse(string);
                    ConfigScreen.setTime(System.currentTimeMillis());
                    return;
                }
                Prestige.Companion.getConfigManager().load(string);
                try {
                    ConfigScreen.setResponse("Successfully loaded config.");
                    ConfigScreen.setTime(System.currentTimeMillis());
                }
                catch (Exception exception) {
                    ConfigScreen.setResponse("Error reading config data.");
                    ConfigScreen.setTime(System.currentTimeMillis());
                }
            }
            if (isInsideDelete((int)d, (int)d2)) {
                String string = DeleteConfig.run(name);
                ConfigScreen.setResponse(string);
                ConfigScreen.setTime(System.currentTimeMillis());
                Prestige.Companion.getConfigScreen().getConfigs();
            }
        }
    }

    boolean isInsideCreate(int n, int n2) {
        return n >= getX() + getWidth() - 25 && n <= getX() + getWidth() - 5 && n2 >= getY() + getHeight() - 25 && n2 <= getY() + getHeight() - 5;
    }

    boolean isInsideDelete(int n, int n2) {
        return n >= getX() + getWidth() - 50 && n <= getX() + getWidth() - 30 && n2 >= getY() + getHeight() - 25 && n2 <= getY() + getHeight() - 5;
    }

    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }

    public String getOwner() {
        return this.owner;
    }

    public boolean isPersonal() {
        return sessionCheck;
    }
}
