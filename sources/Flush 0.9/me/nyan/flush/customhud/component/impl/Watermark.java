package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Watermark extends Component {
    @Override
    public void onAdded() {
    }

    @Override
    public void draw(float x, float y) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 1);
        RenderUtils.glColor(-1);
        RenderUtils.drawImage(new ResourceLocation("flush/Flush.png"), x / 0.5, y / 0.5, 100, 100);
        GlStateManager.popMatrix();
    }


    @Override
    public int width() {
        return 50;
    }

    @Override
    public int height() {
        return 50;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.NORMAL;
    }
}
