/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.selection;

import baritone.api.utils.BetterBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public interface ISelection {
    public BetterBlockPos pos1();

    public BetterBlockPos pos2();

    public BetterBlockPos min();

    public BetterBlockPos max();

    public Vec3i size();

    public AxisAlignedBB aabb();

    public ISelection expand(EnumFacing var1, int var2);

    public ISelection contract(EnumFacing var1, int var2);

    public ISelection shift(EnumFacing var1, int var2);
}

