package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.flux.event.MoveEvent;
import today.flux.event.PacketReceiveEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;

/**
 * Created by John on 2017/06/28.
 */
public class Timer extends Module {

    public static FloatValue timerspeed = new FloatValue("Timer", "Timer Speed", 1.5f, 0.1f, 10.0f, 0.1f);
    public static BooleanValue lagbackCheck = new BooleanValue("Timer", "Lagback Check", true);

    public Timer() {
        super("Timer", Category.World, false);
    }

    @Override
    public void onEnable() {

        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.getTimer().timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onLagback(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && lagbackCheck.getValueState()) {
            this.toggle();
        }
    }

    @EventTarget
    public void onTick(MoveEvent event) {
        mc.getTimer().timerSpeed = timerspeed.getValue();
    }
}
