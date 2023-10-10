package today.flux.addon.api.utils;

import com.soterdev.SoterObfuscator;
import lombok.Getter;
import lombok.Setter;

public class Vec3 {
    @Getter @Setter
    public double xCoord, yCoord, zCoord;

    public Vec3(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public static Vec3 getVec3(net.minecraft.util.Vec3 vec) {
        return new Vec3(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    
    public net.minecraft.util.Vec3 getNativeVec3() {
        return new net.minecraft.util.Vec3(xCoord, yCoord, zCoord);
    }
}
