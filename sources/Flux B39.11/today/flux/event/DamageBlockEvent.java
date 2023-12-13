package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class DamageBlockEvent implements Event {

    private BlockPos pos;
    private EnumFacing face;

    public DamageBlockEvent(BlockPos pos, EnumFacing face) {
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