package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FastSneak extends AbstractModule {

    public FastSneak(@NonNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MOVEMENT, "FastSneak", "Fast Sneak");
    }

    @EventTarget
    public void onPre(MotionUpdateEvent event) {
        if (mc.player.movementInput().sneak() && mc.player.isMoving()) {
            if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
                sendPacketNoEvent(new C0BPacketEntityAction(mc.player, C0BPacketEntityAction.Action.STOP_SNEAKING));
            } else {
                sendPacketNoEvent(new C0BPacketEntityAction(mc.player, C0BPacketEntityAction.Action.START_SNEAKING));
            }
        }
    }
}
