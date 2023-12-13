package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.BlockPosition;
import today.flux.addon.api.utils.Facing;

public class PacketDigging extends AddonPacket {

    public PacketDigging(C07PacketPlayerDigging packet) {
        super(packet);
    }

    public PacketDigging(BlockPosition position, Facing facing, Action action) {
        super(null);
        this.nativePacket = new C07PacketPlayerDigging(action.getAction(), position.getNativeBlockPos(), facing.getFacing());
    }


    public BlockPosition getPosition() {
        return BlockPosition.getBlockPosition(((C07PacketPlayerDigging) nativePacket).getPosition());
    }


    public void setPosition(BlockPosition position) {
        ((C07PacketPlayerDigging) nativePacket).position = position.getNativeBlockPos();
    }


    public Facing getFacing() {
        return Facing.getFacing(((C07PacketPlayerDigging) nativePacket).getFacing());
    }


    public void setFacing(Facing facing) {
        ((C07PacketPlayerDigging) nativePacket).facing = facing.getFacing();
    }


    public Action getAction() {
        return Action.getAction(((C07PacketPlayerDigging) nativePacket).getStatus());
    }


    public void setAction(Action action) {
        ((C07PacketPlayerDigging) nativePacket).status = action.getAction();
    }

    public enum Action {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM;

        public static Action getAction(C07PacketPlayerDigging.Action action) {
            if (action == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK)
                return START_DESTROY_BLOCK;
            else if (action == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
                return ABORT_DESTROY_BLOCK;
            else if (action == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
                return STOP_DESTROY_BLOCK;
            else if (action == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS)
                return DROP_ALL_ITEMS;
            else if (action == C07PacketPlayerDigging.Action.DROP_ITEM)
                return DROP_ITEM;
            else
                return RELEASE_USE_ITEM;
        }


        public C07PacketPlayerDigging.Action getAction() {
            if (this == START_DESTROY_BLOCK)
                return C07PacketPlayerDigging.Action.START_DESTROY_BLOCK;
            else if (this == ABORT_DESTROY_BLOCK)
                return C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK;
            else if (this == STOP_DESTROY_BLOCK)
                return C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK;
            else if (this == DROP_ALL_ITEMS)
                return C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
            else if (this == DROP_ITEM)
                return C07PacketPlayerDigging.Action.DROP_ITEM;
            else
                return C07PacketPlayerDigging.Action.RELEASE_USE_ITEM;
        }
    }

}
