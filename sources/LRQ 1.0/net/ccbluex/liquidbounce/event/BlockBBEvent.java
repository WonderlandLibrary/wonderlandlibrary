/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class BlockBBEvent
extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final IBlock block;
    private IAxisAlignedBB boundingBox;

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }

    public final IBlock getBlock() {
        return this.block;
    }

    public final IAxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public final void setBoundingBox(@Nullable IAxisAlignedBB iAxisAlignedBB) {
        this.boundingBox = iAxisAlignedBB;
    }

    public BlockBBEvent(WBlockPos blockPos, IBlock block, @Nullable IAxisAlignedBB boundingBox) {
        this.block = block;
        this.boundingBox = boundingBox;
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }
}

