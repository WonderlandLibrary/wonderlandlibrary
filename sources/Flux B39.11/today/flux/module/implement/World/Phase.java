package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.World.phase.*;

/**
 * Created by John on 2017/04/27.
 */
//Todo: Skid Phases!
public class Phase extends Module {
    public Phase() {
        super("Phase", Category.World, true, new Skip(), new Old(), new Full(), new Clip()/*, new Hypixel()*/);
    }
}
