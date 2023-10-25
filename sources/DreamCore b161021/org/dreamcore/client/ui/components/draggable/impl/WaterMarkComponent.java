package org.dreamcore.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.feature.impl.hud.WaterMark;
import org.dreamcore.client.feature.impl.misc.Disabler;
import org.dreamcore.client.feature.impl.misc.StreamerMode;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.components.draggable.DraggableModule;
import org.dreamcore.security.utils.LicenseUtil;

import java.awt.*;
import java.util.Objects;

public class WaterMarkComponent extends DraggableModule {

    public WaterMarkComponent() {
        super("LogoComponent", 4, 8);
    }

    @Override
    public int getWidth() {
        return 135;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!mc.gameSettings.showDebugInfo) {
            String server;
            String mode = WaterMark.logoMode.getCurrentMode();
            Color color = Color.BLACK;
            switch (WaterMark.logoColor.currentMode) {
                case "Gradient": {
                    for (int i = x; i < x + getWidth(); i++) {
                        color = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), i));
                    }
                    break;
                }
                case "Client": {
                    color = ClientHelper.getClientColor();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1, 1);
                    break;
                }
                case "Default": {
                    color = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
                }
            }
            if (mode.equalsIgnoreCase("Default")) {
                if (!HUD.font.currentMode.equals("Minecraft")) {
                    mc.robotoRegularFontRender.drawStringWithOutline("N", getX(), getY() + 1, color.getRGB());
                    mc.robotoRegularFontRender.drawStringWithOutline("ever", getX() + 7, getY() + 1, -1);
                    mc.robotoRegularFontRender.drawStringWithOutline("H", getX() + 27, getY() + 1, color.getRGB());
                    mc.robotoRegularFontRender.drawStringWithOutline("ook", getX() + 34, getY() + 1, -1);
                } else {
                    mc.fontRendererObj.drawStringWithOutline("N", getX(), getY() + 1, color.getRGB());
                    mc.fontRendererObj.drawStringWithOutline("ever", getX() + 6, getY() + 1, -1);
                    mc.fontRendererObj.drawStringWithOutline("H", getX() + 30, getY() + 1, color.getRGB());
                    mc.fontRendererObj.drawStringWithOutline("ook", getX() + 36, getY() + 1, -1);
                }
            } else if (mode.equalsIgnoreCase("Skeet")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "never" + ChatFormatting.GREEN + "hook" + ChatFormatting.RESET + " | " + (dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue() ? ChatFormatting.LIGHT_PURPLE + "Protected" + ChatFormatting.RESET : ChatFormatting.RESET + "Ya tut kryaknul pomenyay potom") + " | " + Minecraft.getDebugFPS() + "fps | " + server;
                float width = mc.fontRenderer.getStringWidth(text) + 6;
                int height = 20;
                int posX = getX();
                int posY = getY();
                RectHelper.drawRect(posX, posY, posX + width + 2.0f, posY + height, new Color(5, 5, 5, 255).getRGB());
                RectHelper.drawBorderedRect(posX + 0.5f, posY + 0.5f, posX + width + 1.5f, posY + height - 0.5f, 0.5f, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5f, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);

                switch (WaterMark.logoColor.currentMode) {
                    case "Default": {
                        RenderHelper.drawImage(new ResourceLocation("dreamcore/skeet.png"), posX + 2.5f, posY + 2.5f, width - 3.0f, 1.0f, Color.WHITE);
                        break;
                    }
                    case "Custom": {
                        RectHelper.drawGradientRect(posX + 2.75, posY + 3, getX() + width - 1, posY + 4, new Color(WaterMark.customRectTwo.getColorValue()).getRGB(), color.getRGB());
                        break;
                    }
                    case "Client": {
                        RectHelper.drawRect(posX + 3, posY + 3, getX() + width - 0.7, posY + 4, color.getRGB());
                    }
                    case "Rainbow": {
                        Color rainbow;
                        for (int i = x + 3; i < x + width; i++) {
                            rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                            RectHelper.drawRect(i, posY + 3, getX() + width - 0.7, posY + 4, rainbow.getRGB());
                        }
                    }
                    case "Gradient": {
                        RectHelper.drawGradientRect(x + 3, posY + 3, getX() + width - 0.7, posY + 4, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                    }
                    case "Static": {
                        RectHelper.drawRect(x + 3, posY + 3, getX() + width - 0.7, posY + 4, WaterMark.customRect.getColorValue());
                    }
                }
                mc.fontRenderer.drawStringWithShadow(text, posX + 4, posY + 7, -1);
            } else if (mode.equalsIgnoreCase("OneTap v2")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "dreamcore.pub | " + dreamcore.instance.name + " | " + server + " | 64 tick | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
                float width = mc.fontRenderer.getStringWidth(text) + 4;

                if (WaterMark.glowEffect.getBoolValue()) {
                    if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
                        RectHelper.drawSmoothGradientRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, color.getRGB(), WaterMark.customRectTwo.getColorValue());
                    } else {
                        RectHelper.drawSmoothGradientRect(getX(), getY(), getX() + width, getY() + 12, color.getRGB(), WaterMark.customRectTwo.getColorValue());
                    }
                }

                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() - 3, getX() + width, getY() - 1.5f, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, color.getRGB());
                            break;
                    }
                } else {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY(), getX() + width, getY() + 12, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY(), getX() + width, getY() + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY(), getX() + width, getY() + 12, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY(), getX() + width, getY() + 12, color.getRGB());
                            break;
                    }
                }

                RectHelper.drawSmoothRect(getX(), getY() - 1, getX() + width, getY() + 10, new Color(47, 47, 47).getRGB());

                mc.fontRenderer.drawStringWithShadow(text, getX() + 2, getY() + 1, -1);

                mc.fontRenderer.drawStringWithShadow(text, getX() + 2, getY() + 1, -1);
            } else if (mode.equalsIgnoreCase("OneTap v3")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "dreamcore.pub | " + dreamcore.instance.name + " | " + server + " | Fps " + Minecraft.getDebugFPS() + " | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
                float width = mc.fontRenderer.getStringWidth(text) + 4;

                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() - 3, getX() + width, getY() - 1.5f, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, color.getRGB());
                            break;
                    }
                } else {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() + 10, getX() + width, getY() + 12, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() + 10, getX() + width, getY() + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() + 10, getX() + width, getY() + 12, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() + 10, getX() + width, getY() + 12, color.getRGB());
                            break;
                    }

                    RectHelper.drawSmoothRect(getX(), getY() - 1, getX() + width, getY() + 10, new Color(23, 23, 23, 110).getRGB());

                    mc.fontRenderer.drawStringWithShadow(text, getX() + 2, getY() + 1, -1);
                }
            } else if (mode.equalsIgnoreCase("NeverLose")) {
                String text = ChatFormatting.BOLD + dreamcore.instance.name + ChatFormatting.RESET + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + "Ya tut kryaknul pomenyay potom" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (dreamcore.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + "ms" + ChatFormatting.GRAY;
                float width = mc.clickguismall.getStringWidth(text) + 1;
                RectHelper.drawSmoothRect(getX(), getY() - 2, getX() + width + 3.0f, getY() + 11, new Color(0, 0, 28).getRGB());
                mc.clickguismall.drawStringWithShadow(text, getX() + 2, getY() + 2, -1);
                mc.entityRenderer.setupOverlayRendering();
            } else if (mode.equals("NoRender")) {
                int ping;

                if (mc.isSingleplayer()) {
                    ping = 0;
                } else {
                    ping = (int) mc.getCurrentServerData().pingToServer;
                }

                mc.fontRenderer.drawStringWithShadow("dreamcore §7v" + dreamcore.instance.version + " §7[§f" + Minecraft.getDebugFPS() + " FPS§7]" + " §7[§f" + ping + " PING§7]", getX(), getY(), color.getRGB());
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (!mc.gameSettings.showDebugInfo) {
            String server;
            String mode = WaterMark.logoMode.getCurrentMode();
            Color color = Color.BLACK;
            switch (WaterMark.logoColor.currentMode) {
                case "Gradient": {
                    for (int i = x; i < x + getWidth(); i++) {
                        color = new Color(PaletteHelper.fadeColor(WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue(), i));
                    }
                    break;
                }
                case "Client": {
                    color = ClientHelper.getClientColor();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1, 1);
                    break;
                }
                case "Default": {
                    color = WaterMark.logoMode.currentMode.equals("OneTap v2") ? new Color(161, 0, 255) : Color.RED;
                }
            }
            if (mode.equalsIgnoreCase("Default")) {
                if (!HUD.font.currentMode.equals("Minecraft")) {
                    mc.robotoRegularFontRender.drawStringWithOutline("N", getX(), getY() + 1, color.getRGB());
                    mc.robotoRegularFontRender.drawStringWithOutline("ever", getX() + 7, getY() + 1, -1);
                    mc.robotoRegularFontRender.drawStringWithOutline("H", getX() + 27, getY() + 1, color.getRGB());
                    mc.robotoRegularFontRender.drawStringWithOutline("ook", getX() + 34, getY() + 1, -1);
                } else {
                    mc.fontRendererObj.drawStringWithOutline("N", getX(), getY() + 1, color.getRGB());
                    mc.fontRendererObj.drawStringWithOutline("ever", getX() + 6, getY() + 1, -1);
                    mc.fontRendererObj.drawStringWithOutline("H", getX() + 30, getY() + 1, color.getRGB());
                    mc.fontRendererObj.drawStringWithOutline("ook", getX() + 36, getY() + 1, -1);
                }
            } else if (mode.equalsIgnoreCase("Skeet")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "never" + ChatFormatting.GREEN + "hook" + ChatFormatting.RESET + " | " + (dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getBoolValue() ? ChatFormatting.LIGHT_PURPLE + "Protected" + ChatFormatting.RESET : ChatFormatting.RESET + "Ya tut kryaknul pomenyay potom") + " | " + Minecraft.getDebugFPS() + "fps | " + server;
                float width = mc.fontRenderer.getStringWidth(text) + 6;
                int height = 20;
                int posX = getX();
                int posY = getY();
                RectHelper.drawRect(posX, posY, posX + width + 2.0f, posY + height, new Color(5, 5, 5, 255).getRGB());
                RectHelper.drawBorderedRect(posX + 0.5f, posY + 0.5f, posX + width + 1.5f, posY + height - 0.5f, 0.5f, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5f, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);

                switch (WaterMark.logoColor.currentMode) {
                    case "Default": {
                        RenderHelper.drawImage(new ResourceLocation("dreamcore/skeet.png"), posX + 2.5f, posY + 2.5f, width - 3.0f, 1.0f, Color.WHITE);
                        break;
                    }
                    case "Custom": {
                        RectHelper.drawGradientRect(posX + 2.75, posY + 3, getX() + width - 1, posY + 4, new Color(WaterMark.customRectTwo.getColorValue()).getRGB(), color.getRGB());
                        break;
                    }
                    case "Client": {
                        RectHelper.drawRect(posX + 3, posY + 3, getX() + width - 0.7, posY + 4, color.getRGB());
                    }
                    case "Rainbow": {
                        Color rainbow;
                        for (int i = x + 3; i < x + width; i++) {
                            rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                            RectHelper.drawRect(i, posY + 3, getX() + width - 0.7, posY + 4, rainbow.getRGB());
                        }
                    }
                    case "Gradient": {
                        RectHelper.drawGradientRect(x + 3, posY + 3, getX() + width - 0.7, posY + 4, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                    }
                    case "Static": {
                        RectHelper.drawRect(x + 3, posY + 3, getX() + width - 0.7, posY + 4, WaterMark.customRect.getColorValue());
                    }
                }
                mc.fontRenderer.drawStringWithShadow(text, posX + 4, posY + 7, -1);
            } else if (mode.equalsIgnoreCase("OneTap v2")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "dreamcore.pub | " + dreamcore.instance.name + " | " + server + " | 64 tick | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
                float width = mc.fontRenderer.getStringWidth(text) + 4;

                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() - 3, getX() + width, getY() - 1.5f, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, color.getRGB());
                            break;
                    }
                } else {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY(), getX() + width, getY() + 12, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY(), getX() + width, getY() + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY(), getX() + width, getY() + 12, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY(), getX() + width, getY() + 12, color.getRGB());
                            break;
                    }
                }

                RectHelper.drawSmoothRect(getX(), getY() - 1, getX() + width, getY() + 10, new Color(47, 47, 47).getRGB());
                mc.fontRenderer.drawStringWithShadow(text, getX() + 2, getY() + 1, -1);
            } else if (mode.equalsIgnoreCase("OneTap v3")) {
                if (mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    server = mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String text = "dreamcore.pub | " + dreamcore.instance.name + " | " + server + " | Fps " + Minecraft.getDebugFPS() + " | " + mc.player.connection.getPlayerInfo(mc.player.getUniqueID()).getResponseTime() + "ms";
                float width = mc.fontRenderer.getStringWidth(text) + 4;


                if (WaterMark.colorRectPosition.currentMode.equals("Top")) {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() - 3, getX() + width, getY() - 1.5f, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() - 3, getX() + width, getY() - 0.5F, color.getRGB());
                            break;
                    }
                } else {
                    switch (WaterMark.logoColor.currentMode) {
                        case "Rainbow":
                            Color rainbow;
                            for (int i = x; i < x + width; i++) {
                                rainbow = PaletteHelper.rainbow(i * 15, 0.5f, 1);
                                RectHelper.drawSmoothRect(i, getY() + 10, getX() + width, getY() + 12, rainbow.getRGB());
                            }
                            break;
                        case "Gradient":
                            RectHelper.drawSmoothGradientRect(getX(), getY() + 10, getX() + width, getY() + 12, WaterMark.customRect.getColorValue(), WaterMark.customRectTwo.getColorValue());
                            break;
                        case "Static":
                            RectHelper.drawSmoothRect(getX(), getY() + 10, getX() + width, getY() + 12, WaterMark.customRect.getColorValue());
                            break;
                        case "Default":
                            RectHelper.drawSmoothRect(getX(), getY() + 10, getX() + width, getY() + 12, color.getRGB());
                            break;
                    }


                    RectHelper.drawSmoothRect(getX(), getY() - 1, getX() + width, getY() + 10, new Color(23, 23, 23, 110).getRGB());

                    mc.fontRenderer.drawStringWithShadow(text, getX() + 2, getY() + 1, -1);
                }
            } else if (mode.equalsIgnoreCase("NeverLose")) {
                String text = ChatFormatting.BOLD + dreamcore.instance.name + ChatFormatting.RESET + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + "Ya tut kryaknul pomenyay potom" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + (dreamcore.instance.featureManager.getFeatureByClass(Disabler.class).getState() ? 0 : Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + "ms" + ChatFormatting.GRAY;
                float width = mc.clickguismall.getStringWidth(text) + 1;
                RectHelper.drawSmoothRect(getX(), getY() - 2, getX() + width + 3.0f, getY() + 11, new Color(0, 0, 28).getRGB());
                mc.clickguismall.drawStringWithShadow(text, getX() + 2, getY() + 2, -1);
                mc.entityRenderer.setupOverlayRendering();
            } else if (mode.equals("NoRender")) {
                int ping;

                if (mc.isSingleplayer()) {
                    ping = 0;
                } else {
                    ping = (int) mc.getCurrentServerData().pingToServer;
                }

                mc.fontRenderer.drawStringWithShadow("dreamcore §7v" + dreamcore.instance.version + " §7[§f" + Minecraft.getDebugFPS() + " FPS§7]" + " §7[§f" + ping + " PING§7]", getX(), getY(), color.getRGB());
            }
        }
        super.draw();
    }
}