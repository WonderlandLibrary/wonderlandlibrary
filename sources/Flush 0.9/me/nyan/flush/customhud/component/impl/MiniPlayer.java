package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.utils.render.RenderUtils;

public class MiniPlayer extends Component {
    @Override
    public void onAdded() {

    }

    @Override
    public void draw(float x, float y) {
        RenderUtils.drawEntityOnScreen(mc.thePlayer, (int) x + 25, (int) y + 100, 50, 0, 0);
    }

    @Override
    public int width() {
        return 50;
    }

    @Override
    public int height() {
        return 100;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
