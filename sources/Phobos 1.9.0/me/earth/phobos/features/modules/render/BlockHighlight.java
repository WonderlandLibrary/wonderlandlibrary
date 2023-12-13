package me.earth.phobos.features.modules.render;

import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.client.HUD;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight
        extends Module {
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public Setting<Boolean> rolling = this.register(new Setting<Object>("Rolling", Boolean.valueOf(false), v -> this.colorSync.getValue()));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.outline.getValue()));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.customOutline.getValue() != false && this.outline.getValue() != false));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            if (this.rolling.getValue().booleanValue()) {
                RenderUtil.drawProperGradientBlockOutline(blockpos, new Color(HUD.getInstance().colorMap.get(0)), new Color(HUD.getInstance().colorMap.get(this.renderer.scaledHeight / 4)), new Color(HUD.getInstance().colorMap.get(this.renderer.scaledHeight / 2)), 1.0f);
            } else {
                RenderUtil.drawBoxESP(blockpos, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            }
        }
    }
}

