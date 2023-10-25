/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import net.minecraft.client.Minecraft;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class InfoComponent
extends DraggableModule {
    public InfoComponent() {
        super("InfoComponent", 3, 345);
    }

    @Override
    public int getWidth() {
        return 75;
    }

    @Override
    public int getHeight() {
        return 27;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (HUD.worldInfo.getCurrentValue()) {
            double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
            double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
            float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
            BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
            String speed = "Speed: " + (Object)((Object)ChatFormatting.WHITE) + String.format("%.2f b/s", speedValue);
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow("FPS: " + (Object)((Object)ChatFormatting.WHITE) + Minecraft.getDebugFPS(), this.getX(), this.getY(), ClientHelper.getClientColor().getRGB());
                ClientHelper.getFontRender().drawStringWithShadow(speed, this.getX(), this.getY() + 9, ClientHelper.getClientColor().getRGB());
                ClientHelper.getFontRender().drawStringWithShadow("XYZ: " + (Object)((Object)ChatFormatting.WHITE) + Math.round(this.mc.player.posX) + " " + Math.round(this.mc.player.posY) + " " + Math.round(this.mc.player.posZ), this.getX(), this.getY() + 18, ClientHelper.getClientColor().getRGB());
            } else {
                this.mc.fontRendererObj.drawStringWithShadow("FPS: " + (Object)((Object)ChatFormatting.WHITE) + Minecraft.getDebugFPS(), this.getX(), this.getY(), ClientHelper.getClientColor().getRGB());
                this.mc.fontRendererObj.drawStringWithShadow(speed, this.getX(), this.getY() + 9, ClientHelper.getClientColor().getRGB());
                this.mc.fontRendererObj.drawStringWithShadow("XYZ: " + (Object)((Object)ChatFormatting.WHITE) + Math.round(this.mc.player.posX) + " " + Math.round(this.mc.player.posY) + " " + Math.round(this.mc.player.posZ), this.getX(), this.getY() + 18, ClientHelper.getClientColor().getRGB());
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.worldInfo.getCurrentValue()) {
            double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
            double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
            float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
            BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
            String speed = "Speed: " + (Object)((Object)ChatFormatting.WHITE) + String.format("%.2f b/s", speedValue);
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow("FPS: " + (Object)((Object)ChatFormatting.WHITE) + Minecraft.getDebugFPS(), this.getX(), this.getY(), ClientHelper.getClientColor().getRGB());
                ClientHelper.getFontRender().drawStringWithShadow(speed, this.getX(), this.getY() + 9, ClientHelper.getClientColor().getRGB());
                ClientHelper.getFontRender().drawStringWithShadow("XYZ: " + (Object)((Object)ChatFormatting.WHITE) + Math.round(this.mc.player.posX) + " " + Math.round(this.mc.player.posY) + " " + Math.round(this.mc.player.posZ), this.getX(), this.getY() + 18, ClientHelper.getClientColor().getRGB());
            } else {
                this.mc.fontRendererObj.drawStringWithShadow("FPS: " + (Object)((Object)ChatFormatting.WHITE) + Minecraft.getDebugFPS(), this.getX(), this.getY(), ClientHelper.getClientColor().getRGB());
                this.mc.fontRendererObj.drawStringWithShadow(speed, this.getX(), this.getY() + 9, ClientHelper.getClientColor().getRGB());
                this.mc.fontRendererObj.drawStringWithShadow("XYZ: " + (Object)((Object)ChatFormatting.WHITE) + Math.round(this.mc.player.posX) + " " + Math.round(this.mc.player.posY) + " " + Math.round(this.mc.player.posZ), this.getX(), this.getY() + 18, ClientHelper.getClientColor().getRGB());
            }
        }
        super.draw();
    }
}

