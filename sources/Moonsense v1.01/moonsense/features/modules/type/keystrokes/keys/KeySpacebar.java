// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.keystrokes.keys;

import net.minecraft.client.Minecraft;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.modules.type.keystrokes.KeystrokesModule;
import net.minecraft.client.settings.KeyBinding;

public class KeySpacebar extends Key
{
    public KeySpacebar(final int gapSize, final KeyBinding keyBinding, final KeystrokesModule keystrokesModule) {
        super(gapSize, keyBinding, keystrokesModule);
    }
    
    @Override
    public void render(final boolean dummy) {
        final boolean pressed = !dummy && this.pressed();
        final float pressModifier = Math.min(1.0f, (float)((System.currentTimeMillis() - this.pressTime) / this.keystrokesModule.boxFade.getInt()));
        final float brightness = (pressed ? pressModifier : (1.0f - pressModifier)) * 0.8f;
        this.renderBackground(pressed, brightness);
        final float x = this.getWidth() / 2.0f - 10.0f;
        final float x2 = this.getWidth() / 2.0f + 10.0f;
        GuiUtils.drawGradientRect((int)x, 3, (int)x2, 4, this.getColor(this.keystrokesModule.textColor.getColorObject(), x, pressed), this.getColor(this.keystrokesModule.textColor.getColorObject(), x2, pressed), this.getColor(this.keystrokesModule.textColor.getColorObject(), x, pressed), this.getColor(this.keystrokesModule.textColor.getColorObject(), x2, pressed), 0);
        Minecraft.getMinecraft().ingameGUI.drawGradientRect((int)x, 3, (int)x2, 4, this.getColor(this.keystrokesModule.textColor.getColorObject(), 0.0f, pressed), this.getColor(this.keystrokesModule.textColor.getColorObject(), 0.0f, pressed));
    }
    
    @Override
    public float getWidth() {
        final float n = super.getWidth() * 3.0f;
        this.keystrokesModule.getClass();
        return n + 4.0f;
    }
    
    @Override
    public float getHeight() {
        return super.getHeight() / 2.0f;
    }
}
