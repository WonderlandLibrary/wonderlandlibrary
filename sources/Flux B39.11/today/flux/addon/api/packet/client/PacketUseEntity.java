package today.flux.addon.api.packet.client;

import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import today.flux.addon.api.entities.AddonEntity;
import today.flux.addon.api.packet.AddonPacket;
import today.flux.addon.api.utils.Vec3;

public class PacketUseEntity extends AddonPacket {

    public PacketUseEntity(C02PacketUseEntity packet) {
        super(packet);
    }

    public PacketUseEntity(int entity, Action action) {
        super(null);
        this.nativePacket = new C02PacketUseEntity(mc.theWorld.getEntityByID(entity), action.getAction());
    }

    public PacketUseEntity(int entity, Vec3 hitVec) {
        this(entity, Action.INTERACT_AT);
        ((C02PacketUseEntity) nativePacket).hitVec = hitVec.getNativeVec3();
    }

    public PacketUseEntity(AddonEntity entity, Action action) {
        this(entity.getEntity().getEntityId(), action);
    }

    public PacketUseEntity(AddonEntity entity, Vec3 hitVec) {
        this(entity, Action.INTERACT_AT);
        ((C02PacketUseEntity) nativePacket).hitVec = hitVec.getNativeVec3();
    }


    public int getEntityId() {
        return ((C02PacketUseEntity) nativePacket).entityId;
    }


    public void setEntityId(int entityId) {
        ((C02PacketUseEntity) nativePacket).entityId = entityId;
    }


    public Action getAction() {
        return Action.getAction(((C02PacketUseEntity) nativePacket).action);
    }


    public void setAction(Action action) {
        ((C02PacketUseEntity) nativePacket).action = action.getAction();
    }


    public Vec3 getHitVec() {
        return Vec3.getVec3(((C02PacketUseEntity) nativePacket).hitVec);
    }


    public void setHitVec(Vec3 hitVec) {
        ((C02PacketUseEntity) nativePacket).hitVec = hitVec.getNativeVec3();
    }

    public enum Action {
        INTERACT,
        ATTACK,
        INTERACT_AT;


        public C02PacketUseEntity.Action getAction() {
            if (this == INTERACT)
                return C02PacketUseEntity.Action.INTERACT;
            else if (this == ATTACK)
                return C02PacketUseEntity.Action.ATTACK;
            else
                return C02PacketUseEntity.Action.INTERACT_AT;
        }

        public static Action getAction(C02PacketUseEntity.Action action) {
            if (action == C02PacketUseEntity.Action.INTERACT)
                return INTERACT;
            else if (action == C02PacketUseEntity.Action.ATTACK)
                return ATTACK;
            else
                return INTERACT_AT;
        }
    }

}
