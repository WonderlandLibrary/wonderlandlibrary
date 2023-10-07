package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import static net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SPRINTING;

public final class Sprint extends AbstractModule {

    private boolean sprinting;

    /* constructors @on */
    public Sprint(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Sprint", "Sprint", Keyboard.KEY_NONE, EnumModuleType.MOVEMENT);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (event.getPacket() instanceof C0BPacketEntityAction) {
                C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();

                if (packet.getAction().name().toLowerCase().contains("sprint")) {
                    if (mc.player.onGround) {
                        if (packet.getAction().equals(START_SPRINTING)) {
                            sprinting = true;
                        } else if (packet.getAction().equals(STOP_SPRINTING)) {
                            sprinting = false;
                        }
                    }

                    event.setCancelled(true);
                }
            }
        }
    }

    /* events */
    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            mc.player.setSprinting(mc.player.isMoving());

            if (mc.player.onGround && !isEnabled(Scaffold.class)) {
                if (mc.player.isSprinting() != sprinting) {
                    C0BPacketEntityAction packetEntity = new C0BPacketEntityAction(mc.player, mc.player.isSprinting() ? START_SPRINTING : STOP_SPRINTING);
                    sendPacketNoEvent(packetEntity);
                    sprinting = mc.player.isSprinting();
                }
            }
        }
    }
}
