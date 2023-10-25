package ru.smertnix.celestial.feature.impl.visual;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityPotion;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.MathUtils;
import ru.smertnix.celestial.utils.math.animations.impl.DecelerateAnimation;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.ESPUtil;
import ru.smertnix.celestial.utils.render.RenderUtil;
import ru.smertnix.celestial.utils.render.ShaderUtil;
import ru.smertnix.celestial.utils.render.shader.shaders.EntityGlowShader;

public class ShaderESP extends Feature {
    private static ColorSetting glowColor = new ColorSetting("Glow Color", ClientHelper.getClientColor().getRGB(), () -> true);
    private final ShaderUtil outlineShader = new ShaderUtil("celestial/shaders/outline.frag");
    private final ShaderUtil glowShader = new ShaderUtil("celestial/shaders/glow.frag");
    public Framebuffer glowFrameBuffer;
    private final Frustum frustum = new Frustum();

    public ShaderESP() {
        super("Shader ESP", "Делает обводку на Entity", FeatureCategory.Render);
        this.addSettings(glowColor);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
    EntityGlowShader framebufferShader = EntityGlowShader.GLOW_SHADER;
    @EventTarget
    private void onRender3D(EventRender3D e) {
    	mc.gameSettings.entityShadows = false;
        framebufferShader.renderShader(e.getPartialTicks());
        for (Entity entity : mc.world.loadedEntityList) {
            if (!isValid(entity) || entity instanceof EntityItem)
                continue;

            mc.getRenderManager().renderEntityStatic(entity, e.getPartialTicks(), true);
        }

        framebufferShader.stopRenderShader(new Color(glowColor.getColorValue()), 3, 1);
    }
    
    private boolean isValid(Entity entity) {
        if (mc.gameSettings.thirdPersonView == 0 && entity == mc.player)
            return false;
        if (entity.isDead)
            return false;
        if ((entity instanceof net.minecraft.entity.passive.EntityAnimal))
            return false;
        if ((entity instanceof EntityPlayer))
            return true;
        if ((entity instanceof EntityArmorStand))
            return false;
        if ((entity instanceof IAnimals))
            return false;
        if ((entity instanceof EntityItemFrame))
            return false;
        if (entity instanceof EntityArrow)
            return false;
        if ((entity instanceof EntityMinecart))
            return false;
        if ((entity instanceof EntityBoat))
            return false;
        if ((entity instanceof EntityDragonFireball))
            return false;
        if ((entity instanceof EntityXPOrb))
            return false;
        if ((entity instanceof EntityTNTPrimed))
            return false;
        if ((entity instanceof EntityExpBottle))
            return false;
        if ((entity instanceof EntityLightningBolt))
            return false;
        if ((entity instanceof EntityPotion))
            return false;
        if ((entity instanceof Entity))
            return false;
        if (((entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.boss.EntityDragon
                || entity instanceof net.minecraft.entity.monster.EntityGolem)))
            return false;
        return entity != mc.player;
    }
}

