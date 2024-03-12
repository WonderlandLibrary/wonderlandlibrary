// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu;

import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class MenuButton implements Screen
{
    public final String text;
    private Animation hoverAnimation;
    public float x;
    public float y;
    public float width;
    public float height;
    public Runnable clickAction;
    private static final ResourceLocation rs;
    
    public MenuButton(final String text) {
        this.text = text;
    }
    
    @Override
    public void initGui() {
        this.hoverAnimation = new DecelerateAnimation(200, 1.0);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final boolean hovered = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtil.color(-1);
        RenderUtil.drawImage(MenuButton.rs, this.x, this.y, this.width, this.height);
        MenuButton.tenacityFont22.drawCenteredString(this.text, this.x + this.width / 2.0f, this.y + MenuButton.tenacityFont22.getMiddleOfBox(this.height), -1);
    }
    
    public void drawOutline() {
        RenderUtil.drawImage(MenuButton.rs, this.x, this.y, this.width, this.height);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hovered = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (hovered) {
            this.clickAction.run();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    static {
        rs = new ResourceLocation("Tenacity/MainMenu/menu-rect.png");
    }
}
