package org.dreamcore.client.event.events.impl.player;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventBlockInteract extends EventCancellable {

    private BlockPos pos;
    private EnumFacing face;

    public EventBlockInteract(BlockPos pos, EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public EnumFacing getFace() {
        return face;
    }

    public void setFace(EnumFacing face) {
        this.face = face;
    }
}