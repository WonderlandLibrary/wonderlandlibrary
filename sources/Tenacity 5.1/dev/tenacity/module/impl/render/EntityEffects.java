package dev.tenacity.module.impl.render;

import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.render.RenderModelEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.render.ESPUtil;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ShaderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityEffects extends Module {

    private final MultipleBoolSetting validEntities = new MultipleBoolSetting("Valid Entities",
            new BooleanSetting("Players", true),
            new BooleanSetting("Animals", true),
            new BooleanSetting("Mobs", true));

    private final BooleanSetting blur = new BooleanSetting("Blur", true);
    private final BooleanSetting bloom = new BooleanSetting("Bloom", true);
    private final BooleanSetting blackBloom = new BooleanSetting("Black Bloom", true);


    private Framebuffer entityFramebuffer = new Framebuffer(1, 1, false);

    public EntityEffects() {
        super("Entity Effects", Category.RENDER, "Very unnecessary blur of entities");
        blackBloom.addParent(bloom, ParentAttribute.BOOLEAN_CONDITION);
        addSettings(validEntities, blur, bloom, blackBloom);
    }

    @Override
    public void onEnable() {
        if (Tenacity.INSTANCE.isEnabled(PostProcessing.class)) {
            super.onEnable();
        } else {
            NotificationManager.post(NotificationType.WARNING, "Error", "Post Processing is not enabled");
            toggleSilent();
        }
    }

    private final List<Entity> entities = new ArrayList<>();

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        entities.clear();
        for (final Entity entity : mc.theWorld.loadedEntityList) {
            if (shouldRender(entity) && ESPUtil.isInView(entity)) {
                entities.add(entity);
            }
        }
    }


    @Override
    public void onRenderModelEvent(RenderModelEvent event) {
        if (event.isPost() && entities.contains(event.getEntity())) {
            entityFramebuffer.bindFramebuffer(false);
            RenderUtil.resetColor();
            GlStateManager.enableCull();
            GlowESP.renderGlint = false;
            event.drawModel();

            //Needed to add the other layers to the entity
            event.drawLayers();
            GlowESP.renderGlint = true;
            GlStateManager.disableCull();

            mc.getFramebuffer().bindFramebuffer(false);
        }
    }

    @Override
    public void onShaderEvent(ShaderEvent e) {
        if (e.isBloom() ? bloom.isEnabled() : blur.isEnabled()) {
            RenderUtil.setAlphaLimit(0);
            RenderUtil.resetColor();
            GLUtil.startBlend();

            if (e.isBloom() && blackBloom.isEnabled()) {
                RenderUtil.color(Color.BLACK.getRGB());
            }

            RenderUtil.bindTexture(entityFramebuffer.framebufferTexture);
            ShaderUtil.drawQuads();

        }
    }

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        entityFramebuffer = RenderUtil.createFrameBuffer(entityFramebuffer);
        entityFramebuffer.framebufferClear();
        mc.getFramebuffer().bindFramebuffer(true);
    }

    private boolean shouldRender(Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (validEntities.getSetting("Players").isEnabled() && entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer) {
                return mc.gameSettings.thirdPersonView != 0;
            }
            return !entity.getDisplayName().getUnformattedText().contains("[NPC");
        }
        if (validEntities.getSetting("Animals").isEnabled() && entity instanceof EntityAnimal) {
            return true;
        }

        return validEntities.getSetting("mobs").isEnabled() && entity instanceof EntityMob;
    }

}
