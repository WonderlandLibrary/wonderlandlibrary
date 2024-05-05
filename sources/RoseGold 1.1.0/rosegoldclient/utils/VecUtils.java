package rosegoldclient.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import rosegoldclient.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class VecUtils {

    private static final HashMap<Integer, KeyBinding> keyBindMap = new HashMap<Integer, KeyBinding>() {
        {
            this.put(0, Main.mc.gameSettings.keyBindForward);
            this.put(90, Main.mc.gameSettings.keyBindLeft);
            this.put(180, Main.mc.gameSettings.keyBindBack);
            this.put(-90, Main.mc.gameSettings.keyBindRight);
        }
    };

    public static Vec3d scaleVec(Vec3d vec3d, float scale) {
        return new Vec3d(vec3d.x * scale, vec3d.y * scale, vec3d.z * scale);
    }

    public static Vec3d floorVec(final Vec3d vec3) {
        return new Vec3d(Math.floor(vec3.x), Math.floor(vec3.y), Math.floor(vec3.z));
    }

    public static Vec3d ceilVec(final Vec3d vec3) {
        return new Vec3d(Math.ceil(vec3.x), Math.ceil(vec3.y), Math.ceil(vec3.z));
    }

    public static double getHorizontalDistance(final Vec3d vec1, final Vec3d vec2) {
        final double d0 = vec1.x - vec2.x;
        final double d2 = vec1.z - vec2.z;
        return MathHelper.sqrt(d0 * d0 + d2 * d2);
    }

    public static ArrayList<KeyBinding> getNeededKeyPresses(final Vec3d from, final Vec3d to) {
        final ArrayList<KeyBinding> e = new ArrayList<>();
        final Rotation neededRot = CheetoRotation.getNeededChange(CheetoRotation.getRotation(from, to));
        final double neededYaw = neededRot.getYaw() * -1.0f;
        VecUtils.keyBindMap.forEach((k, v) -> {
            if (Math.abs(k - neededYaw) < 67.5 || Math.abs(k - (neededYaw + 360.0)) < 67.5) {
                e.add(v);
            }
            return;
        });
        return e;
    }
}
