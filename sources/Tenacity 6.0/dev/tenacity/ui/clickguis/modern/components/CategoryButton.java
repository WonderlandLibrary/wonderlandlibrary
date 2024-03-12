// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern.components;

import dev.tenacity.utils.font.FontUtil;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.impl.EaseInOutQuad;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Category;

public class CategoryButton extends Component
{
    public final Category category;
    public Category currentCategory;
    private Animation hoverAnimation;
    private Animation enableAnimation;
    public Animation expandAnimation;
    
    public CategoryButton(final Category category) {
        this.category = category;
    }
    
    @Override
    public void initGui() {
        this.hoverAnimation = new EaseInOutQuad(200, 1.0);
        this.enableAnimation = new DecelerateAnimation(250, 1.0);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final boolean hovering = HoveringUtil.isHovering(this.x, this.y - 3.0f, (float)(83 - (this.expandAnimation.getDirection().forwards() ? 62 : 0)), 18.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        this.hoverAnimation.setDuration(hovering ? 200 : 350);
        this.enableAnimation.setDirection((this.currentCategory == this.category) ? Direction.FORWARDS : Direction.BACKWARDS);
        int color = ColorUtil.interpolateColor(new Color(68, 71, 78), new Color(115, 115, 115), this.hoverAnimation.getOutput().floatValue());
        color = ColorUtil.interpolateColor(new Color(color), new Color(-1), this.enableAnimation.getOutput().floatValue());
        float adjustment = 0.0f;
        if (this.category == Category.COMBAT) {
            adjustment = 2.5f;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        FontUtil.iconFont35.drawCenteredString(this.category.icon, this.x + 10.0f + adjustment, this.y, color);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final float xDiff = 10.0f * this.expandAnimation.getOutput().floatValue();
        CategoryButton.tenacityFont24.drawString(this.category.name, this.x + 27.0f + xDiff, this.y, color);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hovering = HoveringUtil.isHovering(this.x, this.y - 3.0f, (float)(83 - (this.expandAnimation.getDirection().forwards() ? 62 : 0)), 18.0f, mouseX, mouseY);
        this.hovering = (hovering && button == 0);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
    }
}
