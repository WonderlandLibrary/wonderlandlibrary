package org.dreamcore.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

public class InfoComponent extends DraggableModule {

    public InfoComponent() {
        super("InfoComponent", 100, 200);
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
        if (HUD.worldInfo.getBoolValue()) {
            String speed = String.format("%.2f " + ChatFormatting.WHITE + "blocks/sec", (MovementHelper.getSpeed() * 16) * mc.timer.timerSpeed);
            String fps = "" + Minecraft.getDebugFPS();
            String xCoord = "" + Math.round(mc.player.posX);
            String yCoord = "" + Math.round(mc.player.posY);
            String zCoord = "" + Math.round(mc.player.posZ);
            mc.robotoRegularFontRender.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(xCoord, getX() + 10, getY(), -1);
            mc.robotoRegularFontRender.drawStringWithShadow("Y: ", getX() + 30 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(yCoord, getX() + 40 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17, getY(), -1);
            mc.robotoRegularFontRender.drawStringWithShadow("Z: ", getX() + 66 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(zCoord, getX() + 76 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17, getY(), -1);

            mc.robotoRegularFontRender.drawStringWithShadow("FPS: ", getX(), getY() + 11, ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(fps, getX() + 22, getY() + 11, -1);

            mc.robotoRegularFontRender.drawStringWithShadow(speed, getX() + mc.robotoRegularFontRender.getStringWidth(fps) + 25, getY() + 11, ClientHelper.getClientColor().getRGB());
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.worldInfo.getBoolValue()) {
            String speed = String.format("%.2f " + ChatFormatting.WHITE + "blocks/sec", (MovementHelper.getSpeed() * 16) * mc.timer.timerSpeed);
            String fps = "" + Minecraft.getDebugFPS();
            String xCoord = "" + Math.round(mc.player.posX);
            String yCoord = "" + Math.round(mc.player.posY);
            String zCoord = "" + Math.round(mc.player.posZ);
            mc.robotoRegularFontRender.drawStringWithShadow("X: ", getX(), getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(xCoord, getX() + 10, getY(), -1);
            mc.robotoRegularFontRender.drawStringWithShadow("Y: ", getX() + 30 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(yCoord, getX() + 40 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 17, getY(), -1);
            mc.robotoRegularFontRender.drawStringWithShadow("Z: ", getX() + 66 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17, getY(), ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(zCoord, getX() + 76 + mc.robotoRegularFontRender.getStringWidth(xCoord) - 23 + mc.robotoRegularFontRender.getStringWidth(yCoord) - 17, getY(), -1);

            mc.robotoRegularFontRender.drawStringWithShadow("FPS: ", getX(), getY() + 11, ClientHelper.getClientColor().getRGB());
            mc.robotoRegularFontRender.drawStringWithShadow(fps, getX() + 22, getY() + 11, -1);

            mc.robotoRegularFontRender.drawStringWithShadow(speed, getX() + mc.robotoRegularFontRender.getStringWidth(fps) + 25, getY() + 11, ClientHelper.getClientColor().getRGB());
        }
        super.draw();
    }
}