/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations;

public enum Direction {
    FORWARDS,
    BACKWARDS;


    public Direction opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        }
        return FORWARDS;
    }
}

