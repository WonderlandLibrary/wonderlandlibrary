/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class NextTickListEntry
implements Comparable<NextTickListEntry> {
    private static long nextTickEntryID;
    private final Block block;
    public final BlockPos position;
    public long scheduledTime;
    public int priority;
    private long tickEntryID = nextTickEntryID++;

    public NextTickListEntry(BlockPos p_i45745_1_, Block p_i45745_2_) {
        this.position = p_i45745_1_;
        this.block = p_i45745_2_;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
        if (!this.position.equals(nextticklistentry.position)) return false;
        if (!Block.isEqualTo(this.block, nextticklistentry.block)) return false;
        return true;
    }

    public int hashCode() {
        return this.position.hashCode();
    }

    public NextTickListEntry setScheduledTime(long p_77176_1_) {
        this.scheduledTime = p_77176_1_;
        return this;
    }

    public void setPriority(int p_82753_1_) {
        this.priority = p_82753_1_;
    }

    @Override
    public int compareTo(NextTickListEntry p_compareTo_1_) {
        if (this.scheduledTime < p_compareTo_1_.scheduledTime) {
            return -1;
        }
        if (this.scheduledTime > p_compareTo_1_.scheduledTime) {
            return 1;
        }
        if (this.priority != p_compareTo_1_.priority) {
            int n = this.priority - p_compareTo_1_.priority;
            return n;
        }
        if (this.tickEntryID < p_compareTo_1_.tickEntryID) {
            return -1;
        }
        if (this.tickEntryID <= p_compareTo_1_.tickEntryID) return 0;
        return 1;
    }

    public String toString() {
        return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }

    public Block getBlock() {
        return this.block;
    }
}

