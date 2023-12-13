package today.flux.utility;

import net.minecraft.util.Vec3;
import today.flux.addon.api.utils.Rotation;

public class VecRotation {
	Vec3 vec3;
	Rotation rotation;

	public VecRotation(Vec3 Vec3, Rotation Rotation) {
		vec3 = Vec3;
		rotation = Rotation;
	}

	public Vec3 getVec3() {
		return vec3;
	}

	public Rotation getRotation() {
		return rotation;
	}
}
