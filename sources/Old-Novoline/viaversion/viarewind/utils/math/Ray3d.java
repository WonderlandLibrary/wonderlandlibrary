package viaversion.viarewind.utils.math;

public class Ray3d {

	Vector3d start;
	Vector3d dir;

	public Ray3d(Vector3d start, Vector3d dir) {
		this.start = start;
		this.dir = dir;
	}

	public Vector3d getStart() { return start; }
	public Vector3d getDir() { return dir; }
}
