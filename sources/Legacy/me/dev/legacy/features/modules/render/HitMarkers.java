package me.dev.legacy.features.modules.render;

import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import me.dev.legacy.util.*;

public final class HitMarkers extends Module
{

    public final ResourceLocation image;
    private int renderTicks;

    public HitMarkers() {
        super("HitMarkers", "hitmarker thingys", Module.Category.RENDER, false, false, false);
        this.image = new ResourceLocation("hitmarker.png");
        renderTicks = 100;
    }

    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Integer> thickness = this.register(new Setting<Integer>("Thickness", 2, 1, 6));
    public Setting<Double> time = this.register(new Setting<Double>("Time", 20D, 1D, 50D));

    @Override
    public void onRender2D(Render2DEvent event) {
        if (this.renderTicks < time.getValue()) {
            final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
            this.drawHitMarkers();
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (!event.getEntity().equals(HitMarkers.mc.player)) {
            return;
        }
        this.renderTicks = 0;
    }

    @SubscribeEvent
    public void onTickClientTick(final TickEvent event) {
        ++renderTicks;
    }

    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
    }
}