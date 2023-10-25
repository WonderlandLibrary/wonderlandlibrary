/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;

public class ChatLine {
    private final IChatComponent lineString;
    private final int updateCounterCreated;
    private final int chatLineID;

    public ChatLine(int n, IChatComponent iChatComponent, int n2) {
        this.lineString = iChatComponent;
        this.updateCounterCreated = n;
        this.chatLineID = n2;
    }

    public IChatComponent getChatComponent() {
        return this.lineString;
    }

    public int getChatLineID() {
        return this.chatLineID;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
}

