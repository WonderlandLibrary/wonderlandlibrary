package dev.rise.ui.auth;

import dev.rise.util.InstanceAccess;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public final class CannotConnectToRiseServersGUI extends GuiScreen implements InstanceAccess {

    public void initGui() {

    }

    @Override
    public void onGuiClosed() {

    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        final String text = "Cannot connect to Rise servers, please try again later.";

        final float x = sr.getScaledWidth() / 2f;
        final float y = sr.getScaledHeight() / 2f;

        final float width = 260;
        final float height = 20;

        RenderUtil.roundedRect(x - width / 2f, y - height / 2f + 3, width, height, 9, new Color(39, 42, 48, 255));
        this.comfortaa.drawCenteredString(text, x, y, Color.WHITE.hashCode());
    }
}
