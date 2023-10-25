package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.awt.*;

public class ChinaHat extends Feature {

    public ListSetting colorMode = new ListSetting("Color", "Rainbow", () -> true, "Rainbow", "Astolfo", "Client", "Custom");
    public ListSetting colorModeTop = new ListSetting("Color Top", "Rainbow", () -> true, "Rainbow", "Astolfo", "Client", "Custom");
    public ColorSetting customColor = new ColorSetting("Custom Color", new Color(0x0033FF).getRGB(), () -> colorModeTop.currentMode.equals("Custom"));
    public ColorSetting customColorTwo = new ColorSetting("Custom Color Two", new Color(0x0033FF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
    public NumberSetting heightValue = new NumberSetting("Height", 0.4f, 0.1f, 20, 0.1F, () -> true);
    public NumberSetting widthValue = new NumberSetting("Width", 1.2F, 0.1f, 20, 0.1F, () -> true);
    public NumberSetting point = new NumberSetting("Points", 32, 1, 60, 1, () -> true);
    public BooleanSetting hide = new BooleanSetting("Hide In First Person", true, () -> true);

    public ChinaHat() {
        super("ChinaHat", "Показывает китайскую шляпу", Type.Visuals);
        addSettings(colorMode, colorModeTop, hide, widthValue, heightValue, point, customColor, customColorTwo);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
        double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
        double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
        y += mc.player.getEyeHeight() + 0.20 - (mc.player.isSneaking() ? 0.25 : 0);

        if (mc.gameSettings.thirdPersonView == 0 && hide.getBoolValue())
            return;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glBegin(GL11.GL_QUAD_STRIP);

        double size = mc.player.width * widthValue.getNumberValue();

        for (int i = 0; i <= point.getNumberValue(); i++) {
            int customColorValue = -1;
            int customColorValueTop = -1;
            double height = heightValue.getNumberValue();
            String colorModeValue = colorMode.getOptions();
            if (colorModeValue.equalsIgnoreCase("Rainbow")) {
                customColorValue = PaletteHelper.rainbow(i * 10, 0.4F, 1).getRGB();
            } else if (colorModeValue.equalsIgnoreCase("Client")) {
                customColorValue = ClientHelper.getClientColor().getRGB();
            } else if (colorModeValue.equalsIgnoreCase("Custom")) {
                customColorValue = customColorTwo.getColorValue();
            } else if (colorModeValue.equalsIgnoreCase("Astolfo")) {
                Color astolfo = PaletteHelper.astolfo(false, i);
                customColorValue = new Color(astolfo.getRed(), astolfo.getGreen(), astolfo.getBlue()).getRGB();
            }

            String colorModeValueTop = colorModeTop.getOptions();
            if (colorModeValueTop.equalsIgnoreCase("Rainbow")) {
                customColorValueTop = PaletteHelper.rainbow(i * 10, 0.4F, 1).getRGB();
            } else if (colorModeValueTop.equalsIgnoreCase("Client")) {
                customColorValueTop = ClientHelper.getClientColor().getRGB();
            } else if (colorModeValueTop.equalsIgnoreCase("Custom")) {
                customColorValueTop = customColorTwo.getColorValue();
            } else if (colorModeValueTop.equalsIgnoreCase("Astolfo")) {
                Color astolfo = PaletteHelper.astolfo(false, i);
                customColorValueTop = new Color(astolfo.getRed(), astolfo.getGreen(), astolfo.getBlue()).getRGB();
            }

            Color top = new Color(customColorValueTop);
            RenderHelper.setColor(new Color(top.getRed(), top.getGreen(), top.getBlue(), 120).getRGB());
            GL11.glVertex3d(x, y + height, z);
            RenderHelper.setColor(customColorValue);
            double posX = x - Math.sin(i * Math.PI * 2 / point.getNumberValue()) * size;
            double posZ = z + Math.cos(i * Math.PI * 2 / point.getNumberValue()) * size;
            GL11.glVertex3d(posX, y, posZ);
        }

        GL11.glEnd();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
}
