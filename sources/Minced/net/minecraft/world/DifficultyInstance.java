// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.math.MathHelper;
import javax.annotation.concurrent.Immutable;

@Immutable
public class DifficultyInstance
{
    private final EnumDifficulty worldDifficulty;
    private final float additionalDifficulty;
    
    public DifficultyInstance(final EnumDifficulty worldDifficulty, final long worldTime, final long chunkInhabitedTime, final float moonPhaseFactor) {
        this.worldDifficulty = worldDifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(worldDifficulty, worldTime, chunkInhabitedTime, moonPhaseFactor);
    }
    
    public float getAdditionalDifficulty() {
        return this.additionalDifficulty;
    }
    
    public boolean isHarderThan(final float p_193845_1_) {
        return this.additionalDifficulty > p_193845_1_;
    }
    
    public float getClampedAdditionalDifficulty() {
        if (this.additionalDifficulty < 2.0f) {
            return 0.0f;
        }
        return (this.additionalDifficulty > 4.0f) ? 1.0f : ((this.additionalDifficulty - 2.0f) / 2.0f);
    }
    
    private float calculateAdditionalDifficulty(final EnumDifficulty difficulty, final long worldTime, final long chunkInhabitedTime, final float moonPhaseFactor) {
        if (difficulty == EnumDifficulty.PEACEFUL) {
            return 0.0f;
        }
        final boolean flag = difficulty == EnumDifficulty.HARD;
        float f = 0.75f;
        final float f2 = MathHelper.clamp((worldTime - 72000.0f) / 1440000.0f, 0.0f, 1.0f) * 0.25f;
        f += f2;
        float f3 = 0.0f;
        f3 += MathHelper.clamp(chunkInhabitedTime / 3600000.0f, 0.0f, 1.0f) * (flag ? 1.0f : 0.75f);
        f3 += MathHelper.clamp(moonPhaseFactor * 0.25f, 0.0f, f2);
        if (difficulty == EnumDifficulty.EASY) {
            f3 *= 0.5f;
        }
        f += f3;
        return difficulty.getId() * f;
    }
}
