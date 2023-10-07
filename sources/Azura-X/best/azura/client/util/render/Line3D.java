package best.azura.client.util.render;

import net.minecraft.util.Vec3;

public class Line3D {
    private final Vec3 start, end;
    private final long creationTime;
    public Line3D(final Vec3 start, final Vec3 end) {
        this.start = start;
        this.end = end;
        this.creationTime = System.currentTimeMillis();
    }
    public Vec3 getEnd() {
        return end;
    }
    public Vec3 getStart() {
        return start;
    }
    public long getCreationTime() {
        return creationTime;
    }
}