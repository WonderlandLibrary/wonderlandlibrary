/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class NextTickListEntry
implements Comparable<NextTickListEntry> {
    private long tickEntryID = nextTickEntryID++;
    private static long nextTickEntryID;
    public int priority;
    private final Block block;
    public final BlockPos position;
    public long scheduledTime;

    @Override
    public int compareTo(NextTickListEntry nextTickListEntry) {
        return this.scheduledTime < nextTickListEntry.scheduledTime ? -1 : (this.scheduledTime > nextTickListEntry.scheduledTime ? 1 : (this.priority != nextTickListEntry.priority ? this.priority - nextTickListEntry.priority : (this.tickEntryID < nextTickListEntry.tickEntryID ? -1 : (this.tickEntryID > nextTickListEntry.tickEntryID ? 1 : 0))));
    }

    public int hashCode() {
        return this.position.hashCode();
    }

    public NextTickListEntry(BlockPos blockPos, Block block) {
        this.position = blockPos;
        this.block = block;
    }

    public void setPriority(int n) {
        this.priority = n;
    }

    public NextTickListEntry setScheduledTime(long l) {
        this.scheduledTime = l;
        return this;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextTickListEntry = (NextTickListEntry)object;
        return this.position.equals(nextTickListEntry.position) && Block.isEqualTo(this.block, nextTickListEntry.block);
    }

    public String toString() {
        return String.valueOf(Block.getIdFromBlock(this.block)) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }
}

