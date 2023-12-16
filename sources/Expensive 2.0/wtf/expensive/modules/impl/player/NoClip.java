package wtf.expensive.modules.impl.player;

import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventMove;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

/**
 * @author dedinside
 * @since 25.06.2023
 */
@FunctionAnnotation(name = "NoClip", type = Type.Player)
public class NoClip extends Function {

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMove move) {
            if (!collisionPredict(move.to())) {
                if (move.isCollidedHorizontal())
                    move.setIgnoreHorizontalCollision();
                if (move.motion().y > 0 || mc.player.isSneaking()) {
                    move.setIgnoreVerticalCollision();
                }
                move.motion().y = Math.min(move.motion().y, 99999);
            }
        }
    }

    public boolean collisionPredict(Vector3d to) {
        boolean prevCollision = mc.world
                .getCollisionShapes(mc.player, mc.player.getBoundingBox().shrink(0.0625D)).toList().isEmpty();
        Vector3d backUp = new Vector3d(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ());
        mc.player.setPosition(to.x, to.y, to.z);
        boolean collision = mc.world.getCollisionShapes(mc.player, mc.player.getBoundingBox().shrink(0.0625D))
                .toList().isEmpty() && prevCollision;
        mc.player.setPosition(backUp.x, backUp.y, backUp.z);
        return collision;
    }
}
