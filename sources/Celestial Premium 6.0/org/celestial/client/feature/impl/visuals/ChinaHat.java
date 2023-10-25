/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.visuals.Chams;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

public class ChinaHat
extends Feature {
    private final NumberSetting radius;
    private final NumberSetting height;
    private final NumberSetting segments;
    public ListSetting chinaHatMode = new ListSetting("ChinaHat Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom", "Cosmo");
    private final ColorSetting chinaHatColor;
    private final NumberSetting hatAlpha;
    private final BooleanSetting hideInFirstPerson;

    public ChinaHat() {
        super("ChinaHat", "\u0420\u0438\u0441\u0443\u0435\u0442 \u043a\u0438\u0442\u0430\u0439\u0441\u043a\u0443\u044e \u0448\u043b\u044f\u043f\u043a\u0443 \u043d\u0430\u0434 \u0432\u0430\u0448\u0435\u0439 \u0433\u043e\u043b\u043e\u0432\u043e\u0439", Type.Visuals);
        this.segments = new NumberSetting("Segments", 50.0f, 6.0f, 50.0f, 1.0f, () -> true);
        this.radius = new NumberSetting("Radius", 1.0f, 0.1f, 2.0f, 0.1f, () -> true);
        this.height = new NumberSetting("Height", 0.5f, 0.0f, 5.0f, 0.1f, () -> true);
        this.chinaHatColor = new ColorSetting("ChinaHat Color", Color.WHITE.getRGB(), () -> this.chinaHatMode.currentMode.equals("Custom"));
        this.hatAlpha = new NumberSetting("Hat Alpha", 0.5f, 0.2f, 1.0f, 0.1f, () -> !this.chinaHatMode.currentMode.equals("Custom") && !this.chinaHatMode.currentMode.equals("Cosmo"));
        this.hideInFirstPerson = new BooleanSetting("Hide In First Person", true, () -> true);
        this.addSettings(this.segments, this.radius, this.height, this.chinaHatMode, this.chinaHatColor, this.hatAlpha, this.hideInFirstPerson);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (ChinaHat.mc.gameSettings.thirdPersonView == 0 && this.hideInFirstPerson.getCurrentValue()) {
            return;
        }
        if (ChinaHat.mc.player.getHealth() <= 0.0f) {
            return;
        }
        GlStateManager.pushMatrix();
        int oneColor = this.chinaHatColor.getColor();
        int color = 0;
        switch (this.chinaHatMode.currentMode) {
            case "Client": {
                color = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color = oneColor;
                break;
            }
            case "Astolfo": {
                color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        RenderManager renderManager = mc.getRenderManager();
        double x = ChinaHat.mc.player.lastTickPosX + (ChinaHat.mc.player.posX - ChinaHat.mc.player.lastTickPosX) * (double)ChinaHat.mc.timer.renderPartialTicks - renderManager.renderPosX;
        double y = ChinaHat.mc.player.lastTickPosY + (ChinaHat.mc.player.posY - ChinaHat.mc.player.lastTickPosY) * (double)ChinaHat.mc.timer.renderPartialTicks - renderManager.renderPosY;
        double z = ChinaHat.mc.player.lastTickPosZ + (ChinaHat.mc.player.posZ - ChinaHat.mc.player.lastTickPosZ) * (double)ChinaHat.mc.timer.renderPartialTicks - renderManager.renderPosZ;
        float f = ChinaHat.mc.player.getEyeHeight() + 0.35f;
        float f2 = ChinaHat.mc.player.isSneaking() ? 0.25f : 0.0f;
        float center = 0.5f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated((x -= 0.5) + (double)center, (y += (double)(f - f2)) + (double)ChinaHat.mc.player.height + (double)this.height.getCurrentValue() + (double)center - (double)(ChinaHat.mc.player.isSneaking() ? (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus") ? 0.05f : 0.25f) : 0.0f) - (double)(Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus") ? 0.35f : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Red Panda")) ? 0.7f : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Freddy Bear") ? -0.3f : 0.0f))), (z -= 0.5) + (double)center);
        GL11.glRotated(-ChinaHat.mc.player.rotationYaw % 360.0f, 0.0, 1.0, 0.0);
        GL11.glTranslated(-(x + (double)center), -(y + (double)center), -(z + (double)center));
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (!this.chinaHatMode.currentMode.equals("Cosmo")) {
            if (this.chinaHatMode.currentMode.equals("Custom")) {
                RenderHelper.color(oneColor);
            } else {
                GlStateManager.color((float)new Color(color).getRed() / 255.0f, (float)new Color(color).getGreen() / 255.0f, (float)new Color(color).getBlue() / 255.0f, this.hatAlpha.getCurrentValue());
            }
        }
        if (this.chinaHatMode.currentMode.equals("Cosmo")) {
            Chams.shaderAttach(ChinaHat.mc.player);
        }
        RenderHelper.drawCone(this.radius.getCurrentValue(), this.height.getCurrentValue(), (int)this.segments.getCurrentValue(), true);
        if (this.chinaHatMode.currentMode.equals("Cosmo")) {
            Chams.shaderDetach();
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }
}

