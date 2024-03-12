// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.input.Keyboard;
import dev.tenacity.utils.font.CustomFont;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import net.minecraft.client.settings.KeyBinding;
import dev.tenacity.utils.font.AbstractFontRenderer;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class Keystrokes extends Module
{
    private final NumberSetting offsetValue;
    private final NumberSetting sizeValue;
    private static final NumberSetting opacity;
    private static final NumberSetting radius;
    private final Dragging dragging;
    private Button keyBindForward;
    private Button keyBindLeft;
    private Button keyBindBack;
    private Button keyBindRight;
    private Button keyBindJump;
    
    public Keystrokes() {
        super("Keystrokes", "Keystrokes", Category.RENDER, "Shows keystrokes");
        this.offsetValue = new NumberSetting("Offset", 3.0, 10.0, 2.5, 0.5);
        this.sizeValue = new NumberSetting("Size", 25.0, 35.0, 15.0, 1.0);
        this.dragging = Tenacity.INSTANCE.createDrag(this, "keystrokes", 10.0f, 300.0f);
        this.addSettings(this.sizeValue, this.offsetValue, Keystrokes.opacity, Keystrokes.radius);
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        if (this.keyBindForward == null) {
            return;
        }
        final float offset = this.offsetValue.getValue().floatValue();
        final float x = this.dragging.getX();
        final float y = this.dragging.getY();
        final float width = this.dragging.getWidth();
        final float height = this.dragging.getHeight();
        final float size = this.sizeValue.getValue().floatValue();
        final float increment = size + offset;
        this.keyBindForward.renderForEffects(x + width / 2.0f - size / 2.0f, y, size, e);
        this.keyBindLeft.renderForEffects(x, y + increment, size, e);
        this.keyBindBack.renderForEffects(x + increment, y + increment, size, e);
        this.keyBindRight.renderForEffects(x + increment * 2.0f, y + increment, size, e);
        this.keyBindJump.renderForEffects(x, y + increment * 2.0f, width, size, e);
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        final float offset = this.offsetValue.getValue().floatValue();
        this.dragging.setHeight((float)((this.sizeValue.getValue() + offset) * 3.0) - offset);
        this.dragging.setWidth((float)((this.sizeValue.getValue() + offset) * 3.0) - offset);
        if (this.keyBindForward == null) {
            this.keyBindForward = new Button(Keystrokes.mc.gameSettings.keyBindForward);
            this.keyBindLeft = new Button(Keystrokes.mc.gameSettings.keyBindLeft);
            this.keyBindBack = new Button(Keystrokes.mc.gameSettings.keyBindBack);
            this.keyBindRight = new Button(Keystrokes.mc.gameSettings.keyBindRight);
            this.keyBindJump = new Button(Keystrokes.mc.gameSettings.keyBindJump);
        }
        final float x = this.dragging.getX();
        final float y = this.dragging.getY();
        final float width = this.dragging.getWidth();
        final float height = this.dragging.getHeight();
        final float size = this.sizeValue.getValue().floatValue();
        if (HUDMod.customFont.isEnabled()) {
            Button.font = Keystrokes.mc.fontRendererObj;
        }
        else {
            Button.font = Keystrokes.tenacityFont22;
        }
        final float increment = size + offset;
        this.keyBindForward.render(x + width / 2.0f - size / 2.0f, y, size);
        this.keyBindLeft.render(x, y + increment, size);
        this.keyBindBack.render(x + increment, y + increment, size);
        this.keyBindRight.render(x + increment * 2.0f, y + increment, size);
        this.keyBindJump.render(x, y + increment * 2.0f, width, size);
    }
    
    static {
        opacity = new NumberSetting("Opacity", 0.5, 1.0, 0.0, 0.05);
        radius = new NumberSetting("Radius", 3.0, 17.5, 1.0, 0.5);
    }
    
    public static class Button
    {
        private static AbstractFontRenderer font;
        private final KeyBinding binding;
        private final Animation clickAnimation;
        
        public Button(final KeyBinding binding) {
            this.clickAnimation = new DecelerateAnimation(125, 1.0);
            this.binding = binding;
        }
        
        public void renderForEffects(final float x, final float y, final float size, final ShaderEvent event) {
            this.renderForEffects(x, y, size, size, event);
        }
        
        public void renderForEffects(final float x, final float y, final float width, final float height, final ShaderEvent event) {
            Color color = Color.BLACK;
            if (event.getBloomOptions().getSetting("Keystrokes").isEnabled()) {
                color = ColorUtil.interpolateColorC(Color.BLACK, Color.WHITE, this.clickAnimation.getOutput().floatValue());
            }
            RoundedUtil.drawRound(x, y, width, height, Keystrokes.radius.getValue().floatValue(), color);
        }
        
        public void render(final float x, final float y, final float size) {
            this.render(x, y, size, size);
        }
        
        public void render(final float x, final float y, final float width, final float height) {
            final Color color = ColorUtil.applyOpacity(Color.BLACK, Keystrokes.opacity.getValue().floatValue());
            this.clickAnimation.setDirection(this.binding.isKeyDown() ? Direction.FORWARDS : Direction.BACKWARDS);
            RoundedUtil.drawRound(x, y, width, height, Keystrokes.radius.getValue().floatValue(), color);
            final float offsetX = (Button.font instanceof CustomFont) ? 0.0f : 0.5f;
            final int offsetY = (Button.font instanceof CustomFont) ? 0 : 1;
            Button.font.drawCenteredString(Keyboard.getKeyName(this.binding.getKeyCode()), x + width / 2.0f + offsetX, y + height / 2.0f - Button.font.getHeight() / 2.0f + offsetY, Color.WHITE);
            if (!this.clickAnimation.finished(Direction.BACKWARDS)) {
                final float animation = this.clickAnimation.getOutput().floatValue();
                final Color color2 = ColorUtil.applyOpacity(Color.WHITE, 0.5f * animation);
                RenderUtil.scaleStart(x + width / 2.0f, y + height / 2.0f, animation);
                final float diff = height / 2.0f - Keystrokes.radius.getValue().floatValue();
                RoundedUtil.drawRound(x, y, width, height, height / 2.0f - diff * animation, color2);
                RenderUtil.scaleEnd();
            }
        }
    }
}
