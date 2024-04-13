package dev.tenacity.module.impl.render;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ColorSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RoundedUtil;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;

import java.awt.*;

import static dev.tenacity.module.impl.render.HUDModule.blurtint;
import static dev.tenacity.module.impl.render.HUDModule.opacity;

public class PlayerInfoModule extends Module {
    private NumberSetting xvalue = new NumberSetting("X Value", 6, 0, 1100, 1);
    private NumberSetting yvalue = new NumberSetting("Y Value", 63, 0, 500, 1);
    private NumberSetting width = new NumberSetting("Width", 47, 10, 200, 1);
    private NumberSetting height = new NumberSetting("Height", 22, 10, 100, 1);
    private NumberSetting radius = new NumberSetting("Radius", 4, 0, 15, 1);
    private NumberSetting opacitys = new NumberSetting("Opacity", 0.1, 0, 1, 0.1);
    private ColorSetting pimcolors = new ColorSetting("Background Tint", Color.black);
    private ModeSetting mode = new ModeSetting("Mode", "Sync", "Separate");
    public PlayerInfoModule() {
        super("Player Info", "Player Info (FPS, BPS)", ModuleCategory.RENDER);
        opacity.addParent(mode, modeSetting -> mode.isMode("Sync"));
        blurtint.addParent(mode, modeSetting -> mode.isMode("Sync"));
        pimcolors.addParent(mode, modeSetting -> mode.isMode("Separate"));
        opacitys.addParent(mode, modeSetting -> mode.isMode("Separate"));
        initializeSettings(mode, xvalue, yvalue, width, height, radius, opacitys, pimcolors);
    }
    private final IEventListener<Render2DEvent> onRender2DEvent = event -> {
        switch (mode.getCurrentMode()) {
            case "Sync": {
                RoundedUtil.drawRound((float) xvalue.getCurrentValue(), (float) yvalue.getCurrentValue(), (float) width.getCurrentValue(), (float) height.getCurrentValue(), (float) radius.getCurrentValue(), ColorUtil.applyOpacity(new Color(blurtint.getColor().getRGB()), (float) opacity.getCurrentValue()));
                CustomFont small = FontUtil.getFont("OpenSans-Medium", (int) (32 / 1.4));
                small.drawString("BPS: " + calculateBPS(), (float) xvalue.getCurrentValue() + 1, (float) yvalue.getCurrentValue() + 1, -1);
                small.drawString("FPS: " + mc.getDebugFPS(), (float) xvalue.getCurrentValue() +1, (float) yvalue.getCurrentValue() + 10, -1);
                break;
            }
            case "Separate": {
                RoundedUtil.drawRound((float) xvalue.getCurrentValue(), (float) yvalue.getCurrentValue(), (float) width.getCurrentValue(), (float) height.getCurrentValue(), (float) radius.getCurrentValue(), ColorUtil.applyOpacity(new Color(pimcolors.getColor().getRGB()), (float) opacitys.getCurrentValue()));
                CustomFont small = FontUtil.getFont("OpenSans-Medium", (int) (32 / 1.4));
                small.drawString("BPS: " + calculateBPS(), (float) xvalue.getCurrentValue() + 1, (float) yvalue.getCurrentValue() + 1, -1);
                small.drawString("FPS: " + mc.getDebugFPS(), (float) xvalue.getCurrentValue() +1, (float) yvalue.getCurrentValue() + 10, -1);
                break;
            }
        }
    };
    private final IEventListener<ShaderEvent> onShaderEvent = event -> {
        switch (mode.getCurrentMode()) {
            case "Sync": {
                RoundedUtil.drawRound((float) xvalue.getCurrentValue(), (float) yvalue.getCurrentValue(), (float) width.getCurrentValue(), (float) height.getCurrentValue(), (float) radius.getCurrentValue(), new Color(blurtint.getColor().getRGB()));
                break;
            }
            case "Separate": {
                RoundedUtil.drawRound((float) xvalue.getCurrentValue(), (float) yvalue.getCurrentValue(), (float) width.getCurrentValue(), (float) height.getCurrentValue(), (float) radius.getCurrentValue(), new Color(pimcolors.getColor().getRGB()));
                break;
            }
        }
    };
    public double calculateBPS() {
        double bps = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
        return Math.round(bps * 100.0) / 100.0;
    }
}