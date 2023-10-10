package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import today.flux.addon.api.packet.AddonPacket;

public class PacketAction extends AddonPacket {
    public PacketAction(C0BPacketEntityAction packet) {
        super(packet);
    }

    public PacketAction(Action action) {
        super(null);
        this.nativePacket = new C0BPacketEntityAction(mc.thePlayer, action.getAction());
    }


    public Action getAction() {
        return Action.getAction(((C0BPacketEntityAction) nativePacket).getAction());
    }


    public void setAction(Action action) {
        ((C0BPacketEntityAction) this.nativePacket).setAction(action.getAction());
    }

    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        RIDING_JUMP,
        OPEN_INVENTORY;

        public static Action getAction(C0BPacketEntityAction.Action action) {
            if (action == C0BPacketEntityAction.Action.START_SNEAKING) {
                return Action.START_SNEAKING;
            } else if (action == C0BPacketEntityAction.Action.STOP_SNEAKING) {
                return Action.STOP_SNEAKING;
            } else if (action == C0BPacketEntityAction.Action.STOP_SLEEPING) {
                return Action.STOP_SLEEPING;
            } else if (action == C0BPacketEntityAction.Action.START_SPRINTING) {
                return Action.START_SPRINTING;
            } else if (action == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                return Action.STOP_SPRINTING;
            } else if (action == C0BPacketEntityAction.Action.RIDING_JUMP) {
                return Action.RIDING_JUMP;
            } else {
                return Action.OPEN_INVENTORY;
            }
        }


        public C0BPacketEntityAction.Action getAction() {
            if (this == Action.START_SNEAKING) {
                return C0BPacketEntityAction.Action.START_SNEAKING;
            } else if (this == Action.STOP_SNEAKING) {
                return C0BPacketEntityAction.Action.STOP_SNEAKING;
            } else if (this == Action.STOP_SLEEPING) {
                return C0BPacketEntityAction.Action.STOP_SLEEPING;
            } else if (this == Action.START_SPRINTING) {
                return C0BPacketEntityAction.Action.START_SPRINTING;
            } else if (this == Action.STOP_SPRINTING) {
                return C0BPacketEntityAction.Action.STOP_SPRINTING;
            } else if (this == Action.RIDING_JUMP) {
                return C0BPacketEntityAction.Action.RIDING_JUMP;
            } else {
                return C0BPacketEntityAction.Action.OPEN_INVENTORY;
            }
        }
    }
}
