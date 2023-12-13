package today.flux.gui.clickgui.skeet.component.impl.sub.button;

import java.util.function.Consumer;

import net.minecraft.client.gui.Gui;
import today.flux.gui.clickgui.skeet.SkeetUtils;
import today.flux.gui.clickgui.skeet.LockedResolution;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.gui.clickgui.skeet.component.ButtonComponent;
import today.flux.gui.clickgui.skeet.component.Component;

public final class ButtonComponentImpl extends ButtonComponent {
    private final String text;
    private final Consumer<Integer> onPress;

    public ButtonComponentImpl(Component parent, String text, Consumer<Integer> onPress, float width, float height) {
        super(parent, 0.0f, 0.0f, width, height);
        this.text = text;
        this.onPress = onPress;
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        boolean hovered = this.isHovered(mouseX, mouseY);
        Gui.drawRect(x, y, x + width, y + height, SkeetClickGUI.getColor(0x111111));
        Gui.drawRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, SkeetClickGUI.getColor(0x262626));
        SkeetUtils.drawGradientRect(x + 1.0f, y + 1.0f, x + width - 1.0f, y + height - 1.0f, false, SkeetClickGUI.getColor(hovered ? SkeetUtils.darker(0x222222, 1.2f) : 0x222222), SkeetClickGUI.getColor(hovered ? SkeetUtils.darker(0x1E1E1E, 1.2f) : 0x1E1E1E));
        if (SkeetClickGUI.shouldRenderText()) {
            SkeetClickGUI.FONT_RENDERER.drawOutlinedString(this.text, x + width / 2.0f - SkeetClickGUI.FONT_RENDERER.getStringWidth(this.text) / 2.0f, y + height / 2.0f - 4f, SkeetClickGUI.getColor(0xFFFFFF), SkeetClickGUI.getColor(0));
        }
    }

    @Override
    public void onPress(int mouseButton) {
        this.onPress.accept(mouseButton);
    }
}

