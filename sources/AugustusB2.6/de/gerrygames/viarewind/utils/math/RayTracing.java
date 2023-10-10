// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils.math;

public class RayTracing
{
    public static Vector3d trace(final Ray3d ray, final AABB aabb, final double distance) {
        final Vector3d invDir = new Vector3d(1.0 / ray.dir.x, 1.0 / ray.dir.y, 1.0 / ray.dir.z);
        final boolean signDirX = invDir.x < 0.0;
        final boolean signDirY = invDir.y < 0.0;
        final boolean signDirZ = invDir.z < 0.0;
        Vector3d bbox = signDirX ? aabb.max : aabb.min;
        double tmin = (bbox.x - ray.start.x) * invDir.x;
        bbox = (signDirX ? aabb.min : aabb.max);
        double tmax = (bbox.x - ray.start.x) * invDir.x;
        bbox = (signDirY ? aabb.max : aabb.min);
        final double tymin = (bbox.y - ray.start.y) * invDir.y;
        bbox = (signDirY ? aabb.min : aabb.max);
        final double tymax = (bbox.y - ray.start.y) * invDir.y;
        if (tmin > tymax || tymin > tmax) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        bbox = (signDirZ ? aabb.max : aabb.min);
        final double tzmin = (bbox.z - ray.start.z) * invDir.z;
        bbox = (signDirZ ? aabb.min : aabb.max);
        final double tzmax = (bbox.z - ray.start.z) * invDir.z;
        if (tmin > tzmax || tzmin > tmax) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        return (tmin <= distance && tmax > 0.0) ? ray.start.clone().add(ray.dir.clone().normalize().multiply(tmin)) : null;
    }
}
