/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import org.celestial.client.event.events.Event;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventCollide
extends EventCancellable
implements Event {
    public AxisAlignedBB axisAlignedBB;
    public Block block;
    public int x;
    public int y;
    public int z;

    public EventCollide(AxisAlignedBB axisAlignedBB, Block block, int x, int y, int z) {
        this.axisAlignedBB = axisAlignedBB;
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return this.axisAlignedBB;
    }

    public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Block getBlock() {
        return this.block;
    }
}

