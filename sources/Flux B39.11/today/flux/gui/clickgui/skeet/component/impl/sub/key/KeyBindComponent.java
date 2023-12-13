package today.flux.gui.clickgui.skeet.component.impl.sub.key;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lwjgl.input.Keyboard;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.gui.clickgui.skeet.LockedResolution;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.gui.clickgui.skeet.component.ButtonComponent;
import today.flux.gui.clickgui.skeet.component.Component;

public final class KeyBindComponent extends ButtonComponent {
    private static final FontUtils FONT_RENDERER = SkeetClickGUI.KEYBIND_FONT_RENDERER;
    private final Supplier<Integer> getBind;
    private final Consumer<Integer> onSetBind;
    private boolean binding;

    public KeyBindComponent(Component parent, Supplier<Integer> getBind, Consumer<Integer> onSetBind, float x, float y) {
        super(parent, x, y, FONT_RENDERER.getStringWidth("[") * 2.0f, FONT_RENDERER.getHeight("[]"));
        this.getBind = getBind;
        this.onSetBind = onSetBind;
    }

    @Override
    public float getWidth() {
        return super.getWidth() + FONT_RENDERER.getStringWidth(this.getBind());
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        float x = this.getX();
        float y = this.getY();
        float width = this.getWidth();
        FONT_RENDERER.drawOutlinedString("[" + this.getBind() + "]", x + 40.166668f - width, y, SkeetClickGUI.getColor(0x787878), SkeetClickGUI.getColor(0));
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        float x = this.getX();
        float y = this.getY();
        return (float)mouseX >= x + 40.166668f - this.getWidth() && (float)mouseY >= y && (float)mouseX <= x + 40.166668f && (float)mouseY <= y + this.getHeight();
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (this.binding) {
            if (keyCode == 211) {
                keyCode = 0;
            }
            this.onChangeBind(keyCode);
            this.binding = false;
        }
    }

    private String getBind() {
        int bind = this.getBind.get();
        return this.binding ? "..." : (bind == 0 ? "-" : Keyboard.getKeyName((int)bind));
    }

    private void onChangeBind(int bind) {
        this.onSetBind.accept(bind);
    }

    @Override
    public void onPress(int mouseButton) {
        this.binding = !this.binding;
    }
}

