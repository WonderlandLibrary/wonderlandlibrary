// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.render.ShaderUtil;
import java.awt.Color;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.event.impl.render.ShaderEvent;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.event.impl.render.RenderModelEvent;
import java.util.Iterator;
import dev.tenacity.utils.render.ESPUtil;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.Tenacity;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.Module;

public class EntityEffects extends Module
{
    private final MultipleBoolSetting validEntities;
    private final BooleanSetting blur;
    private final BooleanSetting bloom;
    private final BooleanSetting blackBloom;
    private Framebuffer entityFramebuffer;
    private final List<Entity> entities;
    
    public EntityEffects() {
        super("EntityEffects", "Entity Effects", Category.RENDER, "Very unnecessary blur of entities");
        this.validEntities = new MultipleBoolSetting("Valid Entities", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Animals", true), new BooleanSetting("Mobs", true) });
        this.blur = new BooleanSetting("Blur", true);
        this.bloom = new BooleanSetting("Bloom", true);
        this.blackBloom = new BooleanSetting("Black Bloom", true);
        this.entityFramebuffer = new Framebuffer(1, 1, false);
        this.entities = new ArrayList<Entity>();
        this.blackBloom.addParent(this.bloom, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.validEntities, this.blur, this.bloom, this.blackBloom);
    }
    
    @Override
    public void onEnable() {
        if (Tenacity.INSTANCE.isEnabled(PostProcessing.class)) {
            super.onEnable();
        }
        else {
            NotificationManager.post(NotificationType.WARNING, "Error", "Post Processing is not enabled");
            this.toggleSilent();
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        this.entities.clear();
        for (final Entity entity : EntityEffects.mc.theWorld.loadedEntityList) {
            if (this.shouldRender(entity) && ESPUtil.isInView(entity)) {
                this.entities.add(entity);
            }
        }
    }
    
    @Override
    public void onRenderModelEvent(final RenderModelEvent event) {
        if (event.isPost() && this.entities.contains(event.getEntity())) {
            this.entityFramebuffer.bindFramebuffer(false);
            RenderUtil.resetColor();
            GlStateManager.enableCull();
            GlowESP.renderGlint = false;
            event.drawModel();
            event.drawLayers();
            GlowESP.renderGlint = true;
            GlStateManager.disableCull();
            EntityEffects.mc.getFramebuffer().bindFramebuffer(false);
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        if (e.isBloom()) {
            if (!this.bloom.isEnabled()) {
                return;
            }
        }
        else if (!this.blur.isEnabled()) {
            return;
        }
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.startBlend();
        if (e.isBloom() && this.blackBloom.isEnabled()) {
            RenderUtil.color(Color.BLACK.getRGB());
        }
        RenderUtil.bindTexture(this.entityFramebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        (this.entityFramebuffer = RenderUtil.createFrameBuffer(this.entityFramebuffer)).framebufferClear();
        EntityEffects.mc.getFramebuffer().bindFramebuffer(true);
    }
    
    private boolean shouldRender(final Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (!this.validEntities.getSetting("Players").isEnabled() || !(entity instanceof EntityPlayer)) {
            return (this.validEntities.getSetting("Animals").isEnabled() && entity instanceof EntityAnimal) || (this.validEntities.getSetting("mobs").isEnabled() && entity instanceof EntityMob);
        }
        if (entity == EntityEffects.mc.thePlayer) {
            return EntityEffects.mc.gameSettings.thirdPersonView != 0;
        }
        return !entity.getDisplayName().getUnformattedText().contains("[NPC");
    }
}
