/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;

public class DifficultyInstance {
    private final float additionalDifficulty;
    private final EnumDifficulty worldDifficulty;

    private float calculateAdditionalDifficulty(EnumDifficulty enumDifficulty, long l, long l2, float f) {
        if (enumDifficulty == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        boolean bl = enumDifficulty == EnumDifficulty.HARD;
        float f2 = 0.75f;
        float f3 = MathHelper.clamp_float(((float)l + -72000.0f) / 1440000.0f, 0.0f, 1.0f) * 0.25f;
        f2 += f3;
        float f4 = 0.0f;
        f4 += MathHelper.clamp_float((float)l2 / 3600000.0f, 0.0f, 1.0f) * (bl ? 1.0f : 0.75f);
        f4 += MathHelper.clamp_float(f * 0.25f, 0.0f, f3);
        if (enumDifficulty == EnumDifficulty.EASY) {
            f4 *= 0.5f;
        }
        return (float)enumDifficulty.getDifficultyId() * (f2 += f4);
    }

    public float getClampedAdditionalDifficulty() {
        return this.additionalDifficulty < 2.0f ? 0.0f : (this.additionalDifficulty > 4.0f ? 1.0f : (this.additionalDifficulty - 2.0f) / 2.0f);
    }

    public DifficultyInstance(EnumDifficulty enumDifficulty, long l, long l2, float f) {
        this.worldDifficulty = enumDifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(enumDifficulty, l, l2, f);
    }

    public float getAdditionalDifficulty() {
        return this.additionalDifficulty;
    }
}

