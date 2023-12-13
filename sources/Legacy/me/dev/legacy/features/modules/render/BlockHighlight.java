package me.dev.legacy.features.modules.render;

import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.ColorUtil;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight extends Module {
    private final Setting<Float> lineWidth = register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> alpha = register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Integer> red = register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Boolean> rainbow = register(new Setting<Boolean>("Rainbow", false));
    private final Setting<Integer> rainbowhue = register(new Setting<Integer>("RainbowHue", 255, 0, 255, v -> rainbow.getValue()));


    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, rainbow.getValue() != false ? ColorUtil.rainbow(rainbowhue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), lineWidth.getValue().floatValue(), false);
        }
    }
}

