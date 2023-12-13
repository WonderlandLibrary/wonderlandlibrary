package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.FontSetting;
import me.nyan.flush.customhud.setting.impl.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

public class Graph extends Component {
    private final me.nyan.flush.ui.elements.Graph speed = new me.nyan.flush.ui.elements.Graph("Speed");
    private final me.nyan.flush.ui.elements.Graph frameRate = new me.nyan.flush.ui.elements.Graph("FPS");
    private final me.nyan.flush.ui.elements.Graph frameTime = new me.nyan.flush.ui.elements.Graph("Frame Time");

    private ModeSetting mode;
    private FontSetting font;

    @Override
    public void onAdded() {
        settings.add(mode = new ModeSetting("Mode", "Speed", "Speed", "FPS", "Frame Time"));
        settings.add(font = new FontSetting("Font", "GoogleSansDisplay", 20));
    }

    @Override
    public void draw(float x, float y) {
        me.nyan.flush.ui.elements.Graph graph = null;
        switch (mode.getValue().toUpperCase()) {
            case "SPEED":
                graph = speed;
                graph.update(MovementUtils.getBPS());
                break;
            case "FPS":
                graph = frameRate;
                graph.update(RenderUtils.getFPS());
                break;
            case "FRAME TIME":
                graph = frameTime;
                graph.update(Flush.getFrameTime());
                break;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(1 / scaleX, 1 / scaleY, 0);
        graph.setFont(Flush.getFont(font.getFont(), font.getSize()));
        graph.setMinecraftFont(font.isMinecraftFont());
        graph.drawGraph(x * scaleX, y * scaleY, width() * scaleX, height() * scaleY);
        GlStateManager.popMatrix();
    }

    @Override
    public int width() {
        return 200;
    }

    @Override
    public int height() {
        return 60;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
