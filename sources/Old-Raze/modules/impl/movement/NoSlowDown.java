package markgg.modules.impl.movement;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.util.MoveUtil;
import markgg.util.timer.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "NoSlowDown", category = Module.Category.MOVEMENT)
public class NoSlowDown extends Module{

	public Timer timer = new Timer();
	public BooleanSetting ncp = new BooleanSetting("NCP", this, true);

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if (ncp.getValue() && mc.thePlayer.isBlocking() && MoveUtil.isMoving() && mc.thePlayer.onGround) {
				mc.playerController.syncCurrentPlayItem();
				mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
		} else {
			mc.playerController.syncCurrentPlayItem();
			mc.getNetHandler().addToSendQueueSilent((Packet) new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
		}
	};

}
