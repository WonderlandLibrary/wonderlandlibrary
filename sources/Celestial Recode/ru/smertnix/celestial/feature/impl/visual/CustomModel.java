package ru.smertnix.celestial.feature.impl.visual;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventCustomModel;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.MathUtils;
import ru.smertnix.celestial.utils.math.MathematicHelper;
import ru.smertnix.celestial.utils.render.models.TessellatorModel;

public class CustomModel extends Feature {

    public CustomModel() {
        super("Custom Model", "Моделька гитлера онлайн", FeatureCategory.Render);
    }

    private TessellatorModel hitlerHead;
    private TessellatorModel hitlerBody;


    @Override
    public void onEnable() {
        super.onEnable();
        this.hitlerHead = new TessellatorModel("/assets/minecraft/celestial/head.obj");
        this.hitlerBody = new TessellatorModel("/assets/minecraft/celestial/body.obj");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.hitlerHead = null;
        this.hitlerBody = null;
    }

    @EventTarget
    private void on(final EventCustomModel event) {
    	GlStateManager.pushMatrix();

        AbstractClientPlayer entity = mc.player;
        RenderManager manager = mc.getRenderManager();
        double x = MathematicHelper.interpolate(entity.posX, entity.lastTickPosX, 0) - manager.renderPosX;
        double y = MathematicHelper.interpolate(entity.posY, entity.lastTickPosY, 0) - manager.renderPosY;
        double z =  MathematicHelper.interpolate(entity.posZ, entity.lastTickPosZ, 0) - manager.renderPosZ;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        boolean sneak = mc.player.isSneaking();
        GL11.glTranslated(x, y, z);
        if (!(mc.currentScreen instanceof GuiContainer))
            GL11.glRotatef(-yaw, 0.0F, mc.player.height, 0.0F);
        GlStateManager.scale(0.03, sneak ? 0.027 : 0.029, 0.03);

        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1.0F);
        this.hitlerHead.render();
        this.hitlerBody.render();
        GlStateManager.enableLighting();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();

    }
}
