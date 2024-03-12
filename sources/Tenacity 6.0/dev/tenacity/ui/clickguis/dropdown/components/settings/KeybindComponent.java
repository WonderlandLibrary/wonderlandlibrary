// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import org.lwjgl.input.Keyboard;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class KeybindComponent extends SettingComponent<KeybindSetting>
{
    private boolean binding;
    private final Animation clickAnimation;
    private final Animation hoverAnimation;
    
    public KeybindComponent(final KeybindSetting keybindSetting) {
        super(keybindSetting);
        this.clickAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.binding) {
            if (keyCode == 57 || keyCode == 1 || keyCode == 211) {
                this.getSetting().setCode(0);
            }
            else {
                this.getSetting().setCode(keyCode);
            }
            this.typing = false;
            this.binding = false;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.clickAnimation.setDirection(this.binding ? Direction.FORWARDS : Direction.BACKWARDS);
        final String bind = Keyboard.getKeyName(this.getSetting().getCode());
        final float fullTextWidth = KeybindComponent.tenacityFont16.getStringWidth("Bind: §l" + bind);
        final float startX = this.x + this.width / 2.0f - fullTextWidth / 2.0f;
        final float startY = this.y + KeybindComponent.tenacityFont16.getMiddleOfBox(this.height);
        final boolean hovering = HoveringUtil.isHovering(startX - 3.0f, startY - 2.0f, fullTextWidth + 6.0f, (float)(KeybindComponent.tenacityFont16.getHeight() + 4), mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color rectColor = ColorUtil.brighter(this.settingRectColor, 0.7f - 0.25f * this.hoverAnimation.getOutput().floatValue());
        RoundedUtil.drawRound(startX - 3.0f, startY - 2.0f, fullTextWidth + 6.0f, (float)(KeybindComponent.tenacityFont16.getHeight() + 4), 4.0f, rectColor);
        KeybindComponent.tenacityFont16.drawCenteredString("Bind: §l" + bind, this.x + this.width / 2.0f, this.y + KeybindComponent.tenacityFont16.getMiddleOfBox(this.height), this.textColor);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final String bind = Keyboard.getKeyName(this.getSetting().getCode());
        final String text = "§fBind: §r" + bind;
        final float textWidth = KeybindComponent.tenacityFont18.getStringWidth(text);
        final float startX = this.x + this.width / 2.0f - textWidth / 2.0f;
        final float startY = this.y + KeybindComponent.tenacityFont18.getMiddleOfBox(this.height);
        final float rectHeight = (float)(KeybindComponent.tenacityFont18.getHeight() + 4);
        final boolean hovering = HoveringUtil.isHovering(startX - 3.0f, startY - 2.0f, textWidth + 6.0f, (float)(KeybindComponent.tenacityFont18.getHeight() + 4), mouseX, mouseY);
        if (this.isClickable(startY + rectHeight) && hovering && button == 0) {
            this.binding = true;
            this.typing = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
