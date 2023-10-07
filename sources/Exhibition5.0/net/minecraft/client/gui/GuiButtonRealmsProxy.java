// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsButton;

public class GuiButtonRealmsProxy extends GuiButton
{
    private RealmsButton field_154318_o;
    private static final String __OBFID = "CL_00001848";
    
    public GuiButtonRealmsProxy(final RealmsButton p_i46321_1_, final int p_i46321_2_, final int p_i46321_3_, final int p_i46321_4_, final String p_i46321_5_) {
        super(p_i46321_2_, p_i46321_3_, p_i46321_4_, p_i46321_5_);
        this.field_154318_o = p_i46321_1_;
    }
    
    public GuiButtonRealmsProxy(final RealmsButton p_i1090_1_, final int p_i1090_2_, final int p_i1090_3_, final int p_i1090_4_, final String p_i1090_5_, final int p_i1090_6_, final int p_i1090_7_) {
        super(p_i1090_2_, p_i1090_3_, p_i1090_4_, p_i1090_6_, p_i1090_7_, p_i1090_5_);
        this.field_154318_o = p_i1090_1_;
    }
    
    public int getId() {
        return super.id;
    }
    
    public boolean getEnabled() {
        return super.enabled;
    }
    
    public void setEnabled(final boolean p_154313_1_) {
        super.enabled = p_154313_1_;
    }
    
    public void setText(final String p_154311_1_) {
        super.displayString = p_154311_1_;
    }
    
    @Override
    public int getButtonWidth() {
        return super.getButtonWidth();
    }
    
    public int getPositionY() {
        return super.yPosition;
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.field_154318_o.clicked(mouseX, mouseY);
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.field_154318_o.released(mouseX, mouseY);
    }
    
    public void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        this.field_154318_o.renderBg(mouseX, mouseY);
    }
    
    public RealmsButton getRealmsButton() {
        return this.field_154318_o;
    }
    
    public int getHoverState(final boolean mouseOver) {
        return this.field_154318_o.getYImage(mouseOver);
    }
    
    public int func_154312_c(final boolean p_154312_1_) {
        return super.getHoverState(p_154312_1_);
    }
    
    public int func_175232_g() {
        return this.height;
    }
}
