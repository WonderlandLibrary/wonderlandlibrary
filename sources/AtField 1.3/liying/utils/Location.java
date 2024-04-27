/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package liying.utils;

import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.particles.Vec3;
import org.jetbrains.annotations.Nullable;

public final class Location {
    private final Vec3 position;
    private final Rotation rotation;

    public final Rotation getRotation() {
        return this.rotation;
    }

    public Location(Vec3 vec3, Rotation rotation) {
        this.position = vec3;
        this.rotation = rotation;
    }

    public static Location copy$default(Location location, Vec3 vec3, Rotation rotation, int n, Object object) {
        if ((n & 1) != 0) {
            vec3 = location.position;
        }
        if ((n & 2) != 0) {
            rotation = location.rotation;
        }
        return location.copy(vec3, rotation);
    }

    public String toString() {
        return "Location(position=" + this.position + ", rotation=" + this.rotation + ")";
    }

    public final Rotation component2() {
        return this.rotation;
    }

    public final Location copy(Vec3 vec3, Rotation rotation) {
        return new Location(vec3, rotation);
    }

    public int hashCode() {
        Vec3 vec3 = this.position;
        Rotation rotation = this.rotation;
        return (vec3 != null ? vec3.hashCode() : 0) * 31 + (rotation != null ? ((Object)rotation).hashCode() : 0);
    }

    public final Vec3 component1() {
        return this.position;
    }

    public final Vec3 getPosition() {
        return this.position;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof Location)) break block3;
                Location location = (Location)object;
                if (!this.position.equals(location.position) || !((Object)this.rotation).equals(location.rotation)) break block3;
            }
            return true;
        }
        return false;
    }
}

