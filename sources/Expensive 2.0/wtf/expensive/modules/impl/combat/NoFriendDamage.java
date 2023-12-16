package wtf.expensive.modules.impl.combat;

import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CUseEntityPacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

/**
 * @author dedinside
 * @since 27.06.2023
 */
@FunctionAnnotation(name = "No Friend Damage", type = Type.Combat)
public class NoFriendDamage extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket packet) {
            if (packet.getPacket() instanceof CUseEntityPacket useEntityPacket) {
                Entity entity = useEntityPacket.getEntityFromWorld(mc.world);
                if (entity instanceof RemoteClientPlayerEntity
                        && Managment.FRIEND_MANAGER.isFriend(entity.getName().getString())
                        && useEntityPacket.getAction() == CUseEntityPacket.Action.ATTACK) {
                    event.setCancel(true);
                }
            }
        }
    }
}
