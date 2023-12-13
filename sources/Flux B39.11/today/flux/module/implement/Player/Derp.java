package today.flux.module.implement.Player;

import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.utility.DelayTimer;
import today.flux.module.value.BooleanValue;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Derp extends Module {
    int yaw = 0;

    public static BooleanValue rotate = new BooleanValue("Derp", "Rotate", true);
    public static BooleanValue dance = new BooleanValue("Derp", "Dance", true);

    private boolean kusshin;
    private DelayTimer timer = new DelayTimer();

    public Derp() {
        super("Derp", Category.Player, false);
    }

    public void onDisable() {
        super.onDisable();

        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

    @EventTarget(Priority.LOWEST)
    private void onUpdate(final PreUpdateEvent event) {
        if (rotate.getValue()) {
            if (!event.isModified()) {

                yaw++;
                if (yaw > 360)
                    yaw = 0;

                event.setYaw(yaw);
                event.setPitch(10);
            }
        }

        if (dance.getValue()) {
            if (kusshin)
            	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @EventTarget(Priority.LOWEST)
    private void onUpdatePost(final PostUpdateEvent event) {
        if (dance.getValue()) {
            if (timer.hasPassed(100)) {
                kusshin = !kusshin;
                timer.reset();
            }

            if (kusshin)
            	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }
}