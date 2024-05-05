// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.key;

import vip.Resolute.util.font.FontUtil;
import org.lwjgl.input.Keyboard;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public final class KeyBindComponent extends ButtonComponent
{
    private static final MinecraftFontRenderer FONT_RENDERER;
    private final Supplier<Integer> getBind;
    private final Consumer<Integer> onSetBind;
    private boolean binding;
    
    public KeyBindComponent(final Component parent, final Supplier<Integer> getBind, final Consumer<Integer> onSetBind, final float x, final float y) {
        super(parent, x, y, (float)(KeyBindComponent.FONT_RENDERER.getStringWidth("[") * 2.0), (float)KeyBindComponent.FONT_RENDERER.getHeight());
        this.getBind = getBind;
        this.onSetBind = onSetBind;
    }
    
    @Override
    public float getWidth() {
        return (float)(super.getWidth() + KeyBindComponent.FONT_RENDERER.getStringWidth(this.getBind()));
    }
    
    @Override
    public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        KeyBindComponent.FONT_RENDERER.drawStringWithShadow("[" + this.getBind() + "]", x + 65.16667f - width, y - 10.0f, SkeetUI.getColor(7895160));
    }
    
    @Override
    public boolean isHovered(final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        return mouseX >= x + 50.166668f - this.getWidth() && mouseY >= y - 10.0f && mouseX <= x + 150.0f && mouseY <= y + this.getHeight() - 10.0f;
    }
    
    @Override
    public void onKeyPress(int keyCode) {
        if (this.binding) {
            if (keyCode == 211 || keyCode == 14) {
                keyCode = 0;
            }
            this.onChangeBind(keyCode);
            this.binding = false;
        }
    }
    
    private String getBind() {
        final int bind = this.getBind.get();
        return this.binding ? "..." : ((bind == 0) ? "-" : Keyboard.getKeyName(bind));
    }
    
    private void onChangeBind(final int bind) {
        this.onSetBind.accept(bind);
    }
    
    @Override
    public void onPress(final int mouseButton) {
        this.binding = !this.binding;
    }
    
    static {
        FONT_RENDERER = FontUtil.tahomaSmall;
    }
}
