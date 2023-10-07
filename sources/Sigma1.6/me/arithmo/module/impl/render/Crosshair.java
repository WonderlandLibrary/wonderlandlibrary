/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import java.awt.Color;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovementInput;

public class Crosshair
extends Module {
    private String RED = "RED";
    private String GREEN = "GREEN";
    private String BLUE = "BLUE";
    private String OPACITY = "OPACITY";
    private String GAP = "GAP";
    private String WIDTH = "WIDTH";
    private String SIZE = "SIZE";
    private String DYNAMIC = "DYNAMIC";

    public Crosshair(ModuleData data) {
        super(data);
        this.settings.put(this.DYNAMIC, new Setting<Boolean>(this.DYNAMIC, true, "Expands when moving."));
        this.settings.put(this.RED, new Setting<Integer>(this.RED, 255, "Crosshair Red", 5.0, 0.0, 255.0));
        this.settings.put(this.GREEN, new Setting<Integer>(this.GREEN, 0, "Crosshair Green", 5.0, 0.0, 255.0));
        this.settings.put(this.BLUE, new Setting<Integer>(this.BLUE, 0, "Crosshair Blue", 5.0, 0.0, 255.0));
        this.settings.put(this.OPACITY, new Setting<Integer>(this.OPACITY, 255, "Crosshair Opacity", 5.0, 0.0, 255.0));
        this.settings.put(this.GAP, new Setting<Integer>(this.GAP, 5, "Crosshair Gap", 0.25, 0.25, 15.0));
        this.settings.put(this.WIDTH, new Setting<Integer>(this.WIDTH, 2, "Crosshair Width", 0.25, 0.25, 10.0));
        this.settings.put(this.SIZE, new Setting<Integer>(this.SIZE, 7, "Crosshair Size/Length", 0.25, 0.25, 15.0));
    }

    @RegisterEvent(events={EventRenderGui.class})
    public void onEvent(Event event) {
        int red = ((Number)((Setting)this.settings.get(this.RED)).getValue()).intValue();
        int green = ((Number)((Setting)this.settings.get(this.GREEN)).getValue()).intValue();
        int blue = ((Number)((Setting)this.settings.get(this.BLUE)).getValue()).intValue();
        int alph = ((Number)((Setting)this.settings.get(this.OPACITY)).getValue()).intValue();
        double gap = ((Number)((Setting)this.settings.get(this.GAP)).getValue()).doubleValue();
        double width = ((Number)((Setting)this.settings.get(this.WIDTH)).getValue()).doubleValue();
        double size = ((Number)((Setting)this.settings.get(this.SIZE)).getValue()).doubleValue();
        ScaledResolution scaledRes = new ScaledResolution(mc, Crosshair.mc.displayWidth, Crosshair.mc.displayHeight);
        RenderingUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - width, (double)(scaledRes.getScaledHeight() / 2) - gap - size - (double)(this.isMoving() ? 2 : 0), (double)((float)(scaledRes.getScaledWidth() / 2) + 1.0f) + width, (double)(scaledRes.getScaledHeight() / 2) - gap - (double)(this.isMoving() ? 2 : 0), 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderingUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - width, (double)(scaledRes.getScaledHeight() / 2) + gap + 1.0 + (double)(this.isMoving() ? 2 : 0) - 0.15, (double)((float)(scaledRes.getScaledWidth() / 2) + 1.0f) + width, (double)(scaledRes.getScaledHeight() / 2 + 1) + gap + size + (double)(this.isMoving() ? 2 : 0) - 0.15, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderingUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - gap - size - (double)(this.isMoving() ? 2 : 0) + 0.15, (double)(scaledRes.getScaledHeight() / 2) - width, (double)(scaledRes.getScaledWidth() / 2) - gap - (double)(this.isMoving() ? 2 : 0) + 0.15, (double)((float)(scaledRes.getScaledHeight() / 2) + 1.0f) + width, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderingUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2 + 1) + gap + (double)(this.isMoving() ? 2 : 0), (double)(scaledRes.getScaledHeight() / 2) - width, (double)(scaledRes.getScaledWidth() / 2) + size + gap + 1.0 + (double)(this.isMoving() ? 2 : 0), (double)((float)(scaledRes.getScaledHeight() / 2) + 1.0f) + width, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
    }

    public boolean isMoving() {
        return (Boolean)((Setting)this.settings.get(this.DYNAMIC)).getValue() != false && !Crosshair.mc.thePlayer.isCollidedHorizontally && !Crosshair.mc.thePlayer.isSneaking() && (Crosshair.mc.thePlayer.movementInput.moveForward != 0.0f || Crosshair.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
}

