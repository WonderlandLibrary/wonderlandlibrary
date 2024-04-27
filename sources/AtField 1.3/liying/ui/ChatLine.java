/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 */
package liying.ui;

import liying.utils.animation.SmoothAnimationTimer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.text.ITextComponent;

public class ChatLine
extends MinecraftInstance {
    public SmoothAnimationTimer posXTimer = new SmoothAnimationTimer(1.0f, 0.4f);
    public SmoothAnimationTimer alphaTimer;
    public float y = 0.0f;
    private final ITextComponent lineString;
    private final int chatLineID;
    public boolean a;
    private final int updateCounterCreated;
    public SmoothAnimationTimer posYTimer = new SmoothAnimationTimer(1.0f, 0.4f);
    public float tempY = 0.0f;

    public int getChatLineID() {
        return this.chatLineID;
    }

    public ITextComponent getChatComponent() {
        return this.lineString;
    }

    public ChatLine(int n, ITextComponent iTextComponent, int n2) {
        this.alphaTimer = new SmoothAnimationTimer(1.0f, 0.15f);
        this.lineString = iTextComponent;
        this.updateCounterCreated = n;
        this.chatLineID = n2;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
}

