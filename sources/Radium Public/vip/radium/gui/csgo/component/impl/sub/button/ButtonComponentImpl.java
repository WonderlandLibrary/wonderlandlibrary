package vip.radium.gui.csgo.component.impl.sub.button;

import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

import java.util.function.Consumer;

public final class ButtonComponentImpl extends ButtonComponent {

    private final String text;
    private final Consumer<Integer> onPress;

    public ButtonComponentImpl(Component parent, String text, Consumer<Integer> onPress, float width, float height) {
        super(parent, 0, 0, width, height);
        this.text = text;
        this.onPress = onPress;
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();
        final boolean hovered = isHovered(mouseX, mouseY);
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(0x111111));
        Gui.drawRect(x + 0.5F, y + 0.5F, x + width - 0.5F, y + height - 0.5F, SkeetUI.getColor(0x262626));
        RenderingUtils.drawGradientRect(x + 1, y + 1, x + width - 1, y + height - 1, false,
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x222222, 1.2F) :
                        0x222222),
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x1E1E1E, 1.2F) :
                        0x1E1E1E));

        if (SkeetUI.shouldRenderText())
            RenderingUtils.drawOutlinedString(SkeetUI.FONT_RENDERER, text,
                    x + width / 2 - SkeetUI.FONT_RENDERER.getWidth(text) / 2,
                    y + height / 2 - 1,
                    SkeetUI.getColor(0xFFFFFF), SkeetUI.getColor(0));
    }

    @Override
    public void onPress(int mouseButton) {
        onPress.accept(mouseButton);
    }
}
