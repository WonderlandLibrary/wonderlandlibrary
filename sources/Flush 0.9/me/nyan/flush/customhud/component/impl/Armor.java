package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.ModeSetting;
import net.minecraft.client.renderer.RenderHelper;

public class Armor extends Component {
    private ModeSetting mode;

    @Override
    public void onAdded() {
        settings.add(mode = new ModeSetting("Mode", "Vertical", "Vertical", "Horizontal"));
    }

    @Override
    public void draw(float x, float y) {
        boolean horizontal = mode.is("horizontal");

        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 3; i >= 0; i--) {
            if (mc.thePlayer.getCurrentArmor(i) == null) {
                continue;
            }

            int offset = (3 - i) * 16;
            mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getCurrentArmor(i),
                    (int) x + (horizontal ? offset : 0), (int) y + (!horizontal ? offset : 0));
        }
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int width() {
        boolean horizontal = mode.is("horizontal");
        if (!horizontal) {
            return 16;
        }

        int width = 0;
        for (int i = 3; i >= 0; i--) {
            width += 16;
        }
        return width;
    }

    @Override
    public int height() {
        boolean horizontal = mode.is("horizontal");
        if (horizontal) {
            return 16;
        }

        int height = 0;
        for (int i = 3; i >= 0; i--) {
            height += 16;
        }
        return height;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
