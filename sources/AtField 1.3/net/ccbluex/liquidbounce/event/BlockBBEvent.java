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
    private final IBlock block;
    private final int x;
    private final int y;
    private IAxisAlignedBB boundingBox;
    private final int z;

    public final void setBoundingBox(@Nullable IAxisAlignedBB iAxisAlignedBB) {
        this.boundingBox = iAxisAlignedBB;
    }

    public final int getX() {
        return this.x;
    }

    public final IBlock getBlock() {
        return this.block;
    }

    public final IAxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public final int getZ() {
        return this.z;
    }

    public final int getY() {
        return this.y;
    }

    public BlockBBEvent(WBlockPos wBlockPos, IBlock iBlock, @Nullable IAxisAlignedBB iAxisAlignedBB) {
        this.block = iBlock;
        this.boundingBox = iAxisAlignedBB;
        this.x = wBlockPos.getX();
        this.y = wBlockPos.getY();
        this.z = wBlockPos.getZ();
    }
}

