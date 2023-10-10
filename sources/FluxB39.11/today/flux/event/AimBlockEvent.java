package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.BlockPos;

public class AimBlockEvent implements Event {

    public BlockPos To;

    public AimBlockEvent(BlockPos To) {
        this.To = To;
    }

}