package vip.radium.gui.csgo.component.impl.sub.key;

import org.lwjgl.input.Keyboard;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class KeyBindComponent extends ButtonComponent {

    private static final FontRenderer FONT_RENDERER = SkeetUI.KEYBIND_FONT_RENDERER;

    private final Supplier<Integer> getBind;
    private final Consumer<Integer> onSetBind;

    private boolean binding;

    public KeyBindComponent(Component parent, Supplier<Integer> getBind, Consumer<Integer> onSetBind, float x, float y) {
        super(parent, x, y, FONT_RENDERER.getWidth("[") * 2, FONT_RENDERER.getHeight("[]"));

        this.getBind = getBind;
        this.onSetBind = onSetBind;
    }

    @Override
    public float getWidth() {
        return super.getWidth() + FONT_RENDERER.getWidth(getBind());
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();

        RenderingUtils.drawOutlinedString(FONT_RENDERER,
                "[" + getBind() + "]",
                x + SkeetUI.HALF_GROUP_BOX - width,
                y,
                SkeetUI.getColor(0x787878),
                SkeetUI.getColor(0));
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        return mouseX >= x + SkeetUI.HALF_GROUP_BOX - getWidth() &&
                mouseY >= y &&
                mouseX <= x + SkeetUI.HALF_GROUP_BOX &&
                mouseY <= y + getHeight();
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            if (keyCode == Keyboard.KEY_DELETE)
                keyCode = 0;
            onChangeBind(keyCode);
            binding = false;
        }
    }

    private String getBind() {
        final int bind = getBind.get();
        return binding ? "..." : bind == 0 ? "-" : Keyboard.getKeyName(bind);
    }

    private void onChangeBind(int bind) {
        onSetBind.accept(bind);
    }

    @Override
    public void onPress(int mouseButton) {
        binding = !binding;
    }
}
