/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.feature.impl.misc.Disabler;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class LogoComponent
extends DraggableModule {
    public LogoComponent() {
        super("LogoComponent", 2, 2);
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
        if (HUD.logo.getCurrentValue() && !this.mc.gameSettings.showDebugInfo) {
            int y;
            String mode = HUD.logoMode.getCurrentMode();
            Color color = Color.BLACK;
            switch (HUD.logoColor.currentMode) {
                case "Custom": {
                    color = new Color(HUD.customRect.getColor());
                    break;
                }
                case "Client": {
                    color = ClientHelper.getClientColor();
                    break;
                }
                case "Original": {
                    color = Color.WHITE;
                }
            }
            if (mode.equalsIgnoreCase("Skeet")) {
                String server;
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String text = Celestial.name + " | " + this.mc.player.getName() + " | " + server + " | " + dateFormat;
                float width = this.mc.fontRenderer.getStringWidth(text) + 6;
                int height = 20;
                int posX = this.getX();
                int posY = this.getY();
                RectHelper.drawRect(posX, posY, (float)posX + width + 2.0f, posY + height, new Color(5, 5, 5, 255).getRGB());
                RectHelper.drawBorderedRect((double)posX + 0.5, (double)posY + 0.5, (double)((float)posX + width) + 1.5, (double)(posY + height) - 0.5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawBorderedRect(posX + 2, posY + 2, (float)posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawRect((double)posX + 2.5, (double)posY + 2.5, (double)((float)posX + width) - 0.5, (double)posY + 4.5, new Color(9, 9, 9, 255).getRGB());
                RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), (float)posX + 2.5f, (float)posY + 2.5f, width - 3.0f, 1.0f, color);
                this.mc.fontRenderer.drawStringWithShadow(text, 4 + posX, 7 + posY, -1);
            } else if (mode.equalsIgnoreCase("OneTap")) {
                String server;
                int x = this.getX();
                y = this.getY();
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String str = Celestial.instance.getLicenseName();
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String text = "celestial.pub | " + str + " | " + server + " | FPS: " + Minecraft.getDebugFPS() + " | " + dateFormat;
                float width = this.mc.tahoma.getStringWidth(text) + 6;
                RectHelper.drawRect(x, y - 1, (float)x + width + 2.0f, y + 12, new Color(5, 5, 5, 145).getRGB());
                RectHelper.drawGradientSideways(x, (float)y - 1.1f, (float)x + width + 2.0f, (float)y + 0.1f, color.getRGB(), color.darker().darker().getRGB());
                this.mc.tahoma.drawStringWithShadow(text, x + 3, (float)y + 3.5f, new Color(225, 225, 225, 255).getRGB());
            } else if (mode.equalsIgnoreCase("NeverLose")) {
                int posX = this.getX();
                int posY = this.getY();
                int ping = Objects.requireNonNull(this.mc.getConnection()).getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime();
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String clientName = Celestial.name;
                String info = " | " + ping + " ms | FPS " + Minecraft.getDebugFPS() + " | " + this.mc.player.getName() + " | " + dateFormat;
                RenderHelper.renderBlurredShadow(Color.BLACK, (double)(posX - 2), (double)posY, (double)(this.mc.museo.getStringWidth(clientName.toUpperCase()) + this.mc.tahoma.getStringWidth(info) + 9), 13.0, 15);
                RectHelper.drawSmoothRectBetter(posX - 2, posY, this.mc.museo.getStringWidth(clientName.toUpperCase()) + this.mc.tahoma.getStringWidth(info) + 7, 13.0, new Color(20, 20, 20, 255).getRGB());
                this.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX, posY + 3, new Color(96, 158, 190).getRGB());
                this.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX + 1, posY + 3, -1);
                this.mc.tahoma.drawStringWithShadow(info, posX + this.mc.museo.getStringWidth(clientName.toUpperCase()) + 2, posY + 4, -1);
            } else if (mode.equalsIgnoreCase("New")) {
                int x = this.getX();
                y = this.getY();
                String str = Celestial.instance.getLicenseName();
                double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
                double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
                float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
                BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
                String speed = String.format("%.2f", speedValue);
                String text = "Celestial. | Build " + Celestial.version + " | " + str + " | FPS: " + Minecraft.getDebugFPS() + " | BPS: " + speed;
                float width = this.mc.robotoRegularFontRender.getStringWidth(text) + 6;
                RectHelper.drawRect(x, y - 1, (float)x + width + 2.0f, y + 12, new Color(5, 5, 5, 145).getRGB());
                if (HUD.logoGlow.getCurrentValue()) {
                    RenderHelper.renderBlurredShadow(color.brighter(), (double)x, (double)(y - 1), (double)((int)width + 2), 5.0, (int)HUD.glowRadius.getCurrentValue());
                }
                if (HUD.logoColor.currentMode.equals("Original")) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), x, (float)y - 1.1f, width + 2.0f, 0.8f, Color.WHITE);
                } else {
                    RectHelper.drawGradientSideways(x, (float)y - 1.1f, (float)x + width + 2.0f, (float)y + 0.1f, color.getRGB(), color.darker().darker().getRGB());
                }
                this.mc.robotoRegularFontRender.drawStringWithShadow(text, x + 3, (float)y + 3.0f, new Color(225, 225, 225, 255).getRGB());
            } else if (mode.equalsIgnoreCase("Default")) {
                int x = this.getX();
                y = this.getY();
                String check = Calendar.getInstance().getTime().getMinutes() < 9 ? "0" : "";
                String date = Calendar.getInstance().getTime().getHours() + ":" + check + Calendar.getInstance().getTime().getMinutes();
                if (!ClientFont.minecraftFont.getCurrentValue()) {
                    ClientHelper.getFontRender().drawStringWithShadow(Celestial.name + (Object)((Object)ChatFormatting.GRAY) + "(" + (Object)((Object)ChatFormatting.WHITE) + date + (Object)((Object)ChatFormatting.GRAY) + ")", x + 5, y + 2, color.getRGB());
                } else {
                    this.mc.fontRendererObj.drawStringWithShadow(Celestial.name + (Object)((Object)ChatFormatting.GRAY) + "(" + (Object)((Object)ChatFormatting.WHITE) + date + (Object)((Object)ChatFormatting.GRAY) + ")", x + 5, y + 2, color.getRGB());
                }
            } else if (mode.equalsIgnoreCase("Dev")) {
                String server;
                Color color1 = Color.BLACK;
                switch (HUD.logoColor.currentMode) {
                    case "Custom": {
                        color1 = new Color(HUD.customRect.getColor());
                        break;
                    }
                    case "Client": {
                        color1 = ClientHelper.getClientColor();
                        break;
                    }
                    case "Original": {
                        color1 = Color.WHITE;
                    }
                }
                int x = this.getX();
                int y2 = this.getY();
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                int ping = Celestial.instance.featureManager.getFeatureByClass(Disabler.class).getState() && Disabler.mode.currentMode.equals("Old MatrixVl") ? 0 : Objects.requireNonNull(this.mc.getConnection()).getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime();
                String text = "Celestial " + Celestial.status + " | " + server + " | Ping " + ping;
                float width = this.mc.fontRenderer.getStringWidth(text) + 6;
                int top = y2;
                int left = x;
                if (HUD.logoGlow.getCurrentValue()) {
                    RenderHelper.renderBlurredShadow(color1, (double)(left - 5), (double)top, (double)((int)width + 15), 9.0, 25);
                }
                RectHelper.drawRect(x, y2, (float)x + width + 2.0f, y2 + 11, new Color(5, 5, 5, 120).getRGB());
                this.mc.fontRenderer.drawStringWithShadow(text, x + 2, y2 + 2, -1);
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.logo.getCurrentValue() && !this.mc.gameSettings.showDebugInfo) {
            int y;
            String mode = HUD.logoMode.getCurrentMode();
            Color color = Color.BLACK;
            switch (HUD.logoColor.currentMode) {
                case "Custom": {
                    color = new Color(HUD.customRect.getColor());
                    break;
                }
                case "Client": {
                    color = ClientHelper.getClientColor();
                    break;
                }
                case "Original": {
                    color = Color.WHITE;
                }
            }
            if (mode.equalsIgnoreCase("Skeet")) {
                String server;
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String text = Celestial.name + " | " + this.mc.player.getName() + " | " + server + " | " + dateFormat;
                float width = this.mc.fontRenderer.getStringWidth(text) + 6;
                int height = 20;
                int posX = this.getX();
                int posY = this.getY();
                RectHelper.drawRect(posX, posY, (float)posX + width + 2.0f, posY + height, new Color(5, 5, 5, 255).getRGB());
                RectHelper.drawBorderedRect((double)posX + 0.5, (double)posY + 0.5, (double)((float)posX + width) + 1.5, (double)(posY + height) - 0.5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawBorderedRect(posX + 2, posY + 2, (float)posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                RectHelper.drawRect((double)posX + 2.5, (double)posY + 2.5, (double)((float)posX + width) - 0.5, (double)posY + 4.5, new Color(9, 9, 9, 255).getRGB());
                RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), (float)posX + 2.5f, (float)posY + 2.5f, width - 3.0f, 1.0f, color);
                this.mc.fontRenderer.drawStringWithShadow(text, 4 + posX, 7 + posY, -1);
            } else if (mode.equalsIgnoreCase("OneTap")) {
                String server;
                int x = this.getX();
                y = this.getY();
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                String str = Celestial.instance.getLicenseName();
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String text = "celestial.pub | " + str + " | " + server + " | FPS: " + Minecraft.getDebugFPS() + " | " + dateFormat;
                float width = this.mc.tahoma.getStringWidth(text) + 6;
                RectHelper.drawRect(x, y - 1, (float)x + width + 2.0f, y + 12, new Color(5, 5, 5, 145).getRGB());
                RectHelper.drawGradientSideways(x, (float)y - 1.1f, (float)x + width + 2.0f, (float)y + 0.1f, color.getRGB(), color.darker().darker().getRGB());
                this.mc.tahoma.drawStringWithShadow(text, x + 3, (float)y + 3.5f, new Color(225, 225, 225, 255).getRGB());
            } else if (mode.equalsIgnoreCase("NeverLose")) {
                int posX = this.getX();
                int posY = this.getY();
                int ping = Objects.requireNonNull(this.mc.getConnection()).getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime();
                String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String clientName = Celestial.name;
                String info = " | " + ping + " ms | FPS " + Minecraft.getDebugFPS() + " | " + this.mc.player.getName() + " | " + dateFormat;
                RenderHelper.renderBlurredShadow(Color.BLACK, (double)(posX - 2), (double)posY, (double)(this.mc.museo.getStringWidth(clientName.toUpperCase()) + this.mc.tahoma.getStringWidth(info) + 9), 13.0, 15);
                RectHelper.drawSmoothRectBetter(posX - 2, posY, this.mc.museo.getStringWidth(clientName.toUpperCase()) + this.mc.tahoma.getStringWidth(info) + 7, 13.0, new Color(20, 20, 20, 255).getRGB());
                this.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX, posY + 3, new Color(96, 158, 190).getRGB());
                this.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX + 1, posY + 3, -1);
                this.mc.tahoma.drawStringWithShadow(info, posX + this.mc.museo.getStringWidth(clientName.toUpperCase()) + 2, posY + 4, -1);
            } else if (mode.equalsIgnoreCase("Default")) {
                int x = this.getX();
                y = this.getY();
                String check = Calendar.getInstance().getTime().getMinutes() < 9 ? "0" : "";
                String date = Calendar.getInstance().getTime().getHours() + ":" + check + Calendar.getInstance().getTime().getMinutes();
                if (!ClientFont.minecraftFont.getCurrentValue()) {
                    ClientHelper.getFontRender().drawStringWithShadow(Celestial.name + (Object)((Object)ChatFormatting.GRAY) + "(" + (Object)((Object)ChatFormatting.WHITE) + date + (Object)((Object)ChatFormatting.GRAY) + ")", x + 5, y + 2, color.getRGB());
                } else {
                    this.mc.fontRendererObj.drawStringWithShadow(Celestial.name + (Object)((Object)ChatFormatting.GRAY) + "(" + (Object)((Object)ChatFormatting.WHITE) + date + (Object)((Object)ChatFormatting.GRAY) + ")", x + 5, y + 2, color.getRGB());
                }
            } else if (mode.equalsIgnoreCase("Dev")) {
                String server;
                Color color1 = Color.BLACK;
                switch (HUD.logoColor.currentMode) {
                    case "Custom": {
                        color1 = new Color(HUD.customRect.getColor());
                        break;
                    }
                    case "Client": {
                        color1 = ClientHelper.getClientColor();
                        break;
                    }
                    case "Original": {
                        color1 = Color.WHITE;
                    }
                }
                int x = this.getX();
                int y2 = this.getY();
                if (this.mc.isSingleplayer()) {
                    server = "localhost";
                } else {
                    assert (this.mc.getCurrentServerData() != null);
                    server = this.mc.getCurrentServerData().serverIP.toLowerCase();
                }
                int ping = Celestial.instance.featureManager.getFeatureByClass(Disabler.class).getState() && Disabler.mode.currentMode.equals("Old MatrixVl") ? 0 : Objects.requireNonNull(this.mc.getConnection()).getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime();
                String text = "Celestial " + Celestial.status + " | " + server + " | Ping " + ping;
                float width = this.mc.fontRenderer.getStringWidth(text) + 6;
                int top = y2;
                int left = x;
                if (HUD.logoGlow.getCurrentValue()) {
                    RenderHelper.renderBlurredShadow(color1, (double)(left - 5), (double)top, (double)((int)width + 15), 9.0, 25);
                }
                RectHelper.drawRect(x, y2, (float)x + width + 2.0f, y2 + 11, new Color(5, 5, 5, 120).getRGB());
                this.mc.fontRenderer.drawStringWithShadow(text, x + 2, y2 + 2, -1);
            } else if (mode.equalsIgnoreCase("New")) {
                int x = this.getX();
                y = this.getY();
                String str = Celestial.instance.getLicenseName();
                double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
                double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
                float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
                BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
                String speed = String.format("%.2f", speedValue);
                String text = "Celestial. | Build " + Celestial.version + " | " + str + " | FPS: " + Minecraft.getDebugFPS() + " | BPS: " + speed;
                float width = this.mc.robotoRegularFontRender.getStringWidth(text) + 6;
                RectHelper.drawRect(x, y - 1, (float)x + width + 2.0f, y + 12, new Color(5, 5, 5, 145).getRGB());
                if (HUD.logoGlow.getCurrentValue()) {
                    RenderHelper.renderBlurredShadow(color.brighter(), (double)x, (double)(y - 1), (double)((int)width + 2), 5.0, (int)HUD.glowRadius.getCurrentValue());
                }
                if (HUD.logoColor.currentMode.equals("Original")) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), x, (float)y - 1.1f, width + 2.0f, 0.8f, Color.WHITE);
                } else {
                    RectHelper.drawGradientSideways(x, (float)y - 1.1f, (float)x + width + 2.0f, (float)y + 0.1f, color.getRGB(), color.darker().darker().getRGB());
                }
                this.mc.robotoRegularFontRender.drawStringWithShadow(text, x + 3, (float)y + 3.0f, new Color(225, 225, 225, 255).getRGB());
            }
        }
        super.draw();
    }
}

