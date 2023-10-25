/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;

@Immutable
public class DifficultyInstance {
    private final EnumDifficulty worldDifficulty;
    private final float additionalDifficulty;

    public DifficultyInstance(EnumDifficulty worldDifficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor) {
        this.worldDifficulty = worldDifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
    }

    public float getAdditionalDifficulty() {
        return this.additionalDifficulty;
    }

    public boolean func_193845_a(float p_193845_1_) {
        return this.additionalDifficulty > p_193845_1_;
    }

    public float getClampedAdditionalDifficulty() {
        if (this.additionalDifficulty < 2.0f) {
            return 0.0f;
        }
        return this.additionalDifficulty > 4.0f ? 1.0f : (this.additionalDifficulty - 2.0f) / 2.0f;
    }

    private float calculateAdditionalDifficulty(EnumDifficulty difficulty, long worldTime, long chunkInhabitedTime, float moonPhaseFactor) {
        if (difficulty == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        boolean flag = difficulty == EnumDifficulty.HARD;
        float f = 0.75f;
        float f1 = MathHelper.clamp(((float)worldTime + -72000.0f) / 1440000.0f, 0.0f, 1.0f) * 0.25f;
        f += f1;
        float f2 = 0.0f;
        f2 += MathHelper.clamp((float)chunkInhabitedTime / 3600000.0f, 0.0f, 1.0f) * (flag ? 1.0f : 0.75f);
        f2 += MathHelper.clamp(moonPhaseFactor * 0.25f, 0.0f, f1);
        if (difficulty == EnumDifficulty.EASY) {
            f2 *= 0.5f;
        }
        return (float)difficulty.getDifficultyId() * (f += f2);
    }
}

