/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.Block;

public class EventBlockRender
implements Event {
    private int x;
    private int y;
    private int z;
    private final Block block;

    public EventBlockRender(int x2, int y2, int z2, Block block) {
        this.block = block;
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Block getBlock() {
        return this.block;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public void setY(int y2) {
        this.y = y2;
    }

    public void setZ(int z2) {
        this.z = z2;
    }
}

