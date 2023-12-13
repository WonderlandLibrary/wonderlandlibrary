// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;

public class GuiLockIconButton extends GuiButton
{
    private boolean field_175231_o;
    private static final String __OBFID = "CL_00001952";
    
    public GuiLockIconButton(final int p_i45538_1_, final int p_i45538_2_, final int p_i45538_3_) {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
        this.field_175231_o = false;
    }
    
    public boolean func_175230_c() {
        return this.field_175231_o;
    }
    
    public void func_175229_b(final boolean p_175229_1_) {
        this.field_175231_o = p_175229_1_;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Icon var5;
            if (this.field_175231_o) {
                if (!this.enabled) {
                    var5 = Icon.LOCKED_DISABLED;
                }
                else if (var4) {
                    var5 = Icon.LOCKED_HOVER;
                }
                else {
                    var5 = Icon.LOCKED;
                }
            }
            else if (!this.enabled) {
                var5 = Icon.UNLOCKED_DISABLED;
            }
            else if (var4) {
                var5 = Icon.UNLOCKED_HOVER;
            }
            else {
                var5 = Icon.UNLOCKED;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, var5.func_178910_a(), var5.func_178912_b(), this.width, this.height);
        }
    }
    
    enum Icon
    {
        LOCKED("LOCKED", 0, 0, 146), 
        LOCKED_HOVER("LOCKED_HOVER", 1, 0, 166), 
        LOCKED_DISABLED("LOCKED_DISABLED", 2, 0, 186), 
        UNLOCKED("UNLOCKED", 3, 20, 146), 
        UNLOCKED_HOVER("UNLOCKED_HOVER", 4, 20, 166), 
        UNLOCKED_DISABLED("UNLOCKED_DISABLED", 5, 20, 186);
        
        private final int field_178914_g;
        private final int field_178920_h;
        private static final Icon[] $VALUES;
        private static final String __OBFID = "CL_00001951";
        
        private Icon(final String p_i45537_1_, final int p_i45537_2_, final int p_i45537_3_, final int p_i45537_4_) {
            this.field_178914_g = p_i45537_3_;
            this.field_178920_h = p_i45537_4_;
        }
        
        public int func_178910_a() {
            return this.field_178914_g;
        }
        
        public int func_178912_b() {
            return this.field_178920_h;
        }
        
        static {
            $VALUES = new Icon[] { Icon.LOCKED, Icon.LOCKED_HOVER, Icon.LOCKED_DISABLED, Icon.UNLOCKED, Icon.UNLOCKED_HOVER, Icon.UNLOCKED_DISABLED };
        }
    }
}
