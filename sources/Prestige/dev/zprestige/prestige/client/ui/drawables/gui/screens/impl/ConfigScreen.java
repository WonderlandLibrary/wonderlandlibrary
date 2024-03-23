package dev.zprestige.prestige.client.ui.drawables.gui.screens.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.protection.Session;
import dev.zprestige.prestige.client.protection.auth.CreateConfig;
import dev.zprestige.prestige.client.protection.auth.GetConfig;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.buttons.ConfigButton;
import dev.zprestige.prestige.client.ui.drawables.gui.field.CustomTextField;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.DrawableScreen;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.impl.ModeButton;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ConfigScreen extends DrawableScreen {

    public CustomTextField name;
    public CustomTextField description;
    public ModeSetting mode;
    public ModeButton modeButton;
    public Identifier logo = new Identifier("prestige", "icons/logo.png");
    public static ArrayList<ConfigButton> configComponents = new ArrayList<>();
    public static float maxY;
    public static float minY;
    public float screenWidth = 600;
    public float screenHeight = 400;
    public static String response = "";
    public String currentMode = "Personal";
    public float scroll;
    public float idk;
    public static long time;

    public ConfigScreen() {
        name = new CustomTextField("Config Name (max 20.)");
        description = new CustomTextField("Config Description (max 50.)");
        mode = new ModeSetting("Mode", "Personal", new String[]{"Personal", "Public"});
        modeButton = new ModeButton(mode, 0, 0, 150, 20);
        getConfigs();
    }

    public void render(DrawContext drawContext, int n, int n2, float f) {
        RenderHelper.setMatrixStack(drawContext.getMatrices());
        Prestige.Companion.getFontManager().setMatrixStack(drawContext.getMatrices());
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        float f2 = width / 2 - screenWidth / 2;
        float f3 = height / 2 - screenHeight / 2;
        RenderUtil.renderRoundedRect(f2, f3, f2 + screenWidth, f3 + screenHeight, RenderUtil.getColor(0, 0.8f), 9);
        RenderUtil.renderRoundedRect(f2, f3, f2 + screenWidth, f3 + screenHeight, RenderUtil.getColor(0, 1), 10);
        RenderUtil.renderCircularGradient(f2, f3, f2 + screenWidth, f3, RenderUtil.getColor(3, 1), 10);
        RenderUtil.renderColoredQuad(f2, f3 + 25, f2 + screenWidth, f3 + 30, true, true, false, false, 0.4f);
        RenderUtil.renderTexturedRect(f2 + 5, f3 + 2.5f, 20, 20, logo, Color.WHITE);
        float f4 = f2 + screenWidth / 2;
        RenderUtil.renderRoundedRect(f4 - 105, f3 + 5, f4 - 5, f3 + 20,RenderUtil.getColor(1, 1), 5);
        RenderUtil.renderRoundedRect(f4 + 5, f3 + 5, f4 + 105, f3 + 20, RenderUtil.getColor(1, 1), 5);
        RenderUtil.renderRoundedRect(f4 - 105, f3 + 5, f4 - 5, f3 + 20, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), currentMode.equals("Personal") ? (isInsidePublic(n, n2) ? 0.3f : 0) : 1), 5);
        RenderUtil.renderRoundedRect(f4 + 5, f3 + 5, f4 + 105, f3 + 20, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), currentMode.equals("Personal") ? 1 : (isInsidePersonal(n, n2) ? 0.3f : 0)), 5);
        font.drawString("Public", f4 - 55 - font.getStringWidth("Public") / 2, f3 + 10 - font.getStringHeight() / 2, RenderUtil.getColor(0.8f, 1));
        font.drawString("Personal", f4 + 55 - font.getStringWidth("Personal") / 2, f3 + 10 - font.getStringHeight() / 2, RenderUtil.getColor(0.8f, 1));
        RenderUtil.renderCrossed(f2 + screenWidth - 17.5f, f3 + 8.75f, f2 + screenWidth - 10, f3 + 16.25f, Color.WHITE);
        RenderUtil.renderColoredQuad(f2, f3 + screenHeight - 30, f2 + screenWidth, f3 + screenHeight - 25, false, false, true, true, 0.4f);
        RenderUtil.renderColoredEllipseBorder(f2, f3 + screenHeight - 25, f2 + screenWidth, f3 + screenHeight, RenderUtil.getColor(3, 1), 10);
        name.setPosition(f2 + 10, f3 + screenHeight - 19.9f);
        name.setDimension(150, 15);
        name.setMaxLenght(20);
        name.render(n, n2, 1);
        description.setPosition(f2 + 165, f3 + screenHeight - 19.9f);
        description.setDimension(250, 15);
        description.setMaxLenght(50);
        description.render(n, n2, 1);
        RenderUtil.renderColoredQuad(f2 + 420, f3 + screenHeight - 20, f2 + 520, f3 + screenHeight - 5, RenderUtil.getColor(-2, 1));
        RenderUtil.renderColoredRectangleOutline(f2 + 420, f3 + screenHeight - 20, f2 + 520, f3 + screenHeight - 5, RenderUtil.getColor(-5, 1));
        modeButton.setX(f2 + 420);
        modeButton.setY(f3 + screenHeight - 20);
        modeButton.setWidth(100);
        modeButton.setHeight(15);
        modeButton.setSwap(false);
        modeButton.renderButton(n, n2, f, 1, 0, 0, 10000, 10000);
        RenderUtil.renderRoundedRect(f2 + 525, f3 + screenHeight - 20, f2 + screenWidth - 5, f3 + screenHeight - 5, RenderUtil.getColor(-1, 1), 5);
        RenderUtil.renderRoundedRectOutline(f2 + 525, f3 + screenHeight - 20, f2 + screenWidth - 5, f3 + screenHeight - 5, RenderUtil.getColor(-5, 1), 5);
        font.drawString("Create", f2 + 525 + (f2 + screenWidth - 5 - (f2 + 525)) / 2 - font.getStringWidth("Create") / 2, f3 + screenHeight - 15 - font.getStringHeight() / 2, RenderUtil.getColor(0.8f, 1));
        if (!response.isEmpty()) {
            font.drawString(response, f4 - font.getStringWidth(response) / 2, f3 + screenHeight + 10, Color.WHITE);
            if (System.currentTimeMillis() - time > 10000L) {
                response = "";
            }
        }
        minY = f3 + 25;
        maxY = f3 + screenHeight - 25;
        idk = MathUtil.interpolate(idk, scroll, Interface.getDeltaTime() * 0.005f);
        if (currentMode.equals("Personal")) {
            idk = 0;
            scroll = 0;
        }
        float f6 = f2 + 10;
        float f7 = f3 + 40 + idk;
        for (ConfigButton configButton : configComponents) {
            if (configButton.isPersonal() && currentMode.equals("Public")) continue;
            if (!configButton.isPersonal()) {
                String string = configButton.getOwner();
                Session session = Prestige.Companion.getSession();
                if (!string.equals(session.getUsername()) && currentMode.equals("Personal")) {
                    continue;
                }
            }
            configButton.setX(f6);
            configButton.setY(f7);
            configButton.render(n, n2, f, 1);
            if (f6 + configButton.getWidth() + 20 < f2 + screenWidth) {
                f6 += configButton.getWidth() + 20;
                continue;
            }
            f6 = f2 + 10;
            f7 += configButton.getHeight() + 10;
        }
    }

    public boolean mouseClicked(double d, double d2, int n) {
        if (n == 0) {
            if (isInsidePublic((int)d, (int)d2)) {
                currentMode = "Public";
            }
            if (isInsidePersonal((int)d, (int)d2)) {
                currentMode = "Personal";
            }
            if (isInsideCross((int)d, (int)d2)) {
                MinecraftClient.getInstance().setScreen(Prestige.Companion.getClickGUI());
            }
            if (isInsideCreate((int)d, (int)d2)) {
                if (name.getValue().length() < 3) {
                    response = "Config name is too short";
                    return false;
                }
                if (description.getValue().length() < 3) {
                    response = "Config description is too short";
                    return false;
                }
                if (name.getValue().contains(":")) {
                    response = "Config name cannot contain ':'";
                    return false;
                }
                if (name.getValue().contains("=")) {
                    response = "Config name cannot contain '='";
                    return false;
                }
                if (description.getValue().contains(":")) {
                    response = "Config description cannot contain ':'";
                    return false;
                }
                if (description.getValue().contains("=")) {
                    response = "Config description cannot contain '='";
                    return false;
                }
                response = CreateConfig.run(name.getValue(), description.getValue(), Prestige.Companion.getConfigManager().getSettings(), mode.getObject().equals("Public") ? 1 : 0);
                time = System.currentTimeMillis();
                if (response.toLowerCase().contains("successfully")) {
                    getConfigs();
                }
            }
        }
        name.mouseClicked(n, (int)d, (int)d2);
        description.mouseClicked(n, (int)d, (int)d2);
        modeButton.mouseClicked(d, d2, n);
        List<ConfigButton> list = (List<ConfigButton>) configComponents.clone();
        for (ConfigButton configButton : list) {
            if (configButton.isPersonal() && currentMode.equals("Public")) continue;
            if (!configButton.isPersonal()) {
                String string = configButton.getOwner();
                Session session = Prestige.Companion.getSession();
                if (!string.equals(session.getUsername()) && currentMode.equals("Personal")) continue;
            }
            configButton.mouseClicked(d, d2, n);
        }
        return super.mouseClicked(d, d2, n);
    }

    public boolean shouldCloseOnEsc() {
        MinecraftClient.getInstance().setScreen(Prestige.Companion.getClickGUI());
        return false;
    }

    public boolean mouseScrolled(double d, double d2, double d3) {
        scroll += (float) (d3 * 20);
        return super.mouseScrolled(d, d2, d3);
    }

    public boolean charTyped(char c, int n) {
        name.charTyped(c);
        description.charTyped(c);
        return super.charTyped(c, n);
    }

    public boolean keyPressed(int n, int n2, int n3) {
        name.keyPressed(n);
        description.keyPressed(n);
        return super.keyPressed(n, n2, n3);
    }

    public boolean shouldPause() {
        return false;
    }

    public void getConfigs() {
        String string = GetConfig.run();
        String[] stringArray = string.split(";");
        if (stringArray.length < 2) {
            response = string;
            time = System.currentTimeMillis();
            return;
        }
        configComponents.clear();
        for (String config : stringArray) {
            if (!config.isEmpty()) {
                String[] stringArray2 = config.split(":");
                configComponents.add(new ConfigButton(stringArray2[0], stringArray2[1], stringArray2[2], Integer.parseInt(stringArray2[3]) == 0));
            }
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n >= width / 2 - screenWidth / 2 + screenWidth - 17.5f && n <= width / 2 - screenWidth / 2 + screenWidth - 10 && n2 >= height / 2 - screenHeight / 2 + 8.75f && n2 <= height / 2 - screenHeight / 2 + 16.25f;
    }

    boolean isInsidePublic(int n, int n2) {
        return n >= width / 2 - screenWidth / 2 + screenWidth / 2 - 105 && n <= width / 2 - screenWidth / 2 + screenWidth / 2 - 5 && n2 >= height / 2 - screenHeight / 2 + 5 && n2 <= height / 2 - screenHeight / 2 + 20;

    }

    boolean isInsidePersonal(int n, int n2) {
        return n >= width / 2 - screenWidth / 2 + screenWidth / 2 + 5 && n <= width / 2 - screenWidth / 2 + screenWidth / 2 + 105 && n2 >= height / 2 - screenHeight / 2 + 5 && n2 <= height / 2 - screenHeight / 2 + 20;
    }

    boolean isInsideCreate(int n, int n2) {
        return n >= width / 2 - screenWidth / 2 + 525 && n <= width / 2 - screenWidth / 2 + screenWidth - 5 && n2 >= height / 2 - screenHeight / 2 + screenHeight - 20 && n2 <= height / 2 - screenHeight / 2 + screenHeight - 5;
    }

    public static float getMaxY() {
        return maxY;
    }

    public static float getMinY() {
        return minY;
    }

    public static void setResponse(String string) {
        response = string;
    }

    public static void setTime(long l) {
        time = l;
    }
}