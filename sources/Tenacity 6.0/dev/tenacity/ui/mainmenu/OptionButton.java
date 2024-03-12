// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu;

import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.awt.Color;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class OptionButton implements Screen
{
    private Animation hoverAnimation;
    private final String icon;
    public final String name;
    public float x;
    public float y;
    public float iconAdjustY;
    public Color color;
    public float width;
    public float height;
    public Runnable clickAction;
    
    public OptionButton(final String icon, final String name) {
        this.icon = icon;
        this.name = name;
    }
    
    public OptionButton(final String name) {
        this.icon = null;
        this.name = name;
    }
    
    @Override
    public void initGui() {
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.hoverAnimation.setDirection(HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY) ? Direction.FORWARDS : Direction.BACKWARDS);
        RoundedUtil.drawRound(this.x - 69.0f, this.y, this.width + 69.0f, this.height, 6.0f, ColorUtil.applyOpacity(Color.BLACK, 0.2f));
        RoundedUtil.drawRound(this.x - 10.0f, this.y, (this.width + 10.0f) * this.hoverAnimation.getOutput().floatValue(), this.height, 6.0f, ColorUtil.applyOpacity(this.color, 0.5f));
        Gui.drawRect2(this.x, this.y, 3.0, this.height, ColorUtil.applyOpacity(this.color, this.hoverAnimation.getOutput().floatValue()).getRGB());
        final float middle = this.y + OptionButton.tenacityBoldFont40.getMiddleOfBox(this.height);
        OptionButton.tenacityBoldFont40.drawString(this.name, this.x + 60.0f, this.y + OptionButton.tenacityBoldFont40.getMiddleOfBox(this.height), -1);
        if (this.icon != null) {
            FontUtil.iconFont40.drawString(this.icon, this.x + 20.0f, middle + this.iconAdjustY, -1);
        }
        else {
            final float iconWidth = 19.0f;
            final float iconHeight = 13.5f;
            GLUtil.startBlend();
            OptionButton.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/MainMenu/discord.png"));
            Gui.drawModalRectWithCustomSizedTexture(this.x + 20.0f, middle + this.iconAdjustY, 0.0f, 0.0f, iconWidth, iconHeight, iconWidth, iconHeight);
            GLUtil.endBlend();
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            this.clickAction.run();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
