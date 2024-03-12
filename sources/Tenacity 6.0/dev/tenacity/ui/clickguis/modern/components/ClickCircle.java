// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern.components;

import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class ClickCircle extends Component
{
    public final Animation fadeAnimation;
    private final Animation scaleAnimation;
    
    public ClickCircle() {
        this.fadeAnimation = new DecelerateAnimation(150, 1.0, Direction.FORWARDS);
        this.scaleAnimation = new DecelerateAnimation(450, 1.0);
        this.fadeAnimation.setDirection(Direction.FORWARDS);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        if (this.fadeAnimation.isDone() && this.fadeAnimation.getDirection().equals(Direction.FORWARDS)) {
            this.fadeAnimation.setDirection(Direction.BACKWARDS);
            this.fadeAnimation.setDuration(300);
        }
        GlStateManager.alphaFunc(516, 0.15f);
        final int color = ColorUtil.interpolateColor(new Color(249, 249, 249, 28), new Color(255, 255, 240), this.fadeAnimation.getOutput().floatValue());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.drawUnfilledCircle(this.x, this.y, 1.0f + 6.0f * this.scaleAnimation.getOutput().floatValue(), 4.0f, color);
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
    }
}
