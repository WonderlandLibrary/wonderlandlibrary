// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.keystrokes.keys;

import moonsense.MoonsenseClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import moonsense.features.modules.type.keystrokes.KeystrokesModule;
import net.minecraft.client.settings.KeyBinding;

public class KeyMouse extends Key
{
    private Type type;
    
    public KeyMouse(final int gapSize, final KeyBinding keyBinding, final KeystrokesModule keystrokesModule) {
        super(gapSize, keyBinding, keystrokesModule);
    }
    
    @Override
    public void render(final boolean dummy) {
        final boolean pressed = !dummy && this.pressed();
        final float pressModifier = Math.min(1.0f, (float)((System.currentTimeMillis() - this.pressTime) / this.keystrokesModule.boxFade.getInt()));
        final float brightness = (pressed ? pressModifier : (1.0f - pressModifier)) * 0.8f;
        this.renderBackground(pressed, brightness);
        final String keyname = (this.type == Type.LEFT) ? "LMB" : "RMB";
        final boolean cps = this.keystrokesModule.showCPS.getBoolean();
        this.drawString(keyname, (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f, cps ? (this.getHeight() / 5.0f) : (this.getHeight() / 2.0f - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2.0f), pressed);
        if (cps) {
            GlStateManager.pushMatrix();
            final float cpsScale = 0.65f;
            GlStateManager.scale(0.65f, 0.65f, 1.0f);
            final String cpsText = String.valueOf((this.type == Type.LEFT) ? MoonsenseClient.left.getCPS() : MoonsenseClient.right.getCPS()) + " CPS";
            this.drawString(cpsText, (this.getWidth() / 0.65f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(cpsText)) / 2.0f, this.getHeight() / 0.65f - this.getHeight() / 5.0f - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, pressed);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public float getWidth() {
        final float n = super.getWidth() + super.getWidth() / 2.0f;
        this.keystrokesModule.getClass();
        return n + 1.0f;
    }
    
    @Override
    public float getHeight() {
        return super.getHeight() + super.getHeight() / 5.0f;
    }
    
    public KeyMouse setLeft() {
        this.type = Type.LEFT;
        return this;
    }
    
    public KeyMouse setRight() {
        this.type = Type.RIGHT;
        return this;
    }
    
    public enum Type
    {
        LEFT("LEFT", 0), 
        RIGHT("RIGHT", 1);
        
        private Type(final String name, final int ordinal) {
        }
    }
}
