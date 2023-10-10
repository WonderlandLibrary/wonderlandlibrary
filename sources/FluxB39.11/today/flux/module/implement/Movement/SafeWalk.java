package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.MoveEvent;
import today.flux.module.Category;
import today.flux.module.Module;

/**
 * Created by John on 2017/04/26.
 */
public class SafeWalk extends Module {
    public SafeWalk(){
        super("SafeWalk", Category.Movement, false);
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        e.setSafeWalk(true);
    }
}
