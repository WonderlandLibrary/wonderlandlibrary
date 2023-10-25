/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.misc.TpsHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class ClientInfoComponent
extends DraggableModule {
    public ClientInfoComponent() {
        super("ClientInfoComponent", 801, 366);
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (HUD.clientInfo.getCurrentValue()) {
            String buildStr = (Object)((Object)ChatFormatting.GRAY) + "Build - " + (Object)((Object)ChatFormatting.WHITE) + Celestial.version + (Object)((Object)ChatFormatting.GRAY) + " | User - " + (Object)((Object)ChatFormatting.WHITE) + Celestial.instance.getLicenseName();
            String tpsStr = (Object)((Object)ChatFormatting.GRAY) + "TPS - " + (Object)((Object)ChatFormatting.WHITE) + MathematicHelper.round(TpsHelper.getTickRate(), 1);
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow(buildStr, this.getX(), this.getY(), -1);
                ClientHelper.getFontRender().drawStringWithShadow(tpsStr, this.getX() + ClientHelper.getFontRender().getStringWidth(buildStr) - 1 - ClientHelper.getFontRender().getStringWidth(tpsStr), this.getY() - 10, -1);
            } else {
                this.mc.fontRendererObj.drawStringWithShadow(buildStr, this.getX(), this.getY(), -1);
                this.mc.fontRendererObj.drawStringWithShadow(tpsStr, this.getX() + this.mc.fontRendererObj.getStringWidth(buildStr) - 1 - this.mc.fontRendererObj.getStringWidth(tpsStr), this.getY() - 10, -1);
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.clientInfo.getCurrentValue()) {
            String buildStr = (Object)((Object)ChatFormatting.GRAY) + "Build - " + (Object)((Object)ChatFormatting.WHITE) + Celestial.version + (Object)((Object)ChatFormatting.GRAY) + " | User - " + (Object)((Object)ChatFormatting.WHITE) + Celestial.instance.getLicenseName();
            String tpsStr = (Object)((Object)ChatFormatting.GRAY) + "TPS - " + (Object)((Object)ChatFormatting.WHITE) + MathematicHelper.round(TpsHelper.getTickRate(), 1);
            if (!ClientFont.minecraftFont.getCurrentValue()) {
                ClientHelper.getFontRender().drawStringWithShadow(buildStr, this.getX(), this.getY(), -1);
                ClientHelper.getFontRender().drawStringWithShadow(tpsStr, this.getX() + ClientHelper.getFontRender().getStringWidth(buildStr) - 1 - ClientHelper.getFontRender().getStringWidth(tpsStr), this.getY() - 10, -1);
            } else {
                this.mc.fontRendererObj.drawStringWithShadow(buildStr, this.getX(), this.getY(), -1);
                this.mc.fontRendererObj.drawStringWithShadow(tpsStr, this.getX() + this.mc.fontRendererObj.getStringWidth(buildStr) - 1 - this.mc.fontRendererObj.getStringWidth(tpsStr), this.getY() - 10, -1);
            }
        }
        super.draw();
    }
}

