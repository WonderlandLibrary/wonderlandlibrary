/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import tk.rektsky.event.Event;

public class RenderPlayerEvent
extends Event {
    public AbstractClientPlayer player;
    public double x;
    public double y;
    public double z;
    public float entityYaw;
    public float partialTicks;

    public RenderPlayerEvent(AbstractClientPlayer player, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        this.player = player;
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }
}

