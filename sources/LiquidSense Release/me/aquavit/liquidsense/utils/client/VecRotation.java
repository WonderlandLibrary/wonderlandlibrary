package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.util.Vec3;

public final class VecRotation extends MinecraftInstance {

    private Vec3 vec;
    private Rotation rotation;

    public VecRotation(Vec3 vec,Rotation rotation){
        this.vec = vec;
        this.rotation = rotation;

    }

    public final Vec3 getVec() {
        return this.vec;
    }

    public final void setVec(Vec3 vec) {
        this.vec = vec;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }

    public final void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public final Vec3 component1() {
        return this.vec;
    }

    public final Rotation component2() {
        return this.rotation;
    }
}
