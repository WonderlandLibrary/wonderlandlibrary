/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.InfoBoard;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class HudInfoComponent
extends DraggableModule {
    public HudInfoComponent() {
        super("HudInfoComponent", 40, 100);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void draw() {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(InfoBoard.class).getState()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        String server = "";
        if (this.mc.isSingleplayer()) {
            server = "localhost";
        } else if (this.mc.getCurrentServerData() != null) {
            server = this.mc.getCurrentServerData().serverIP.toLowerCase();
        }
        String str = this.mc.player.getName().length() > server.length() ? this.mc.player.getName() : server;
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 55.0f + (float)this.mc.fontRenderer.getStringWidth(str);
        float bottom = top + 60.0f;
        String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
        double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
        float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
        BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
        String speed = String.format("%.2f", speedValue);
        GlStateManager.pushMatrix();
        if (InfoBoard.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), (double)(55 + this.mc.fontRenderer.getStringWidth(str) + 20), 75.0, (int)InfoBoard.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(left, top, right, bottom, new Color(5, 5, 5, 175).getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 4.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 7.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 10.0f, 15.0, 1.0, Color.WHITE.getRGB());
        if (InfoBoard.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor(), (double)((int)x), (double)((int)y), (double)(55 + this.mc.fontRenderer.getStringWidth(str)), 3.0, 20);
        }
        RectHelper.drawSmoothRect(x, y, right, y - 1.0f, ClientHelper.getClientColor().getRGB());
        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
        RenderHelper.drawImage(new ResourceLocation("celestial/info/time.png"), x + 2.0f, y + 17.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/speed.png"), x + 2.0f, y + 28.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/name.png"), x + 2.0f, y + 38.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/ip.png"), x + 2.0f, y + 48.0f, 10.0f, 9.0f, Color.WHITE);
        this.mc.comfortaa.drawStringWithShadow("Info Board", x + 20.0f, y + 5.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Time: " + dateFormat, x + 14.0f, y + 19.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Speed: " + speed, (double)x + 14.5, y + 29.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Name: " + this.mc.player.getName(), x + 15.0f, y + 39.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("IP: " + server, x + 15.0f, y + 49.0f, -1);
        GlStateManager.popMatrix();
        super.draw();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(InfoBoard.class).getState()) {
            return;
        }
        String server = "";
        if (this.mc.isSingleplayer()) {
            server = "localhost";
        } else if (this.mc.getCurrentServerData() != null) {
            server = this.mc.getCurrentServerData().serverIP.toLowerCase();
        }
        String str = this.mc.player.getName().length() > server.length() ? this.mc.player.getName() : server;
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 55.0f + (float)this.mc.fontRenderer.getStringWidth(str);
        float bottom = top + 60.0f;
        String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
        double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
        float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
        BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
        String speed = String.format("%.2f", speedValue);
        GlStateManager.pushMatrix();
        if (InfoBoard.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), (double)(55 + this.mc.fontRenderer.getStringWidth(str) + 20), 75.0, (int)InfoBoard.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(left, top, right, bottom, new Color(5, 5, 5, 175).getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 4.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 7.0f, 15.0, 1.0, Color.WHITE.getRGB());
        RectHelper.drawRectBetter(x + 3.0f, y + 10.0f, 15.0, 1.0, Color.WHITE.getRGB());
        if (InfoBoard.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor(), (double)((int)x), (double)((int)y), (double)(55 + this.mc.fontRenderer.getStringWidth(str)), 3.0, 20);
        }
        RectHelper.drawSmoothRect(x, y, right, y - 1.0f, ClientHelper.getClientColor().getRGB());
        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
        RenderHelper.drawImage(new ResourceLocation("celestial/info/time.png"), x + 2.0f, y + 17.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/speed.png"), x + 2.0f, y + 28.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/name.png"), x + 2.0f, y + 38.0f, 10.0f, 9.0f, Color.WHITE);
        RenderHelper.drawImage(new ResourceLocation("celestial/info/ip.png"), x + 2.0f, y + 48.0f, 10.0f, 9.0f, Color.WHITE);
        this.mc.comfortaa.drawStringWithShadow("Info Board", x + 20.0f, y + 5.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Time: " + dateFormat, x + 14.0f, y + 19.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Speed: " + speed, (double)x + 14.5, y + 29.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("Name: " + this.mc.player.getName(), x + 15.0f, y + 39.0f, -1);
        this.mc.fontRenderer.drawStringWithShadow("IP: " + server, x + 15.0f, y + 49.0f, -1);
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }
}

