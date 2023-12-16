package wtf.expensive.events.impl.player;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.events.Event;

public class EventMove extends Event     {
    public Vector3d from, to, motion;
    private boolean toGround;
    private AxisAlignedBB aabbFrom;
    public boolean ignoreHorizontal, ignoreVertical, collidedHorizontal, collidedVertical;

    public EventMove(Vector3d from, Vector3d to, Vector3d motion,boolean toGround,
                     boolean isCollidedHorizontal, boolean isCollidedVertical, AxisAlignedBB aabbFrom) {
        this.from = from;
        this.to = to;
        this.motion = motion;
        this.toGround = toGround;
        this.collidedHorizontal = isCollidedHorizontal;
        this.collidedVertical = isCollidedVertical;
        this.aabbFrom = aabbFrom;
    }

    public void setIgnoreHorizontalCollision() {
        this.ignoreHorizontal = true;
    }

    public void setIgnoreVerticalCollision() {
        this.ignoreVertical = true;
    }

    public boolean isIgnoreHorizontal() {
        return this.ignoreHorizontal;
    }

    public AxisAlignedBB getAABBFrom() {
        return this.aabbFrom;
    }

    public boolean isIgnoreVertical() {
        return this.ignoreVertical;
    }

    public boolean isCollidedHorizontal() {
        return this.collidedHorizontal;
    }

    public boolean isCollidedVertical() {
        return this.collidedVertical;
    }

    public boolean toGround() {
        return this.toGround;
    }

    public Vector3d from() {
        return this.from;
    }

    public Vector3d to() {
        return this.to;
    }

    public Vector3d motion() {
        return this.motion;
    }

}
