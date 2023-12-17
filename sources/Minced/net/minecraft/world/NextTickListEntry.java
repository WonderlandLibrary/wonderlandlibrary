// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;

public class NextTickListEntry implements Comparable<NextTickListEntry>
{
    private static long nextTickEntryID;
    private final Block block;
    public final BlockPos position;
    public long scheduledTime;
    public int priority;
    private final long tickEntryID;
    
    public NextTickListEntry(final BlockPos positionIn, final Block blockIn) {
        this.tickEntryID = NextTickListEntry.nextTickEntryID++;
        this.position = positionIn.toImmutable();
        this.block = blockIn;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
        return this.position.equals(nextticklistentry.position) && Block.isEqualTo(this.block, nextticklistentry.block);
    }
    
    @Override
    public int hashCode() {
        return this.position.hashCode();
    }
    
    public NextTickListEntry setScheduledTime(final long scheduledTimeIn) {
        this.scheduledTime = scheduledTimeIn;
        return this;
    }
    
    public void setPriority(final int priorityIn) {
        this.priority = priorityIn;
    }
    
    @Override
    public int compareTo(final NextTickListEntry p_compareTo_1_) {
        if (this.scheduledTime < p_compareTo_1_.scheduledTime) {
            return -1;
        }
        if (this.scheduledTime > p_compareTo_1_.scheduledTime) {
            return 1;
        }
        if (this.priority != p_compareTo_1_.priority) {
            return this.priority - p_compareTo_1_.priority;
        }
        if (this.tickEntryID < p_compareTo_1_.tickEntryID) {
            return -1;
        }
        return (this.tickEntryID > p_compareTo_1_.tickEntryID) ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
