// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiLockIconButton extends GuiButton
{
    private boolean locked;
    
    public GuiLockIconButton(final int buttonId, final int x, final int y) {
        super(buttonId, x, y, 20, 20, "");
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void setLocked(final boolean lockedIn) {
        this.locked = lockedIn;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            Icon guilockiconbutton$icon;
            if (this.locked) {
                if (!this.enabled) {
                    guilockiconbutton$icon = Icon.LOCKED_DISABLED;
                }
                else if (flag) {
                    guilockiconbutton$icon = Icon.LOCKED_HOVER;
                }
                else {
                    guilockiconbutton$icon = Icon.LOCKED;
                }
            }
            else if (!this.enabled) {
                guilockiconbutton$icon = Icon.UNLOCKED_DISABLED;
            }
            else if (flag) {
                guilockiconbutton$icon = Icon.UNLOCKED_HOVER;
            }
            else {
                guilockiconbutton$icon = Icon.UNLOCKED;
            }
            this.drawTexturedModalRect(this.x, this.y, guilockiconbutton$icon.getX(), guilockiconbutton$icon.getY(), this.width, this.height);
        }
    }
    
    enum Icon
    {
        LOCKED(0, 146), 
        LOCKED_HOVER(0, 166), 
        LOCKED_DISABLED(0, 186), 
        UNLOCKED(20, 146), 
        UNLOCKED_HOVER(20, 166), 
        UNLOCKED_DISABLED(20, 186);
        
        private final int x;
        private final int y;
        
        private Icon(final int xIn, final int yIn) {
            this.x = xIn;
            this.y = yIn;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
    }
}
