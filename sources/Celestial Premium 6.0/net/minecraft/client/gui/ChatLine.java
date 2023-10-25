/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

public class ChatLine {
    private final int updateCounterCreated;
    private final ITextComponent lineString;
    private float posX;
    private float posY;
    private final int chatLineID;

    public ChatLine(int p_i45000_1_, ITextComponent p_i45000_2_, int p_i45000_3_) {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
        this.posX = -Minecraft.getMinecraft().fontRendererObj.getStringWidth(p_i45000_2_.getUnformattedText());
        this.posY = -Minecraft.getMinecraft().fontRendererObj.getStringHeight(p_i45000_2_.getUnformattedText());
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

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }
}

