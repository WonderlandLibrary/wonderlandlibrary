/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.EnumFacing$AxisDirection
 */
package net.dev.important.injection.forge.mixins.optimize;

import java.util.Random;
import net.dev.important.injection.access.StaticStorage;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={EnumFacing.class})
public class MixinEnumFacing {
    @Shadow
    @Final
    private int field_176759_h;

    @Overwrite
    public EnumFacing func_176734_d() {
        return StaticStorage.facings()[this.field_176759_h];
    }

    @Overwrite
    public static EnumFacing func_82600_a(int n) {
        return StaticStorage.facings()[n % StaticStorage.facings().length];
    }

    @Overwrite
    public static EnumFacing func_176741_a(Random random) {
        return StaticStorage.facings()[random.nextInt(StaticStorage.facings().length)];
    }

    @Overwrite
    public static EnumFacing func_176737_a(float f, float f2, float f3) {
        EnumFacing enumFacing = EnumFacing.NORTH;
        float f4 = Float.MIN_VALUE;
        for (EnumFacing enumFacing2 : StaticStorage.facings()) {
            float f5 = f * (float)enumFacing2.func_176730_m().func_177958_n() + f2 * (float)enumFacing2.func_176730_m().func_177956_o() + f3 * (float)enumFacing2.func_176730_m().func_177952_p();
            if (!(f5 > f4)) continue;
            f4 = f5;
            enumFacing = enumFacing2;
        }
        return enumFacing;
    }

    @Overwrite
    public static EnumFacing func_181076_a(EnumFacing.AxisDirection axisDirection, EnumFacing.Axis axis) {
        for (EnumFacing enumFacing : StaticStorage.facings()) {
            if (enumFacing.func_176743_c() != axisDirection || enumFacing.func_176740_k() != axis) continue;
            return enumFacing;
        }
        throw new IllegalArgumentException("No such direction: " + axisDirection + " " + axis);
    }
}

