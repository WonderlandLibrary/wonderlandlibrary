package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChinaHat extends Feature {
    final ListSetting colorMode = new ListSetting("Mode", "Client", () -> true, "Client", "Astolfo");

    public ChinaHat() {
        super("China Hat", "Красивая шляпа на Entity", FeatureCategory.Render);
        addSettings(colorMode);
    }

    @EventTarget
    public void asf(EventRender3D event) {
    
    ItemStack stack = mc.player.getEquipmentInSlot(4);
    double height = stack.getItem() instanceof ItemArmor ? mc.player.isSneaking() ? -0.1 : 0.12 : mc.player.isSneaking() ? -0.22 : 0;
    if (Celestial.instance.featureManager.getFeature(CustomModel.class).isEnabled()) {
    	height += 0.3f;
    }
    if ((mc.gameSettings.thirdPersonView == 1 || mc.gameSettings.thirdPersonView == 2)) {
        GlStateManager.pushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        RenderUtils.enableSmoothLine(2.5f);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glTranslatef(0f, (float) (mc.player.height + height), 0f);
        GL11.glRotatef(-mc.player.rotationYaw, 0f, 1f, 0f);
        Color color2 = Color.WHITE;
        switch (colorMode.currentMode) {
            case "Client":
                color2 = ClientHelper.getClientColor(5, 1, 16);
                break;
            case "Astolfo":
                color2 = ColorUtils.astolfo(5, 5, 1, 16);
                break;
        }

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        RenderUtils.glColor(color2, 255);
        GL11.glVertex3d(0.0, 0.3, 0.0);

        for (float i = 0; i < 360.7; i += 1) {
            Color color = Color.WHITE;
            switch (colorMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor(i * 3.7f, i, 5);
                    break;
                case "Astolfo":
                    color = ColorUtils.astolfo(i - i + 1.0F, i, 0.7f,10);
                    break;
            }

            RenderUtils.glColor(color, 150);
            GL11.glVertex3d(Math.cos(i * Math.PI / 180.0) * 0.66, 0, Math.sin(i * Math.PI / 180.0) * 0.66);
        }
        GL11.glVertex3d(0.0, 0.3, 0.0);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (float i = 0; i < 360.7; i += 1) {
            Color color = Color.WHITE;
            switch (colorMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor(i * 3.7f, i, 5);
                    break;
                case "Astolfo":
                    color = ColorUtils.astolfo(i - i + 1.0F, i, 0.7f,10);
                    break;
            }
            RenderUtils.glColor(color, 255);
            GL11.glVertex3d(Math.cos(i * Math.PI / 180.0) * 0.66, 0, Math.sin(i * Math.PI / 180.0) * 0.66);
        }
        GL11.glEnd();
        GlStateManager.enableAlpha();
        RenderUtils.disableSmoothLine();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
}
}
