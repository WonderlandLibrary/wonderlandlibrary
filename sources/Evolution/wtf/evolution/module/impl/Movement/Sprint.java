package wtf.evolution.module.impl.Movement;

import net.minecraft.network.play.client.CPacketEntityAction;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;

@ModuleInfo(name = "Sprint", type = Category.Movement)
public class Sprint extends Module {

    public BooleanSetting allmode = new BooleanSetting("All - Direction", false).call(this);

@EventTarget
public void onUpdate(EventMotion e) {
    if (allmode.get())
        mc.player.setSprinting(MovementUtil.isMoving());
    else
        mc.player.setSprinting(mc.player.moveForward > 0);
}

@EventTarget
public void onUpdate(EventPacket e) {
    if (e.getPacket() instanceof CPacketEntityAction) {
        CPacketEntityAction packet = (CPacketEntityAction) e.getPacket();
        if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING) {
            e.cancel();
        }
        if (packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
            e.cancel();
        }
       
    }
}

}
