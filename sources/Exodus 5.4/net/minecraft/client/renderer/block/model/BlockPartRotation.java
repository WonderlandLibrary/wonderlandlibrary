/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockPartRotation {
    public final boolean rescale;
    public final Vector3f origin;
    public final EnumFacing.Axis axis;
    public final float angle;

    public BlockPartRotation(Vector3f vector3f, EnumFacing.Axis axis, float f, boolean bl) {
        this.origin = vector3f;
        this.axis = axis;
        this.angle = f;
        this.rescale = bl;
    }
}

