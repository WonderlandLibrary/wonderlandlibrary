package net.minecraft.util;

import lombok.Getter;

@Getter
public class Vector3d {


    public double x;
    public double y;
    public double z;

    public Vector3d() {
        this.x = this.y = this.z = 0.0D;
    }
}
