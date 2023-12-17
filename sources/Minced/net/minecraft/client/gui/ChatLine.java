// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

public class ChatLine
{
    private final int updateCounterCreated;
    private final ITextComponent lineString;
    private float posX;
    private float posY;
    private final int chatLineID;
    
    public ChatLine(final int p_i45000_1_, final ITextComponent p_i45000_2_, final int p_i45000_3_) {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
        this.posX = (float)(-Minecraft.getMinecraft().fontRenderer.getStringWidth(p_i45000_2_.getUnformattedText()));
        this.posY = (float)(-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT);
    }
    
    public ITextComponent getChatComponent() {
        return this.lineString;
    }
    
    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
    
    public int getChatLineID() {
        return this.chatLineID;
    }
    
    public float getPosX() {
        return this.posX;
    }
    
    public void setPosX(final float posX) {
        this.posX = posX;
    }
    
    public float getPosY() {
        return this.posY;
    }
    
    public void setPosY(final float posY) {
        this.posY = posY;
    }
}
