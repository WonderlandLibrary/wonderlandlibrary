package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import today.flux.event.PacketReceiveEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Movement.longjump.Dev;
import today.flux.module.implement.Movement.longjump.Hypixel;
import today.flux.module.implement.Movement.longjump.Normal;
import today.flux.module.implement.Movement.longjump.Old;
import today.flux.module.value.BooleanValue;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Category.Movement, true, new Normal(), new Old(), new Dev());
    }

    public static BooleanValue autoToggle = new BooleanValue("LongJump", "Auto Toggle", false);

    @EventTarget
    public void onLagback(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.toggle();
        }
    }
}
